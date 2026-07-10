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
DB_CITIES = {**DB_CONFIG, 'database': 'transport_db_citiesservice'}
DB_LINES = {**DB_CONFIG, 'database': 'transport_db_linesservice'}
DB_STATIONS = {**DB_CONFIG, 'database': 'transport_db_stationsservice'}
DB_MANAGERS = {**DB_CONFIG, 'database': 'transport_db_managersservice'}
DB_DRIVERS = {**DB_CONFIG, 'database': 'transport_db_driversservice'}
DB_CLIENTS = {**DB_CONFIG, 'database': 'transport_db_clientsservice'}

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
    """Truncate table safely with backticks."""
    with conn.cursor() as cursor:
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        cursor.execute(f"TRUNCATE TABLE `{table_name}`")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
    conn.commit()
    print(f"  Cleared {table_name}")

# ---------- Clear all tables in dependency order ----------
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

# ---------- Populate functions ----------

def populate_engines(conn):
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

def populate_clients(conn):
    clients = []
    for i in range(1, 21):
        code = f"CL-{i:03d}"
        first_name = random_string(8, string.ascii_uppercase)
        last_name = random_string(10, string.ascii_uppercase)
        email = f"{first_name.lower()}.{last_name.lower()}@example.com"
        phone = f"+1-{random.randint(100,999)}-{random.randint(100,999)}-{random.randint(1000,9999)}"
        reg_date = random_date(2018, 2025)
        clients.append((code, first_name, last_name, email, phone, reg_date))
    with conn.cursor() as cursor:
        for cl in clients:
            cursor.execute("""
                INSERT INTO clients (code, first_name, last_name, email, phone_number, registration_date)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, cl)
    conn.commit()
    print("✅ Clients populated (20)")

def populate_tickets(conn_trains, conn_clients, conn_tickets):
    with conn_trains.cursor() as cursor:
        cursor.execute("SELECT id FROM trains")
        train_ids = [row[0] for row in cursor.fetchall()]
    with conn_clients.cursor() as cursor:
        cursor.execute("SELECT id FROM clients")
        client_ids = [row[0] for row in cursor.fetchall()]
    if not train_ids or not client_ids:
        print("❌ Missing train or client data")
        return
    with conn_tickets.cursor() as cursor:
        for i in range(1, 11):
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
    print("✅ Tickets populated")

def populate_reviews(conn_trains, conn_tickets, conn_reviews):
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

def populate_cities(conn):
    cities = [
        ("LON", "London", "2020-01-01", 9000000, "GB"),
        ("PAR", "Paris", "2020-02-01", 11000000, "FR"),
        ("BER", "Berlin", "2020-03-01", 3600000, "DE"),
        ("MAD", "Madrid", "2020-04-01", 3200000, "ES"),
        ("ROM", "Rome", "2020-05-01", 2800000, "IT"),
        ("AMS", "Amsterdam", "2020-06-01", 1100000, "NL"),
        ("BRU", "Brussels", "2020-07-01", 1200000, "BE"),
        ("VIE", "Vienna", "2020-08-01", 1900000, "AT"),
        ("ZUR", "Zurich", "2020-09-01", 400000, "CH"),
        ("STO", "Stockholm", "2020-10-01", 1600000, "SE")
    ]
    with conn.cursor() as cursor:
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        cursor.execute("SET UNIQUE_CHECKS = 0")
        for city_code, name, founding, pop, country in cities:
            try:
                cursor.execute("""
                    INSERT IGNORE INTO cities (city_code, city_name, founding_date, city_population, country_code)
                    VALUES (%s, %s, %s, %s, %s)
                """, (city_code, name, founding, pop, country))
            except pymysql.Error as e:
                print(f"⚠️ Failed to insert city {city_code}: {e}")
        cursor.execute("SET UNIQUE_CHECKS = 1")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
    conn.commit()
    print("✅ Cities populated (10)")

def populate_lines(conn):
    lines = []
    for i in range(1, 16):
        line_code = i
        length = random.randint(10, 200)
        people = random.randint(10000, 5000000)
        lines.append((line_code, length, people))
    with conn.cursor() as cursor:
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        for code, length, people in lines:
            cursor.execute("""
                INSERT INTO line (line_code, line_length, line_people_served)
                VALUES (%s, %s, %s)
            """, (code, length, people))
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
    conn.commit()
    print("✅ Lines populated (15)")

def populate_stations(conn_cities, conn_lines, conn_stations):
    with conn_cities.cursor() as cursor:
        cursor.execute("SELECT city_code FROM cities")
        city_codes = [row[0] for row in cursor.fetchall()]
    with conn_lines.cursor() as cursor:
        cursor.execute("SELECT line_code FROM line")
        line_codes = [row[0] for row in cursor.fetchall()]
    if not city_codes or not line_codes:
        print("❌ Missing city or line data")
        return
    stations = []
    for i in range(1, 21):
        station_code = f"ST{i:03d}"
        city = random.choice(city_codes)
        chosen_lines = random.sample(line_codes, min(random.randint(1, 4), len(line_codes)))
        l1 = chosen_lines[0] if len(chosen_lines) > 0 else None
        l2 = chosen_lines[1] if len(chosen_lines) > 1 else None
        l3 = chosen_lines[2] if len(chosen_lines) > 2 else None
        l4 = chosen_lines[3] if len(chosen_lines) > 3 else None
        stations.append((station_code, city, l1, l2, l3, l4))
    with conn_stations.cursor() as cursor:
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        for st in stations:
            cursor.execute("""
                INSERT INTO stations (station_code, city_code, line_1, line_2, line_3, line_4)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, st)
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
    conn_stations.commit()
    print("✅ Stations populated (20)")

