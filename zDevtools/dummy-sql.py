#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Populate the 'engines' and 'trains' tables with 10 rows each,
using random but valid data that satisfies the validation constraints.
"""

import random
import string
from datetime import date, timedelta
import pymysql

# Database connection details
DB_CONFIG_ENGINES = {
    'host': 'localhost',
    'user': 'root',
    'password': '',
    'database': 'transport_db_enginesservice',
    'charset': 'utf8mb4'
}

DB_CONFIG_TRAINS = {
    'host': 'localhost',
    'user': 'root',
    'password': '',
    'database': 'transport_db_trainsservice',
    'charset': 'utf8mb4'
}

# Helper functions
def random_string(length, chars=string.ascii_uppercase + string.digits):
    return ''.join(random.choices(chars, k=length))

def random_manufacturer():
    manufacturers = ['Siemens', 'GE', 'Alstom', 'Bombardier', 'Hitachi', 'Toshiba', 'Mitsubishi', 'CAF', 'Talgo', 'Stadler']
    return random.choice(manufacturers)

def random_code():
    return random_string(5, string.ascii_uppercase + string.digits)

def random_date(start_year=2020, end_year=2025):
    start = date(start_year, 1, 1)
    end = date(end_year, 12, 31)
    delta = end - start
    random_days = random.randint(0, delta.days)
    return start + timedelta(days=random_days)

def populate_engines():
    print("Connecting to engines database...")
    conn = pymysql.connect(**DB_CONFIG_ENGINES)
    cursor = conn.cursor()

    # Optional: clear existing data
    cursor.execute("DELETE FROM engines")
    print("Cleared old data from engines table.")

    for i in range(1, 11):
        engine_id = 10000 + i  # business key, unique
        manufacturer = random_manufacturer()
        engine_code = random_string(6, string.ascii_uppercase + string.digits)
        horsepower = round(random.uniform(100, 800), 2)
        weight = round(random.uniform(100, 500), 2)
        price = round(random.uniform(1000, 20000), 2)
        prod_date = random_date()

        sql = """
        INSERT INTO engines (
            engine_id, manufacturer_id, engine_code,
            engine_horsepower, engine_weight, engine_price, production_date
        ) VALUES (%s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (engine_id, manufacturer, engine_code,
                             horsepower, weight, price, prod_date))
        print(f"  Inserted engine {i}: {engine_code}")

    conn.commit()
    cursor.close()
    conn.close()
    print("✅ Engines table populated.\n")

def populate_trains():
    print("Connecting to trains database...")
    conn = pymysql.connect(**DB_CONFIG_TRAINS)
    cursor = conn.cursor()

    # First, fetch the list of engine IDs from the engines database
    # We need to know which engine IDs exist to set the foreign key.
    # We'll read from engines table (we need a separate connection).
    try:
        eng_conn = pymysql.connect(**DB_CONFIG_ENGINES)
        eng_cursor = eng_conn.cursor()
        eng_cursor.execute("SELECT id FROM engines")  # primary key id
        engine_ids = [row[0] for row in eng_cursor.fetchall()]
        eng_cursor.close()
        eng_conn.close()
        if not engine_ids:
            print("⚠️  No engines found. Please run populate_engines() first.")
            return
    except Exception as e:
        print(f"❌ Could not fetch engine IDs: {e}")
        return

    # Clear trains table
    cursor.execute("DELETE FROM trains")
    print("Cleared old data from trains table.")

    for i in range(1, 11):
        code = random_code()
        manufacturer = random_manufacturer()
        engine_id = random.choice(engine_ids)  # pick a random existing engine
        car_amount = random.randint(1, 20)
        cost_per_car = random.randint(1000, 8000)
        prod_date = random_date()

        sql = """
        INSERT INTO trains (
            code, manufacturer_id, engine_id,
            car_amount, costs, production_date
        ) VALUES (%s, %s, %s, %s, %s, %s)
        """
        cursor.execute(sql, (code, manufacturer, engine_id,
                             car_amount, cost_per_car, prod_date))
        print(f"  Inserted train {i}: {code}")

    conn.commit()
    cursor.close()
    conn.close()
    print("✅ Trains table populated.\n")

if __name__ == "__main__":
    # Ensure engine entries exist first (because trains reference them)
    populate_engines()
    populate_trains()
    print("🎉 All dummy data inserted successfully.")