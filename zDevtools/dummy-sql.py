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

# Database names
DB_ENGINES = {**DB_CONFIG, 'database': 'transport_db_enginesservice'}
DB_TRAINS = {**DB_CONFIG, 'database': 'transport_db_trainsservice'}
DB_TICKETS = {**DB_CONFIG, 'database': 'transport_db_ticketsservice'}
DB_REVIEWS = {**DB_CONFIG, 'database': 'transport_db_reviewsservice'}
DB_MAINTENANCES = {**DB_CONFIG, 'database': 'transport_db_maintenancesservice'}
DB_CITIES = {**DB_CONFIG, 'database': 'transport_db_citiesservice'}
DB_LINES = {**DB_CONFIG, 'database': 'transport_db_linesservice'}
DB_STATIONS = {**DB_CONFIG, 'database': 'transport_db_stationsservice'}
DB_MANAGERS = {**DB_CONFIG, 'database': 'transport_db_managersservice'}
DB_DRIVERS = {**DB_CONFIG, 'database': 'transport_db_driversservice'}
DB_CLIENTS = {**DB_CONFIG, 'database': 'transport_db_clientsservice'}

# ========== Utility functions ==========

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

def clear_table(conn, table_name):
    try:
        with conn.cursor() as cursor:
            cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
            cursor.execute(f"TRUNCATE TABLE `{table_name}`")
            cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
        conn.commit()
        print(f"  Cleared {table_name}")
    except pymysql.Error as e:
        print(f"  Could not clear {table_name}: {e}")

def clear_all(conns):
    clear_table(conns['reviews'], 'reviews')
    clear_table(conns['tickets'], 'tickets')
    clear_table(conns['maintenances'], 'maintenance_reports')
    clear_table(conns['stations'], 'stations')
    clear_table(conns['trains'], 'trains')
    clear_table(conns['engines'], 'engines')
    clear_table(conns['cities'], 'cities')
    clear_table(conns['lines'], 'line')
    clear_table(conns['clients'], 'clients')
    clear_table(conns['managers'], 'managers')
    clear_table(conns['drivers'], 'drivers')

# ========== Population functions ==========

def populate_cities(conn, count=30):
    city_data = [
        ("LON", "London", "2020-01-01", 8982000, "GB"),
        ("PAR", "Paris", "2020-02-01", 11060000, "FR"),
        ("BER", "Berlin", "2020-03-01", 3645000, "DE"),
        ("MAD", "Madrid", "2020-04-01", 3223000, "ES"),
        ("ROM", "Rome", "2020-05-01", 2873000, "IT"),
        ("AMS", "Amsterdam", "2020-06-01", 1149000, "NL"),
        ("BRU", "Brussels", "2020-07-01", 1208000, "BE"),
        ("VIE", "Vienna", "2020-08-01", 1911000, "AT"),
        ("ZUR", "Zurich", "2020-09-01", 434000, "CH"),
        ("STO", "Stockholm", "2020-10-01", 1608000, "SE"),
        ("OSL", "Oslo", "2020-11-01", 697000, "NO"),
        ("HEL", "Helsinki", "2020-12-01", 658000, "FI"),
        ("COP", "Copenhagen", "2021-01-01", 1362000, "DK"),
        ("DUB", "Dublin", "2021-02-01", 1400000, "IE"),
        ("LIS", "Lisbon", "2021-03-01", 3018000, "PT"),
        ("ATH", "Athens", "2021-04-01", 3120000, "GR"),
        ("PRA", "Prague", "2021-05-01", 1309000, "CZ"),
        ("BUD", "Budapest", "2021-06-01", 1763000, "HU"),
        ("WAR", "Warsaw", "2021-07-01", 1797000, "PL"),
        ("BUC", "Bucharest", "2021-08-01", 1839000, "RO"),
        ("SOF", "Sofia", "2021-09-01", 1281000, "BG"),
        ("BEL", "Belgrade", "2021-10-01", 1380000, "RS"),
        ("ZAG", "Zagreb", "2021-11-01", 769000, "HR"),
        ("LJU", "Ljubljana", "2021-12-01", 295000, "SI"),
        ("BRA", "Bratislava", "2022-01-01", 432000, "SK"),
        ("TAL", "Tallinn", "2022-02-01", 446000, "EE"),
        ("RIG", "Riga", "2022-03-01", 632000, "LV"),
        ("VIL", "Vilnius", "2022-04-01", 540000, "LT"),
        ("REY", "Reykjavik", "2022-05-01", 131000, "IS"),
        ("BRN", "Bern", "2022-06-01", 134000, "CH")
    ]
    city_data = city_data[:count]
    with conn.cursor() as cursor:
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        cursor.execute("SET UNIQUE_CHECKS = 0")
        for code, name, founding, pop, country in city_data:
            if len(code) != 3:
                print(f"  Skipping city {code} – code must be 3 characters")
                continue
            cursor.execute("SELECT COUNT(*) FROM cities WHERE city_code = %s", (code,))
            if cursor.fetchone()[0] > 0:
                print(f"  City {code} already exists, skipping")
                continue
            try:
                cursor.execute("""
                    INSERT INTO cities (city_code, city_name, founding_date, city_population, population_number, country_code)
                    VALUES (%s, %s, %s, %s, %s, %s)
                """, (code, name, founding, pop, pop, country))
            except pymysql.Error as e:
                print(f"  Failed to insert city {code}: {e}")
        cursor.execute("SET UNIQUE_CHECKS = 1")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
    conn.commit()
    print(f"[OK] Cities populated ({len(city_data)})")

