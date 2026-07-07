import os
import subprocess
import sys
import platform
from concurrent.futures import ThreadPoolExecutor

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
IS_WINDOWS = platform.system() == "Windows"

def purge_and_rebuild_module(service_name):
    """Purges intermediate build targets and aggressively pulls dependency modifications."""
    service_path = os.path.join(ROOT_DIR, service_name)
    mvn_cmd = "mvnw.cmd" if IS_WINDOWS else "./mvnw"
    
    if not os.path.exists(os.path.join(service_path, mvn_cmd.replace("./", ""))):
        mvn_cmd = "mvn"

    print(f"[REBUILD] Beginning clean dependency resolution for: {service_name}")
    
    cmd = [mvn_cmd, "clean", "dependency:purge-local-repository", "compile", "-Dmaven.test.skip=true", "-U", "-DactTransitively=false", "-DreResolve=false"]
    fallback_cmd = [mvn_cmd, "clean", "compile", "-Dmaven.test.skip=true", "-U"]

    try:
        # Added shell=IS_WINDOWS to correctly resolve global 'mvn' boundaries on Windows systems
        process = subprocess.run(cmd, cwd=service_path, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=IS_WINDOWS)
        if process.returncode != 0:
            print("Error, using fallback (compiling)")
            process = subprocess.run(fallback_cmd, cwd=service_path, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=IS_WINDOWS)
            
        if process.returncode == 0:
            print(f"  ✓ [SUCCESS] Classpath storage tree verified for: {service_name}")
            return True
        else:
            print(f"  ❌ [FAILED] Refactoring fault inside context: {service_name}\n{process.stderr.decode('utf-8')}")
            return False
    except Exception as e:
        print(f"  ❌ [ERROR] Fatal execution failure on module {service_name}: {e}")
        return False

def main():
    print("=================================================================")
    print("             MAVEN ARCHETYPE RE-RESOLUTION ENGINE                ")
    print("=================================================================\n")

    services = [item for item in os.listdir(ROOT_DIR) if os.path.isdir(os.path.join(ROOT_DIR, item)) and os.path.exists(os.path.join(ROOT_DIR, item, "pom.xml"))]
    
    if not services:
        print("[ERROR] No submodules mapped. Execution terminated.")
        sys.exit(1)

    print(f"[INFO] Concurrent compilation initialized for targets: {services}\n")

    with ThreadPoolExecutor() as executor:
        results = list(executor.map(purge_and_rebuild_module, services))

    print("\n=================================================================")
    if all(results):
        print(" Cache synchronization finalized. System is cleared to run startup.py.")
    else:
        print(" Rebuild operations completed with compilation warnings or failures.")
    print("=================================================================")

if __name__ == "__main__":
    main()