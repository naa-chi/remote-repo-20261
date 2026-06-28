import os, sys, signal, platform

PID_FILE = "microservices.pid"

def kill_pid(pid):
    try:
        if platform.system() == "Windows":
            os.system(f"taskkill /F /PID {pid}")
        else:
            os.kill(pid, signal.SIGTERM)
        print(f"Terminated {pid}")
    except Exception as e:
        print(f"Could not kill PID {pid}: {e}")

def main():
    if not os.path.exists(PID_FILE):
        print("No PID file found. Are there any services running?")
        return
    with open(PID_FILE, "r") as f:
        pids = [line.strip() for line in f if line.strip()]
    if not pids:
        print("PID file is empty. Removing it.")
        os.remove(PID_FILE)
        return
    print(f"Found {len(pids)} running processes. Terminating...")
    for pid in pids:
        kill_pid(int(pid))
    os.remove(PID_FILE)
    print("All microservices have been stopped.")

if __name__ == "__main__":
    main()