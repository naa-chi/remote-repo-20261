#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import random
import string
from datetime import date, timedelta
import pymysql

DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': '',
    'charset': 'utf8mb4'
}

DB_ENGINES = {**DB_CONFIG, 'database': 'transport_db_enginesservice'}
DB_TRAINS = {**DB_CONFIG, 'database': 'transport_db_trainsservice'}
DB_TICKETS = {**DB_CONFIG, 'database': 'transport_db_ticketsservice'}
DB_REVIEWS = {**DB_CONFIG, 'database': 'transport_db_reviewsservice'}
DB_MAINTENANCES = {**DB_CONFIG, 'database': 'transport_db_maintenancesservice'}

def random_string(length, chars=string.ascii_uppercase + string.digits):
    return ''.join(random.choices(chars, k=length))

def random_manufacturer():
    mfgs = ['Siemens', 'GE', 'Alstom', 'Bombardier', 'Hitachi', 'Toshiba', 'Mitsubishi', 'CAF', 'Talgo', 'Stadler']
    return random.choice(mfgs)

def random_date(start_year=2020, end_year=2025):
    start = date(start_year, 1, 1)
    end = date(end_year, 12, 31)
    delta = end - start
    random_days = random.randint(0, delta.days)
    return start + timedelta(days=random_days)

def truncate_table(conn, table_name):
    with conn.cursor() as cursor:
        cursor.execute(f"TRUNCATE TABLE {table_name}")
    conn.commit()
    print(f"  Truncated {table_name}")

def populate_engines(conn):
    truncate_table(conn, 'engines')
    with conn.cursor() as cursor:
        for i in range(1, 11):
            engine_id = 10000 + i
            manufacturer = random_manufacturer()
            engine_code = random_string(6)
            horsepower = round(random.uniform(100, 800), 2)
            weight = round(random.uniform(100, 500), 2)
            price = round(random.uniform(1000, 20000), 2)
            prod_date = random_date()
            cursor.execute("""
                INSERT INTO engines (engine_id, manufacturer_id, engine_code,
                    engine_horsepower, engine_weight, engine_price, production_date)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
            """, (engine_id, manufacturer, engine_code, horsepower, weight, price, prod_date))
        conn.commit()
    print("✅ Engines populated")

def populate_trains(conn_engines, conn_trains):
    truncate_table(conn_trains, 'trains')
    with conn_engines.cursor() as cursor:
        cursor.execute("SELECT id FROM engines")
        engine_ids = [row[0] for row in cursor.fetchall()]
    if not engine_ids:
        print("❌ No engines found – run engines first")
        return
    with conn_trains.cursor() as cursor:
        for i in range(1, 11):
            code = random_string(5)
            manufacturer = random_manufacturer()
            engine_id = random.choice(engine_ids)
            car_amount = random.randint(1, 20)
            cost_per_car = random.randint(1000, 8000)
            prod_date = random_date()
            cursor.execute("""
                INSERT INTO trains (code, manufacturer_id, engine_id,
                    car_amount, costs, production_date)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (code, manufacturer, engine_id, car_amount, cost_per_car, prod_date))
        conn_trains.commit()
    print("✅ Trains populated")

def populate_tickets(conn_trains, conn_tickets):
    truncate_table(conn_tickets, 'tickets')
    with conn_trains.cursor() as cursor:
        cursor.execute("SELECT id FROM trains")
        train_ids = [row[0] for row in cursor.fetchall()]
    if not train_ids:
        print("❌ No trains found – run trains first")
        return
    with conn_tickets.cursor() as cursor:
        for i in range(1, 11):
            code = random_string(8, string.ascii_uppercase + string.digits)
            origin = random_string(3, string.ascii_uppercase)
            dest = random_string(3, string.ascii_uppercase)
            price = round(random.uniform(50, 500), 2)
            client_id = random.randint(1, 20)
            train_id = random.choice(train_ids)
            departure = random_date()
            cursor.execute("""
                INSERT INTO tickets (code, city_code_origin, city_code_destination,
                    price, client_id, train_id, departure_date)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
            """, (code, origin, dest, price, client_id, train_id, departure))
        conn_tickets.commit()
    print("✅ Tickets populated")

def populate_reviews(conn_trains, conn_tickets, conn_reviews):
    truncate_table(conn_reviews, 'reviews')
    with conn_trains.cursor() as cursor:
        cursor.execute("SELECT id FROM trains")
        train_ids = [row[0] for row in cursor.fetchall()]
    with conn_tickets.cursor() as cursor:
        cursor.execute("SELECT code FROM tickets")
        ticket_codes = [row[0] for row in cursor.fetchall()]
    if not train_ids or not ticket_codes:
        print("❌ Missing train or ticket data")
        return
    with conn_reviews.cursor() as cursor:
        for i in range(1, 11):
            client_id = random.randint(1, 20)
            train_id = random.choice(train_ids)
            ticket_code = random.choice(ticket_codes)
            rating = random.randint(1, 5)
            comment = f"Review #{i} – " + random_string(20, string.ascii_letters + ' ')
            review_date = random_date()
            cursor.execute("""
                INSERT INTO reviews (client_id, train_id, ticket_code,
                    rating, comment, review_date)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (client_id, train_id, ticket_code, rating, comment, review_date))
        conn_reviews.commit()
    print("✅ Reviews populated")