def populate_lines(conn, count=30):
    with conn.cursor() as cursor:
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        for i in range(1, count + 1):
            length = random.randint(10, 200)
            people = random.randint(10000, 5000000)
            try:
                cursor.execute("""
                    INSERT INTO line (line_code, line_length, line_people_served)
                    VALUES (%s, %s, %s)
                """, (i, length, people))
            except pymysql.Error as e:
                print(f"  Failed to insert line {i}: {e}")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
    conn.commit()
    print(f"[OK] Lines populated ({count})")

def populate_engines(conn, count=30):
    with conn.cursor() as cursor:
        for i in range(1, count + 1):
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
    print(f"[OK] Engines populated ({count})")

def populate_trains(conn_engines, conn_trains, count=30):
    with conn_engines.cursor() as cursor:
        cursor.execute("SELECT id FROM engines")
        engine_ids = [row[0] for row in cursor.fetchall()]
    if not engine_ids:
        print("[ERROR] No engines found – cannot populate trains")
        return
    with conn_trains.cursor() as cursor:
        for i in range(1, count + 1):
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
    print(f"[OK] Trains populated ({count})")

def populate_clients(conn, count=30):
    with conn.cursor() as cursor:
        for i in range(1, count + 1):
            code = f"CL-{i:03d}"
            first_name = random_string(8, string.ascii_uppercase)
            last_name = random_string(10, string.ascii_uppercase)
            email = f"{first_name.lower()}.{last_name.lower()}@example.com"
            phone = f"+1-{random.randint(100,999)}-{random.randint(100,999)}-{random.randint(1000,9999)}"
            reg_date = random_date(2018, 2025)
            cursor.execute("""
                INSERT INTO clients (code, first_name, last_name, email, phone_number, registration_date)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (code, first_name, last_name, email, phone, reg_date))
    conn.commit()
    print(f"[OK] Clients populated ({count})")

def populate_tickets(conn_trains, conn_clients, conn_tickets, count=30):
    with conn_trains.cursor() as cursor:
        cursor.execute("SELECT id FROM trains")
        train_ids = [row[0] for row in cursor.fetchall()]
    with conn_clients.cursor() as cursor:
        cursor.execute("SELECT id FROM clients")
        client_ids = [row[0] for row in cursor.fetchall()]
    if not train_ids or not client_ids:
        print("[ERROR] Missing train or client data for tickets")
        return
    with conn_tickets.cursor() as cursor:
        for i in range(1, count + 1):
            code = random_string(8, string.ascii_uppercase + string.digits)
            origin = random_string(3, string.ascii_uppercase)
            dest = random_string(3, string.ascii_uppercase)
            price = round(random.uniform(50, 500), 2)
            client_id = random.choice(client_ids)
            train_id = random.choice(train_ids)
            departure = random_date()
            cursor.execute("""
                INSERT INTO tickets (code, city_code_origin, city_code_destination,
                    price, client_id, train_id, departure_date)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
            """, (code, origin, dest, price, client_id, train_id, departure))
    conn_tickets.commit()
    print(f"[OK] Tickets populated ({count})")

def populate_reviews(conn_trains, conn_tickets, conn_reviews, count=30):
    with conn_trains.cursor() as cursor:
        cursor.execute("SELECT id FROM trains")
        train_ids = [row[0] for row in cursor.fetchall()]
    with conn_tickets.cursor() as cursor:
        cursor.execute("SELECT code FROM tickets")
        ticket_codes = [row[0] for row in cursor.fetchall()]
    if not train_ids or not ticket_codes:
        print("[ERROR] Missing train or ticket data for reviews")
        return
    with conn_reviews.cursor() as cursor:
        for i in range(1, count + 1):
            client_id = random.randint(1, count)
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
    print(f"[OK] Reviews populated ({count})")

def populate_maintenances(conn_trains, conn_engines, conn_maintenances, count=30):
    with conn_trains.cursor() as cursor:
        cursor.execute("SELECT id FROM trains")
        train_ids = [row[0] for row in cursor.fetchall()]
    with conn_engines.cursor() as cursor:
        cursor.execute("SELECT engine_code FROM engines")
        engine_codes = [row[0] for row in cursor.fetchall()]
    if not train_ids or not engine_codes:
        print("[ERROR] Missing train or engine data for maintenance")
        return
    with conn_maintenances.cursor() as cursor:
        for i in range(1, count + 1):
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
    print(f"[OK] Maintenances populated ({count})")

def populate_stations(conn_cities, conn_lines, conn_stations, count=30):
    with conn_cities.cursor() as cursor:
        cursor.execute("SELECT city_code FROM cities")
        city_codes = [row[0] for row in cursor.fetchall()]
    with conn_lines.cursor() as cursor:
        cursor.execute("SELECT line_code FROM line")
        line_codes = [row[0] for row in cursor.fetchall()]
    if not city_codes or not line_codes:
        print("[ERROR] Missing city or line data for stations")
        return
    with conn_stations.cursor() as cursor:
        for i in range(1, count + 1):
            station_code = f"ST{i:03d}"
            city = random.choice(city_codes)
            chosen = random.sample(line_codes, min(random.randint(1, 4), len(line_codes)))
            l1 = chosen[0] if len(chosen) > 0 else None
            l2 = chosen[1] if len(chosen) > 1 else None
            l3 = chosen[2] if len(chosen) > 2 else None
            l4 = chosen[3] if len(chosen) > 3 else None
            # Use backticks for column names with dashes
            cursor.execute("""
                INSERT INTO stations (station_code, city_code, `line-1`, `line-2`, `line-3`, `line-4`)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (station_code, city, l1, l2, l3, l4))
    conn_stations.commit()
    print(f"[OK] Stations populated ({count})")

