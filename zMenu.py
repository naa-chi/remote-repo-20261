import subprocess, sys, time, os, shutil
ROOT_DIR = os.path.dirname(os.path.abspath(__file__))


def start_all():
    logs_folder = os.path.join(ROOT_DIR, "zzLogs") #removes logs every time it gets executed to have it be constantly refreshed
    if os.path.exists(logs_folder):
        shutil.rmtree(logs_folder)

    if os.path.exists(os.path.join(ROOT_DIR, "microservices.pid")):
        print("You tried to start it again without terminating previous microservices. Terminating.")
        menu()
    else:
        print("\nRun all..")
        time.sleep(1)
        print("Run all...")
        time.sleep(1)
        print("Run all....")
        time.sleep(1)
        print("Run all.....\n")
        time.sleep(1)
        subprocess.run(["python", "zAPIStarter.py"])

def stop_all():
    print("\nStopping all..")
    time.sleep(1)
    print("Stopping all...")
    time.sleep(1)
    print("Stopping all....")
    time.sleep(1)
    print("Stopping all.....\n")
    time.sleep(1)
    subprocess.run(["python", "zTerminateMicroservices.py"])


def clear_console():
    os.system('cls' if os.name == 'nt' else 'clear')

def menu():
    try:
        while True:
            print("\n! Microservices Controller !")
            print("1 - Start all services")
            print("2 - Stop all services")
            print("3 - Exit")
            choice = input("Choose an option (1/2/3): ").strip()
            if choice == "1":
                start_all()
                time.sleep(3)
                clear_console()
            elif choice == "2":
                stop_all()
                time.sleep(3)
                clear_console()
            elif choice == "3":
                print("Goodbye!")
                sys.exit(0)
            else:
                print("Invalid choice. Please enter 1, 2, or 3.")
                menu()
    except KeyboardInterrupt:
        print("Terminating")

if __name__ == "__main__":
    while True:
        menu()