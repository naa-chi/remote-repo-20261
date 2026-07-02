import os
import sys
import time
import subprocess

from central import STARTUP_ORDER

DB_HOST = os.environ.get('DB_HOST', 'localhost')
DB_PORT = int(os.environ.get('DB_PORT', '3306'))
DB_USER = os.environ.get('DB_USER', 'root')
DB_PASSWORD = os.environ.get('DB_PASSWORD', '')

def clear_and_recreate_databases():
    print("=================================================================")
    print("              DATABASE RESTRUCTURING ENGINE                      ")
    print("=================================================================\n")

    try:
        import pymysql
    except ImportError:
        print("[INFO] 'pymysql' package missing. Installing...")
        subprocess.run([sys.executable, "-m", "pip", "install", "pymysql"], check=True, stdout=subprocess.DEVNULL)
        import pymysql

    try:
        conn = pymysql.connect(
            host=DB_HOST,
            port=DB_PORT,
            user=DB_USER,
            password=DB_PASSWORD,
            charset='utf8mb4'
        )
        cursor = conn.cursor()
        
        for service in STARTUP_ORDER:
            db_name = f"transport_db_{service}"
            
            cursor.execute(f"DROP DATABASE IF EXISTS `{db_name}`;")
            print(f" :x: [DROPPED] Wiped catalog node: '{db_name}'")
            
            cursor.execute(f"CREATE DATABASE `{db_name}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;")
            print(f"  ✓ [CREATED] Provisioned clean context: '{db_name}'")
            
        cursor.close()
        conn.close()
        print("\n[SUCCESS] All databases successfully purged and reset.")
    except Exception as e:
        print(f"\n[CRITICAL] Database structural processing failed: {e}")
        sys.exit(1)

clear_and_recreate_databases()