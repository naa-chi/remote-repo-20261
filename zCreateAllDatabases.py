import os
import subprocess
import time
import sys
import platform
from concurrent.futures import ThreadPoolExecutor

import zMenu

DB_HOST = os.environ.get('DB_HOST', 'localhost')
DB_PORT = int(os.environ.get('DB_PORT', '3306'))
DB_USER = os.environ.get('DB_USER', 'root')
DB_PASSWORD = os.environ.get('DB_PASSWORD', '')
ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
PID_FILE = os.path.join(ROOT_DIR, "microservices.pid")
LOG_DIR = os.path.join(ROOT_DIR, "zzLogs")          #Keeps logs in here as well

running_pids = []

STARTUP_ORDER = [
    "auth", "city", "client", "driver", "line", "maintenance",
    "manufacturer", "review", "station", "ticket", "train", "typeengine"
]

start_delay = 8
osPlatform = platform.system() == "Windows"


def create_database_for_service(service_name):
    db_name = f"transport_db_{service_name}"
    
    if service_name == "api-gateway":
        print(f"No need for '{service_name}' (no DB needed).")
        return True
    #skips unnecessary db
    
    try:
        import pymysql
    except ImportError:
        print("Run 'pip install pymysql' in your terminal first. Won't work without it")
        sys.exit(1)

    try:
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
        print(f"Db '{db_name}' creation failed: {e}")
        print("Check MySQL server, credentials, and that you're not attempting api-gateway.")
        return False


def setup_all_databases(services):
    print("\nChecking && creating databases...")
    all_ok = True
    for service in services:
        if not create_database_for_service(service['name']):
            all_ok = False
    if not all_ok:
        print("Db failed. Terminating.")
        sys.exit(1)
    print("All required databases are ready.\n")


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
    print(f"Launching [{service['name']}]!")
    if service['type'] == "maven":
        if osPlatform:
            wrapper = os.path.join(service['path'], "mvnw.cmd")
            cmd = [wrapper, "clean", "package", "-DskipTests"] if os.path.exists(wrapper) else ["mvn", "clean", "package", "-DskipTests"]
        else:
            wrapper = os.path.join(service['path'], "mvnw")
            cmd = [wrapper, "clean", "package", "-DskipTests"] if os.path.exists(wrapper) else ["mvn", "clean", "package", "-DskipTests"]
    else: 
        if osPlatform:
            wrapper = os.path.join(service['path'], "gradlew.bat")
            cmd = [wrapper, "build", "-x", "test"] if os.path.exists(wrapper) else ["gradle", "build", "-x", "test"]
        else:
            wrapper = os.path.join(service['path'], "gradlew")
            cmd = [wrapper, "build", "-x", "test"] if os.path.exists(wrapper) else ["gradle", "build", "-x", "test"]

    process = subprocess.run(cmd, cwd=service['path'], stdout=subprocess.DEVNULL, stderr=subprocess.PIPE)
    if process.returncode == 0:
        print(f"[{service['name']}] has launched successfully.")
        return True
    else:
        print(f"[{service['name']}] failed to build. Error:\n{process.stderr.decode('utf-8')}")
        return False


def run_service(service):
    print(f"Initializing [{service['name']}]...")
    jar_dir = os.path.join(service['path'], "target" if service['type'] == "maven" else "build/libs")
    if not os.path.exists(jar_dir):
        print(f"Dir not found [{service['name']}]?")
        return None
    jar_files = [f for f in os.listdir(jar_dir) if f.endswith(".jar") and not f.endswith("-plain.jar")]
    if not jar_files:
        print(f"No executable JAR found for [{service['name']}]")
        return None
    jar_path = os.path.join(jar_dir, jar_files[0])

    os.makedirs(LOG_DIR, exist_ok=True)
    log_path = os.path.join(LOG_DIR, f"{service['name']}.log")
    log_file = open(log_path, "w")

    log_file_abs = os.path.join(ROOT_DIR, "zzLogs", f"{service['name']}.log")
    process = subprocess.Popen(
        ["java", f"-Dlogging.file.name={log_file_abs}", "-jar", jar_path],
        stdout=log_file,
        stderr=log_file,
        text=True
    )

    running_pids.append(process.pid)
    with open(PID_FILE, "w") as f:
        for pid in running_pids:
            f.write(str(pid) + "\n")
    return process

def main():
    print("Searching for spring micros")
    all_services = find_microservices(ROOT_DIR)
    if not all_services:
        print("No microservices found. Ensure this script is placed in the root directory.")
        sys.exit(1)
    print(f"Found {len(all_services)} services: {[s['name'] for s in all_services]}")

    setup_all_databases(all_services)

    print("\nStep 1: initializing microservices")
    with ThreadPoolExecutor() as executor:
        results = list(executor.map(build_service, all_services))
    if not all(results):
        print("\n❌ Some services failed to build. Stopping.")
        sys.exit(1)

    print("\nStep 2: starting 'em")
    running_processes = []
    ordered_services = [s for name in STARTUP_ORDER for s in all_services if s['name'] == name]
    remaining_services = [s for s in all_services if s['name'] not in STARTUP_ORDER]

    for service in ordered_services:
        proc = run_service(service)
        if proc:
            running_processes.append(proc)
            print(f"Letting microservice launch for {start_delay}s for infrastructure to settle...")
            time.sleep(start_delay)

    for service in remaining_services:
        proc = run_service(service)
        if proc:
            running_processes.append(proc)

    print("\nstarting!\n")

    logs_folder = os.path.join(ROOT_DIR, "zzLogs")

    try:
        zMenu.menu() #Yeah
    except KeyboardInterrupt:
        print("\nTerminating microservices")
        for proc in running_processes:
            proc.terminate()
        if os.path.exists(PID_FILE):
            os.remove(PID_FILE)
        print("Microservices terminated.")


if __name__ == "__main__":
    main()