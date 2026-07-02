import os
import subprocess
import time
import sys
import platform

# Import shared service list
from central import STARTUP_ORDER

# Runtime Constants
DB_HOST = os.environ.get('DB_HOST', 'localhost')
DB_PORT = int(os.environ.get('DB_PORT', '3306'))
DB_USER = os.environ.get('DB_USER', 'root')
DB_PASSWORD = os.environ.get('DB_PASSWORD', '')

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
START_DELAY = .5
IS_WINDOWS = platform.system() == "Windows"

def setup_databases(services):
    print("--- STEP 1: INITIALIZING DATABASES ---")
    try:
        import pymysql
    except ImportError:
        print("[ERROR] Database module missing. Run 'pip install pymysql' first.")
        sys.exit(1)

    try:
        conn = pymysql.connect(
            host=DB_HOST, port=DB_PORT, user=DB_USER, password=DB_PASSWORD, charset='utf8mb4'
        )
        cursor = conn.cursor()
        for service in services:
            if service == "api-gateway":
                continue
            db_name = f"transport_db_{service}"
            cursor.execute(f"CREATE DATABASE IF NOT EXISTS `{db_name}` CHARACTER SET utf8mb4;")
            print(f" ✓ Database cluster storage verified: '{db_name}'")
        cursor.close()
        conn.close()
        print("All target database nodes synchronized successfully.\n")
    except Exception as e:
        print(f"[CRITICAL] Database storage layer handshakes failed: {e}")
        sys.exit(1)

def run_service(service_name):
    """Executes the microservice via spring-boot:run in an isolated terminal, skipping tests."""
    service_path = os.path.join(ROOT_DIR, service_name)
    
    if not os.path.exists(service_path):
        print(f"[ERROR] Workspace directory missing for: {service_name}")
        return False

    mvn_cmd = "mvnw.cmd" if IS_WINDOWS else "./mvnw"
    if not os.path.exists(os.path.join(service_path, mvn_cmd)):
        mvn_cmd = "mvn"

    if IS_WINDOWS:
        cmd_str = f'start "{service_name}" cmd /k "cd /d {service_path} && {mvn_cmd} spring-boot:run -Dmaven.test.skip=true"'
        subprocess.Popen(cmd_str, shell=True)
    else:
        subprocess.Popen(f'cd "{service_path}" && {mvn_cmd} spring-boot:run -Dmaven.test.skip=true', shell=True)

    return True

def main():
    print("=================================================================")
    print("           LIVE DEVELOPMENT SYSTEM INITIALIZER                   ")
    print("=================================================================\n")

    setup_databases(STARTUP_ORDER)

    print("--- STEP 2: INFRASTRUCTURE RUNTIME DEPLOYMENT ---")

    for service in STARTUP_ORDER:
        if run_service(service):
            print(f"[LAUNCHED] Node '{service}' executing via Maven. Stabilizing vector ({START_DELAY}s)...")
            time.sleep(START_DELAY)

    print("[EDGE PROXY] Initializing api-gateway module mapping rules...")
    if run_service("api-gateway"):
        print("[LAUNCHED] Edge api-gateway executed.")

    print("\n=================================================================")
    print("Initialization sequence finalized.")
    print("To terminate all services, close the individual terminal windows.")
    print("=================================================================")

if __name__ == "__main__":
    main()