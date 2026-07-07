#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import json
import requests
import sys
import time
import argparse
from datetime import datetime
from typing import Dict, List, Tuple, Optional

DEFAULT_CONFIG_FILE = "services_config.json"
GATEWAY_LOG_FILE = "Api-Gateway-Logging.txt"
MICROSERVICE_LOG_FILE = "Microservice-Endpoint-Logging.txt"

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

def write_logs(filename: str, header: str, en_lines: List[str], zh_lines: List[str]):
    with open(filename, "a", encoding="utf-8") as f:
        f.write(header + "\n")
        for line in en_lines:
            f.write("[EN] " + line + "\n")
        for line in zh_lines:
            f.write("[CN] " + line + "\n")
        f.write("\n")

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("-c", "--config", default=DEFAULT_CONFIG_FILE)
    parser.add_argument("-g", "--gateway", help="Override gateway URL")
    parser.add_argument("-t", "--timeout", type=int, default=3)
    args = parser.parse_args()

    try:
        with open(args.config, "r", encoding="utf-8") as f:
            config = json.load(f)
    except FileNotFoundError:
        print(f"❌ Config file '{args.config}' not found.")
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
            # If only 'path' is provided, use it for both
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
    write_logs(GATEWAY_LOG_FILE, header, all_gateway_en, all_gateway_zh)
    write_logs(MICROSERVICE_LOG_FILE, header, all_micro_en, all_micro_zh)

    print("\n✅ Testing complete. Logs written to:")
    print(f"  - {GATEWAY_LOG_FILE}")
    print(f"  - {MICROSERVICE_LOG_FILE}")

if __name__ == "__main__":
    main()