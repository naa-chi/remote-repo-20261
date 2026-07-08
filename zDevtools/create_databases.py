import mysql.connector
from mysql.connector import Error


DB_HOST = "localhost"
DB_PORT = 3306
DB_USER = "root"
DB_PASSWORD = ""  

DATABASES = [
    "transport_db_trainsservice",
    "transport_db_enginesservice",
    "transport_db_ticketsservice",
    "transport_db_reviewsservice",
    "transport_db_stationsservice",
    "transport_db_managersservice",
    "transport_db_maintenancesservice",
    "transport_db_linesservice",
    "transport_db_driversservice",
    "transport_db_clientsservice",
    "transport_db_citiesservice"
    # add more as your microservices grow
    # "transport_db_ticketservice",
    # "transport_db_userservice",
]



def create_database(cursor, db_name):
    try:
        cursor.execute(f"CREATE DATABASE IF NOT EXISTS {db_name}")
        print(f"Database '{db_name}' created (or already exists).")
    except Error as e:
        print(f"Failed to create database '{db_name}': {e}")

def main():
    print(f"Host: {DB_HOST}:{DB_PORT}")
    print(f"User: {DB_USER}")
    print(f"Databases: {', '.join(DATABASES)}")
    print()

    try:
        conn = mysql.connector.connect(
            host=DB_HOST,
            port=DB_PORT,
            user=DB_USER,
            password=DB_PASSWORD
        )

        if conn.is_connected():
            cursor = conn.cursor()
            print("Connected to MySQL server.\n")

            for db in DATABASES:
                create_database(cursor, db)

            cursor.close()
            conn.close()
            print("\nAll databases processed successfully.")
        else:
            print("Could not connect to MySQL server.")
            return

    except Error as e:
        print(f"MySQL connection error: {e}")
        return

if __name__ == "__main__":
    main()