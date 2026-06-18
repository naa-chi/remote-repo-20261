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


OpenAllMicroservices.py automatically opens every microservice so it's no longer needed to be done manually ¿
Doesn't need to open individual terminals either. It does take a while, though.

Run pip install pymysql to run it properly
Then run python OpenAllMicroservices.py on the root folder of the microservices


Docs use swagger and there is logging enabled on every service
We are currently debatably missing hateoas??? I'm not sure.
This readme isn't really packed with information as much as it is one dev's ramblings.

Help.

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