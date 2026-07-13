### ReadMe.md
Hecho por: 
- Naa-chi,
- Bartolomeo123bot

### Prerequisitos
Se necesita apache maven, y Java 21. También se necesita MySQL, en este caso MySQL Workbench CE.
Se necesita un usuario en el puerto 3306, con nombre root y sin contraseña.

Técnicamente, se necesitaba Docker, pero decidí usar Python, por que se como se usa y lo encuentro más fácil. 

Se debe correr:
    1) create_databases.py
    2) project_starter.py
    3) dummy-sql.py
    4) microservice_health_check.py

### Arquitectura
Opera en una arquitectura de microservicios, osea no es monolitica.

Se puede explicar como "modular", ya que se puede implementar un servicio nuevo sin tener que hacer un millón de cambios.

### Documentación

Una vez instalado y ejecutado, ve a
    http://localhost:8080/swagger-ui/index.html?urls.primaryName=Drivers+Service
Para entrar a la documentación de Swagger! Esta en formato dropdown, asi que funcionará todo acá.

### Tech 

Java 21+ / Spring boot 4.1.0
Discovery: Eureka
Gateway: Spring Cloud Gateway
Comms: OpenFeign
Resilience: Resilience4j 
Database: MySQL
Documentación: Swagger