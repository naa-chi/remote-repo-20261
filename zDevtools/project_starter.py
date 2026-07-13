#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Start all microservices in the background.
Each service is started with `mvn spring-boot:run` from its directory.
Logs are written to individual files inside `zPythonLogs/` and overwritten on each run.
Press Ctrl+C to stop all services gracefully.

Place this script anywhere (e.g., in a `zDevtools/` subfolder).
It will look for microservice directories one level up by default.
"""

import subprocess
import os
import sys
import signal
import time
import argparse
import platform
from typing import List, Dict
import webbrowser

# ============================================
# CONFIGURATION – add/remove services here
# ============================================

SERVICES: List[Dict[str, str]] = [
    {"name": "Discovery Server", "dir": "discovery"},
    {"name": "Engine Service", "dir": "engines-service"},
    {"name": "Train Service", "dir": "trains-service"},
    {"name": "API Gateway", "dir": "api-gateway"},
    {"name": "Ticket Service", "dir": "tickets-service"},
    {"name": "Review Service", "dir": "reviews-service"},
    {"name": "Maintenances Report Service", "dir": "maintenances-service"},
    {"name": "Manufacturers Service", "dir": "manufacturers-service"},
    {"name": "Stations Service", "dir": "stations-service"},
    {"name": "Lines Service", "dir": "lines-service"},
    {"name": "Cities Service", "dir": "cities-service"},
    {"name": "Managers Service", "dir": "managers-service"},
    {"name": "Drivers Service", "dir": "drivers-service"},
    {"name": "Clients Service", "dir": "clients-service"}
    # Add more services here
]

# ============================================
# SCRIPT
# ============================================

def get_script_dir() -> str:
    return os.path.dirname(os.path.abspath(__file__))

def get_project_root(script_dir: str, cli_root: str = None) -> str:
    if cli_root:
        return os.path.abspath(cli_root)
    return os.path.abspath(os.path.join(script_dir, ".."))

def ensure_log_dir(project_root: str) -> str:
    log_dir = os.path.join(project_root, "zPythonLogs")
    os.makedirs(log_dir, exist_ok=True)
    return log_dir

def start_service(service_name: str, service_dir: str, project_root: str, log_dir: str) -> subprocess.Popen:
    full_path = os.path.join(project_root, service_dir)
    log_file_name = f"{service_name.lower().replace(' ', '_')}.log"
    log_path = os.path.join(log_dir, log_file_name)

    if not os.path.exists(full_path):
        print(f"Service directory not found: {full_path}")
        return None

    # Open log file in write mode (overwrites on each run)
    log_fd = open(log_path, "w")
    print(f"Starting {service_name} (logs -> zPythonLogs/{log_file_name})")

    # On Windows, use mvn.cmd; on Unix, use mvn
    mvn_cmd = "mvn.cmd" if platform.system() == "Windows" else "mvn"

    process = subprocess.Popen(
        [mvn_cmd, "spring-boot:run"],
        cwd=full_path,
        stdout=log_fd,
        stderr=subprocess.STDOUT,
        stdin=subprocess.DEVNULL,
        text=True
    )
    return process

def stop_all_processes(processes: Dict[subprocess.Popen, str]):
    for proc, name in processes.items():
        try:
            print(f"Stopping {name} (PID: {proc.pid})...")
            proc.terminate()
            try:
                proc.wait(timeout=5)
            except subprocess.TimeoutExpired:
                proc.kill()
        except Exception as e:
            print(f"Error stopping {name}: {e}")

def main():
    parser = argparse.ArgumentParser(description="Start all microservices.")
    parser.add_argument("--root", help="Override project root directory.")
    args = parser.parse_args()

    script_dir = get_script_dir()
    project_root = get_project_root(script_dir, args.root)
    log_dir = ensure_log_dir(project_root)

    print(f"Project root: {project_root}")
    print(f"Log directory: {log_dir}")
    print(f"Starting microservices\n")

    processes = {}

    def signal_handler(sig, frame):
        print("\nReceived interrupt. Shutting down all services...")
        stop_all_processes(processes)
        sys.exit(0)

    signal.signal(signal.SIGINT, signal_handler)

    for svc in SERVICES:
        name = svc["name"]
        directory = svc["dir"]
        proc = start_service(name, directory, project_root, log_dir)
        if proc:
            processes[proc] = name

    if not processes:
        print("❌ No services were started. Exiting.")
        return

    print(f"\nAll services started. Running {len(processes)} processes.")
    print("Press Ctrl+C to stop all services and exit.\n")

    print("docs are here: \n http://localhost:8080/swagger-ui/index.html?urls.primaryName=Drivers+Service")

    try:
        while True:
            for proc in list(processes.keys()):
                if proc.poll() is not None:
                    name = processes.pop(proc)
                    print(f"{name} (PID: {proc.pid}) exited unexpectedly.")
            time.sleep(1)
    except KeyboardInterrupt:
        pass
    finally:
        stop_all_processes(processes)
        print("Goodbye.")

if __name__ == "__main__":
    main()