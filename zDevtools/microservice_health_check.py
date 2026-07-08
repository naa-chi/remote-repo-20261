#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import json
import requests
import sys
import time
import argparse
import os
from datetime import datetime
from typing import Dict, List, Tuple, Optional

def get_script_dir() -> str:
    """Return the directory where this script is located."""
    return os.path.dirname(os.path.abspath(__file__))

def get_project_root() -> str:
    """Return the project root (parent of the directory containing this script)."""
    script_dir = get_script_dir()
    return os.path.abspath(os.path.join(script_dir, ".."))

def timestamp() -> str:
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")

def call_endpoint(url: str, method: str = "GET", json_data: dict = None, timeout: int = 3):
    try:
        method = method.upper()
        if method == "GET":
            resp = requests.get(url, timeout=timeout, allow_redirects=False)
        elif method == "POST":
            resp = requests.post(url, json=json_data, timeout=timeout, allow_redirects=False)
        else:
            return (False, None, "", f"Unsupported method: {method}")
        preview = resp.text[:200] if resp.text else ""
        ok = resp.status_code < 400
        return (ok, resp.status_code, preview, None)
    except Exception as e:
        return (False, None, "", str(e))

def build_log_entry(level: str, url: str, status: Optional[int], ok: bool, error: Optional[str] = None, preview: str = ""):
    status_str = f"{status}" if status is not None else "N/A"
    if ok:
        en = f"[{timestamp()}] [OK] {url} -> {status_str} {preview}"
        zh = f"[{timestamp()}] [成功] {url} -> {status_str} {preview}"
    else:
        en = f"[{timestamp()}] [FAIL] {url} -> {status_str} Error: {error or 'Unknown'}"
        zh = f"[{timestamp()}] [失败] {url} -> {status_str} 错误: {error or '未知'}"
    return {"en": en, "zh": zh}

def write_logs(filename: str, header: str, en_lines: List[str], zh_lines: List[str], log_dir: str):
    os.makedirs(log_dir, exist_ok=True)
    filepath = os.path.join(log_dir, filename)
    with open(filepath, "a", encoding="utf-8") as f:
        f.write(header + "\n")
        for line in en_lines:
            f.write("[EN] " + line + "\n")
        for line in zh_lines:
            f.write("[CN] " + line + "\n")
        f.write("\n")

def main():
    parser = argparse.ArgumentParser(description="Health check for microservices.")
    parser.add_argument("-c", "--config", help="Path to services_config.json (default: script directory -> project root)")
    parser.add_argument("-g", "--gateway", help="Override gateway URL")
    parser.add_argument("-t", "--timeout", type=int, default=3)
    parser.add_argument("--logs-dir", help="Directory for log files (default: zPythonLogs in project root)")
    args = parser.parse_args()

    project_root = get_project_root()
    script_dir = get_script_dir()
    logs_dir = args.logs_dir or os.path.join(project_root, "zPythonLogs")

    # Determine config file location
    if args.config:
        config_path = args.config
    else:
        # First try in the script directory (zDevtools)
        config_path = os.path.join(script_dir, "services_config.json")
        if not os.path.exists(config_path):
            # Fallback to project root
            config_path = os.path.join(project_root, "services_config.json")

    try:
        with open(config_path, "r", encoding="utf-8") as f:
            config = json.load(f)
    except FileNotFoundError:
        print(f"❌ Config file not found: {config_path}")
        sys.exit(1)
    except json.JSONDecodeError as e:
        print(f"❌ Invalid JSON: {e}")
        sys.exit(1)

    gateway_url = args.gateway or config.get("gateway_url", "http://localhost:8080")
    services = config.get("services", [])
    if not services:
        print("⚠️ No services defined.")
        return

    print(f"🚀 Starting health check at {timestamp()}")
    print(f"Gateway: {gateway_url}")
    print(f"Logs directory: {logs_dir}")
    print(f"Config file: {config_path}")

    all_gateway_en, all_gateway_zh = [], []
    all_micro_en, all_micro_zh = [], []

    for svc in services:
        name = svc.get("name", "Unnamed")
        base = svc.get("base_url")
        route = svc.get("route_prefix")
        endpoints = svc.get("endpoints", [])
        if not base or not route:
            print(f"⚠️ Skipping {name}: missing base_url or route_prefix.")
            continue

        print(f"\n==== Testing {name} ====")
        print(f"  Direct base: {base}")
        print(f"  Route prefix: {route}")

        for ep in endpoints:
            g_path = ep.get("gateway_path")
            d_path = ep.get("direct_path")
            if g_path is None and d_path is None:
                common = ep.get("path")
                if common is None:
                    print(f"⚠️ Skipping endpoint: no path defined")
                    continue
                g_path = d_path = common
            elif g_path is None:
                g_path = d_path
            elif d_path is None:
                d_path = g_path

            method = ep.get("method", "GET")
            json_data = ep.get("json_data", {}) if method.upper() == "POST" else None

            # Gateway test
            gw_url = f"{gateway_url}{route}{g_path}"
            ok_gw, status_gw, preview_gw, err_gw = call_endpoint(gw_url, method, json_data, args.timeout)
            entry_gw = build_log_entry("Gateway", gw_url, status_gw, ok_gw, err_gw, preview_gw)
            all_gateway_en.append(entry_gw["en"])
            all_gateway_zh.append(entry_gw["zh"])
            print(f"  Gateway: {gw_url} -> {status_gw if status_gw else 'ERR'}")

            # Direct test
            dr_url = f"{base}{d_path}"
            ok_dr, status_dr, preview_dr, err_dr = call_endpoint(dr_url, method, json_data, args.timeout)
            entry_dr = build_log_entry("Direct", dr_url, status_dr, ok_dr, err_dr, preview_dr)
            all_micro_en.append(entry_dr["en"])
            all_micro_zh.append(entry_dr["zh"])
            print(f"  Direct : {dr_url} -> {status_dr if status_dr else 'ERR'}")

            time.sleep(0.2)

    header = f"=== Test run at {timestamp()} ==="
    write_logs("Api-Gateway-Logging.txt", header, all_gateway_en, all_gateway_zh, logs_dir)
    write_logs("Microservice-Endpoint-Logging.txt", header, all_micro_en, all_micro_zh, logs_dir)

    print("\n✅ Testing complete. Logs written to:")
    print(f"  - {os.path.join(logs_dir, 'Api-Gateway-Logging.txt')}")
    print(f"  - {os.path.join(logs_dir, 'Microservice-Endpoint-Logging.txt')}")

if __name__ == "__main__":
    main()