def populate_managers(conn, count=30):
    groups = ['A', 'B', 'C', 'D', 'E', 'F', 'G']
    with conn.cursor() as cursor:
        for i in range(1, count + 1):
            code = f"MGR-{i:03d}"
            salary = random.randint(40000, 200000)
            contract_date = random_date(2015, 2025)
            first_name = random_string(8, string.ascii_uppercase)
            second_name = random_string(10, string.ascii_uppercase)
            group = random.choice(groups)
            cursor.execute("""
                INSERT INTO managers (code, salary, contract_date, first_name, second_name, manager_group)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (code, salary, contract_date, first_name, second_name, group))
    conn.commit()
    print(f"[OK] Managers populated ({count})")

def populate_drivers(conn, count=30):
    capacitated_codes = ['A', 'B', 'C', 'D', 'E']
    with conn.cursor() as cursor:
        for i in range(1, count + 1):
            code = f"DRV-{i:03d}"
            salary = random.randint(30000, 150000)
            contract_date = random_date(2018, 2025)
            birth_date = random_date(1980, 2005)
            first_name = random_string(8, string.ascii_uppercase)
            second_name = random_string(10, string.ascii_uppercase)
            cap_code = random.choice(capacitated_codes)
            cursor.execute("""
                INSERT INTO drivers (code, salary, contract_date, birth_date, first_name, second_name, trained_on_type_trains)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
            """, (code, salary, contract_date, birth_date, first_name, second_name, cap_code))
    conn.commit()
    print(f"[OK] Drivers populated ({count})")

# ========== Main ==========

def main():
    print("Connecting to databases...")
    conn_engines = pymysql.connect(**DB_ENGINES)
    conn_trains = pymysql.connect(**DB_TRAINS)
    conn_tickets = pymysql.connect(**DB_TICKETS)
    conn_reviews = pymysql.connect(**DB_REVIEWS)
    conn_maintenances = pymysql.connect(**DB_MAINTENANCES)
    conn_cities = pymysql.connect(**DB_CITIES)
    conn_lines = pymysql.connect(**DB_LINES)
    conn_stations = pymysql.connect(**DB_STATIONS)
    conn_managers = pymysql.connect(**DB_MANAGERS)
    conn_drivers = pymysql.connect(**DB_DRIVERS)
    conn_clients = pymysql.connect(**DB_CLIENTS)

    conns = {
        'reviews': conn_reviews,
        'tickets': conn_tickets,
        'maintenances': conn_maintenances,
        'stations': conn_stations,
        'trains': conn_trains,
        'engines': conn_engines,
        'cities': conn_cities,
        'lines': conn_lines,
        'clients': conn_clients,
        'managers': conn_managers,
        'drivers': conn_drivers
    }

    try:
        print("Clearing existing data...")
        clear_all(conns)

        print("Populating tables with 30 records each...")
        populate_engines(conn_engines, 30)
        populate_trains(conn_engines, conn_trains, 30)
        populate_clients(conn_clients, 30)
        populate_tickets(conn_trains, conn_clients, conn_tickets, 30)
        populate_reviews(conn_trains, conn_tickets, conn_reviews, 30)
        populate_maintenances(conn_trains, conn_engines, conn_maintenances, 30)
        populate_cities(conn_cities, 30)
        populate_lines(conn_lines, 30)
        populate_stations(conn_cities, conn_lines, conn_stations, 30)
        populate_managers(conn_managers, 30)
        populate_drivers(conn_drivers, 30)

        print("[SUCCESS] All data inserted successfully.")
    except Exception as e:
        print(f"[ERROR] {e}")
    finally:
        for conn in conns.values():
            conn.close()

if __name__ == "__main__":
    main()