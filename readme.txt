Dev'd by naa-chi and bartolomeo123-bot

Reminder of what ports are which 
- Manufacturer = 7770
- Train = 7771
- TypeEngine = 7772
- Station = 7773
- Line = 7774
- Maintenance = 7775
- Ticket = 7776
- City = 7777
- Driver = 7778
- Client = 7779
- Auth = 7780
- Supervisor = 7781
- Review = 7782


Run zMenu.py to start the program
Then, if pymysql is NOT installed, run "pip install mysql" on cmd (Windows+r -> cmd -> execute)
Then, press:
    1- To start zAPIStarter & zCreateAllDatabases
    2- zTerminateMicroservices to end the program neatly 
    3- Just leaves (functionally the same as Ctrl + C)


Docs use swagger and there is logging enabled on every service
We are currently debatably missing hateoas??? I'm not sure.

Startup developed by @naa-chi


naa-chi made the following contributions:
    - typeEngine
    - train
    - maintenance
    - manufacturer
    - ticket
    - review 
    - Added swagger and liquibase on EVERY microservice
    - Fixed issues on exceptions

bartolomeo123 made the following contributions:
    - line
    - city
    - client
    - driver
    - station
    - supervisor
    - Figured out authentication
    - Retrofitted older code for modern exam requierements (Exceptions, update methods)