def populate_maintenances(conn_trains, conn_engines, conn_maintenances):
    truncate_table(conn_maintenances, 'maintenance_reports')
    with conn_trains.cursor() as cursor:
        cursor.execute("SELECT id FROM trains")
        train_ids = [row[0] for row in cursor.fetchall()]
    with conn_engines.cursor() as cursor:
        cursor.execute("SELECT engine_code FROM engines")
        engine_codes = [row[0] for row in cursor.fetchall()]
    if not train_ids or not engine_codes:
        print("❌ Missing train or engine data for maintenance")
        return
    with conn_maintenances.cursor() as cursor:
        for i in range(1, 11):
            maintenance_id = f"MNT-{i:03d}"
            description = f"Maintenance #{i} – " + random_string(15, string.ascii_letters + ' ')
            entry_date = random_date()
            end_date = entry_date + timedelta(days=random.randint(1, 14))
            release_date = end_date + timedelta(days=random.randint(1, 5))
            crew = f"CREW-{random.choice(string.ascii_uppercase)}"
            price = random.randint(1000, 15000)
            engine_code = random.choice(engine_codes)
            train_id = random.choice(train_ids)
            cursor.execute("""
                INSERT INTO maintenance_reports (
                    maintenance_description, maintenance_entry_date,
                    maintenance_end_date, maintenance_release_date,
                    maintenance_crew, maintenance_price,
                    engine_code, train_id, maintenance_id
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
            """, (description, entry_date, end_date, release_date,
                  crew, price, engine_code, train_id, maintenance_id))
        conn_maintenances.commit()
    print("✅ Maintenances populated")

def main():
    print("Connecting to databases...")
    conn_engines = pymysql.connect(**DB_ENGINES)
    conn_trains = pymysql.connect(**DB_TRAINS)
    conn_tickets = pymysql.connect(**DB_TICKETS)
    conn_reviews = pymysql.connect(**DB_REVIEWS)
    conn_maintenances = pymysql.connect(**DB_MAINTENANCES)

    try:
        populate_engines(conn_engines)
        populate_trains(conn_engines, conn_trains)
        populate_tickets(conn_trains, conn_tickets)
        populate_reviews(conn_trains, conn_tickets, conn_reviews)
        populate_maintenances(conn_trains, conn_engines, conn_maintenances)
        print("🎉 All data inserted successfully.")
    except Exception as e:
        print(f"❌ Error: {e}")
    finally:
        conn_engines.close()
        conn_trains.close()
        conn_tickets.close()
        conn_reviews.close()
        conn_maintenances.close()

if __name__ == "__main__":
    main()