def populate_managers(conn):
    managers = []
    groups = ['A', 'B', 'C', 'D', 'E', 'F', 'G']
    for i in range(1, 16):
        code = f"MGR-{i:03d}"
        salary = random.randint(40000, 200000)
        contract_date = random_date(2015, 2025)
        first_name = random_string(8, string.ascii_uppercase)
        second_name = random_string(10, string.ascii_uppercase)
        group = random.choice(groups)
        managers.append((code, salary, contract_date, first_name, second_name, group))
    with conn.cursor() as cursor:
        for mgr in managers:
            cursor.execute("""
                INSERT INTO managers (code, salary, contract_date, first_name, second_name, manager_group)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, mgr)
    conn.commit()
    print("✅ Managers populated (15)")

def populate_drivers(conn):
    drivers = []
    capacitated_codes = ['A', 'B', 'C', 'D', 'E']
    for i in range(1, 16):
        code = f"DRV-{i:03d}"
        salary = random.randint(30000, 150000)
        contract_date = random_date(2018, 2025)
        birth_date = random_date(1980, 2005)
        first_name = random_string(8, string.ascii_uppercase)
        second_name = random_string(10, string.ascii_uppercase)
        cap_code = random.choice(capacitated_codes)
        drivers.append((code, salary, contract_date, birth_date, first_name, second_name, cap_code))
    with conn.cursor() as cursor:
        for drv in drivers:
            cursor.execute("""
                INSERT INTO drivers (code, salary, contract_date, birth_date, first_name, second_name, trained_on_type_trains)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
            """, drv)
    conn.commit()
    print("✅ Drivers populated (15)")

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
        clear_all(conns)
        populate_engines(conn_engines)
        populate_trains(conn_engines, conn_trains)
        populate_clients(conn_clients)
        populate_tickets(conn_trains, conn_clients, conn_tickets)
        populate_reviews(conn_trains, conn_tickets, conn_reviews)
        populate_maintenances(conn_trains, conn_engines, conn_maintenances)
        populate_cities(conn_cities)
        populate_lines(conn_lines)
        populate_stations(conn_cities, conn_lines, conn_stations)
        populate_managers(conn_managers)
        populate_drivers(conn_drivers)
        print("🎉 All data inserted successfully.")
    except Exception as e:
        print(f"❌ Error: {e}")
    finally:
        for conn in conns.values():
            conn.close()

if __name__ == "__main__":
    main()