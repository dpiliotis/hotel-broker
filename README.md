**Hotel Broker API.**

It's a basic CRUD rest API.

It persists and retrieves data from the Database regarding Hotels and Bookings.

By default it requires MySQL.

In order to test it without MySQL, follow the steps bellow:
1. RUN "mvn clean verify"
2. Then go to the target folder
3. RUN "java -jar -Dspring.profiles.active=test hotel-broker-1.0-SNAPSHOT.jar"
4. RUN "curl localhost:8080/hotels"