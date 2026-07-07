import os
import subprocess
import time
import sys
import platform

# Infrastructure Connectivity Settings
DB_HOST = os.environ.get('DB_HOST', 'localhost')
DB_PORT = int(os.environ.get('DB_PORT', '3306'))
DB_USER = os.environ.get('DB_USER', 'root')
DB_PASSWORD = os.environ.get('DB_PASSWORD', '')

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
START_DELAY = 6
IS_WINDOWS = platform.system() == "Windows"

def discover_microservices():
    """Scans the immediate directory structure to locate active Maven submodules."""
    services = []
    for item in os.listdir(ROOT_DIR):
        item_path = os.path.join(ROOT_DIR, item)
        if os.path.isdir(item_path) and os.path.exists(os.path.join(item_path, "pom.xml")):
            services.append(item)
    return services

def setup_persistence_layers(services):
    """Dynamically provisions physical relational schema structures on the target RDBMS."""
    print("--- STEP 1: PROVISIONING PERSISTENCE SUBSYSTEMS ---")
    try:
        import pymysql
    except ImportError:
        print("[INFO] 'pymysql' dependency missing. Attempting automated installation...")
        subprocess.run([sys.executable, "-m", "pip", "install", "pymysql"], check=True, stdout=subprocess.DEVNULL)
        import pymysql

    try:
        conn = pymysql.connect(
            host=DB_HOST, port=DB_PORT, user=DB_USER, password=DB_PASSWORD, charset='utf8mb4', connect_timeout=5
        )
        cursor = conn.cursor()
        for service in services:
            if "gateway" in service.lower() or service == "api-gateway":
                continue
            db_name = f"transport_db_{service}"
            cursor.execute(f"CREATE DATABASE IF NOT EXISTS `{db_name}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;")
            print(f"  ✓ [READY] Target Persator Schema: '{db_name}'")
        cursor.close()
        conn.close()
        print("[SUCCESS] Persistence initialization complete.\n")
    except Exception as e:
        print(f"[CRITICAL] Connection interface to database engine failed: {e}")
        sys.exit(1)

def launch_microservice(service_name):
    """Spawns an isolated command process window executing spring-boot:run."""
    service_path = os.path.join(ROOT_DIR, service_name)
    mvn_cmd = "mvnw.cmd" if IS_WINDOWS else "./mvnw"
    
    if not os.path.exists(os.path.join(service_path, mvn_cmd.replace("./", ""))):
        mvn_cmd = "mvn"

    if IS_WINDOWS:
        cmd_str = f'start "{service_name}" cmd /k "cd /d {service_path} && {mvn_cmd} spring-boot:run -Dmaven.test.skip=true"'
        subprocess.Popen(cmd_str, shell=True)
    else:
        # Fallback terminal execution context for POSIX compliant platforms
        subprocess.Popen(f'cd "{service_path}" && {mvn_cmd} spring-boot:run -Dmaven.test.skip=true', shell=True)

def main():
    print("=================================================================")
    print("          DYNAMIC INFRASTRUCTURE ORCHESTRATION ENGINE            ")
    print("=================================================================\n")

    microservices = discover_microservices()
    if not microservices:
        print("[ERROR] No valid Maven project subdirectories detected. Topology aborted.")
        sys.exit(1)

    print(f"[INFO] Discovered {len(microservices)} microservice modules: {microservices}\n")
    
    setup_persistence_layers(microservices)

    print("--- STEP 2: RUNTIME APPLICATION DEPLOYMENT ---")
    
    # Sort array to boot edge routers last if present
    functional_nodes = [s for s in microservices if "gateway" not in s.lower()]
    gateway_nodes = [s for s in microservices if "gateway" in s.lower()]

    for node in functional_nodes:
        print(f"[LAUNCHING] Initializing {node} context space...")
        launch_microservice(node)
        time.sleep(START_DELAY)

    for gateway in gateway_nodes:
        print(f"[LAUNCHING] Deploying Edge Routing Interface: {gateway}...")
        launch_microservice(gateway)

    print("\n=================================================================")
    print("Topology sequence finalized. Monitor external console views.")
    print("=================================================================")

if __name__ == "__main__":
    main()