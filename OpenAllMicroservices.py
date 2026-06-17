import os
import subprocess
import time
import sys
import platform
from concurrent.futures import ThreadPoolExecutor

DB_HOST = os.environ.get('DB_HOST', 'localhost')
DB_PORT = int(os.environ.get('DB_PORT', '3306'))
DB_USER = os.environ.get('DB_USER', 'root')
DB_PASSWORD = os.environ.get('DB_PASSWORD', '')
ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

STARTUP_ORDER = [
    "auth", 
    "city", 
    "client", 
    "driver", 
    "line", 
    "maintenance",
    "manufacturer", 
    "review", 
    "station", 
    "ticket", 
    "train", 
    "typeengine"
]

BOOT_DELAY = 12 #If it's too low you can have multiple microservices trying to run at the same time which may be resource intensive

IS_WINDOWS = platform.system() == "Windows"


def create_database_for_service(service_name):
    """
    Creates database 'transport_db_<service_name>' if it does not exist.
    Uses PyMySQL (pure Python MySQL client).
    """
    db_name = f"transport_db_{service_name}"
    try:
        import pymysql
    except ImportError:
        print("ERROR: PyMySQL is not installed. Run 'pip install pymysql' and try again.")
        sys.exit(1)

    try:
        # Connect without selecting a database
        conn = pymysql.connect(
            host=DB_HOST,
            port=DB_PORT,
            user=DB_USER,
            password=DB_PASSWORD,
            charset='utf8mb4'
        )
        cursor = conn.cursor()
        cursor.execute(f"CREATE DATABASE IF NOT EXISTS `{db_name}`")
        print(f"✓ Database '{db_name}' is ready.")
        cursor.close()
        conn.close()
        return True
    except Exception as e:
        print(f"Db '{db_name}' has not been lifted: {e}")
        print("Make sure your MySQL server is running and user/pswd are correct.")
        return False


def setup_all_databases(services):
    """Creates a database for every microservice."""
    print("\n--- Phase 0: Ensuring databases exist ---")
    all_ok = True
    for service in services:
        if not create_database_for_service(service['name']):
            all_ok = False
    if not all_ok:
        print("Database setup failed. Aborting.")
        sys.exit(1)
    print("All databases are ready.\n")


# -------------------------------------------------------------------
# Microservice discovery & build (cross‑platform, as before)
# -------------------------------------------------------------------
def find_microservices(root_path):
    services = []
    for item in os.listdir(root_path):
        item_path = os.path.join(root_path, item)
        if os.path.isdir(item_path):
            has_maven = os.path.exists(os.path.join(item_path, "pom.xml"))
            has_gradle = os.path.exists(os.path.join(item_path, "build.gradle")) or \
                         os.path.exists(os.path.join(item_path, "build.gradle.kts"))
            if has_maven or has_gradle:
                services.append({
                    "name": item,
                    "path": item_path,
                    "type": "maven" if has_maven else "gradle"
                })
    return services


def build_service(service):
    print(f"Building [{service['name']}]")
    if service['type'] == "maven":
        if IS_WINDOWS:
            wrapper = os.path.join(service['path'], "mvnw.cmd")
            cmd = [wrapper, "clean", "package", "-DskipTests"] if os.path.exists(wrapper) else ["mvn", "clean", "package", "-DskipTests"]
        else:
            wrapper = os.path.join(service['path'], "mvnw")
            cmd = [wrapper, "clean", "package", "-DskipTests"] if os.path.exists(wrapper) else ["mvn", "clean", "package", "-DskipTests"]
    else:  # gradle
        if IS_WINDOWS:
            wrapper = os.path.join(service['path'], "gradlew.bat")
            cmd = [wrapper, "build", "-x", "test"] if os.path.exists(wrapper) else ["gradle", "build", "-x", "test"]
        else:
            wrapper = os.path.join(service['path'], "gradlew")
            cmd = [wrapper, "build", "-x", "test"] if os.path.exists(wrapper) else ["gradle", "build", "-x", "test"]

    process = subprocess.run(cmd, cwd=service['path'], stdout=subprocess.DEVNULL, stderr=subprocess.PIPE)
    if process.returncode == 0:
        print(f"[{service['name']}] built successfully.")
        return True
    else:
        print(f"Failed to build [{service['name']}]. Error:\n{process.stderr.decode('utf-8')}")
        return False


def run_service(service):
    print(f"Opening [{service['name']}]...")
    jar_dir = os.path.join(service['path'], "target" if service['type'] == "maven" else "build/libs")
    if not os.path.exists(jar_dir):
        print(f"Could not find build directory for [{service['name']}]")
        return None
    jar_files = [f for f in os.listdir(jar_dir) if f.endswith(".jar") and not f.endswith("-plain.jar")]
    if not jar_files:
        print(f"No executable JAR found for [{service['name']}]")
        return None
    jar_path = os.path.join(jar_dir, jar_files[0])
    log_file = open(os.path.join(service['path'], f"{service['name']}.log"), "w")
    process = subprocess.Popen(
        ["java", "-jar", jar_path],
        stdout=log_file,
        stderr=log_file,
        text=True
    )
    return process


# -------------------------------------------------------------------
# Main
# -------------------------------------------------------------------
def main():
    print("Scanning for Spring Microservices...")
    all_services = find_microservices(ROOT_DIR)
    if not all_services:
        print("No microservices found. Ensure this script is placed in the root directory.")
        sys.exit(1)
    print(f"Found {len(all_services)} microservices: {[s['name'] for s in all_services]}")

    # --- Create databases for each microservice ---
    setup_all_databases(all_services)

    # --- Build phase (parallel) ---
    print("\n--- Phase 1: Building Services ---")
    with ThreadPoolExecutor() as executor:
        results = list(executor.map(build_service, all_services))
    if not all(results):
        print("\nSome services failed to build. Stopping execution loop.")
        sys.exit(1)

    # --- Run phase (sequential with delay for ordered services) ---
    print("\n--- Phase 2: Starting Services ---")
    running_processes = []
    ordered_services = [s for name in STARTUP_ORDER for s in all_services if s['name'] == name]
    remaining_services = [s for s in all_services if s['name'] not in STARTUP_ORDER]

    for service in ordered_services:
        proc = run_service(service)
        if proc:
            running_processes.append(proc)
            print(f"Waiting {BOOT_DELAY}s for infrastructure initialization...")
            time.sleep(BOOT_DELAY)

    for service in remaining_services:
        proc = run_service(service)
        if proc:
            running_processes.append(proc)

    print("\nAll microservices are initializing! Press Ctrl+C to terminate all services.")

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nShutting down all microservices gracefully...")
        for proc in running_processes:
            proc.terminate()
        print("Active processes cleared.")


if __name__ == "__main__":
    main()