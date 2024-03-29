# README #

Spring Boot demo accompanying source code for blog post at [http://tech.asimio.net/2017/01/17/Multitenant-applications-using-Spring-Boot-JPA-Hibernate-and-Postgres.html](http://tech.asimio.net/2017/01/17/Multitenant-applications-using-Spring-Boot-JPA-Hibernate-and-Postgres.html)

### Requirements ###

* Java 8
* Maven 3.2.x
* Postgres server to create a couple of databases or Docker host to start a couple of [asimio/db_dvdrental](https://hub.docker.com/r/asimio/db_dvdrental/) Postgres containers.

```
docker run -d -p 5432:5432 -e DB_NAME=db_dvdrental -e DB_USER=user_dvdrental -e DB_PASSWD=changeit asimio/db_dvdrental:latest
docker run -d -p 5532:5432 -e DB_NAME=db_dvdrental -e DB_USER=user_dvdrental -e DB_PASSWD=changeit asimio/db_dvdrental:latest
```

and update `application.yml` appropriately.

### Building the artifact ###

```
mvn clean package
```

### Running the application from command line ###

```
mvn spring-boot:run
```

### Available URLs

```
curl -v -H "X-TENANT-ID: tenant_1" "http://localhost:8800/demo/1"
curl -v -H "X-TENANT-ID: tenant_2" "http://localhost:8800/demo/1"
curl -v -H "X-TENANT-ID: tenant_1" -H "Content-Type: application/json" "http://localhost:8800/demo/1" -X PATCH --data '{"firstName": "Orlando"}'
```
should result in successful responses.

### Who do I talk to? ###

* ootero at asimio dot net
* https://www.linkedin.com/in/ootero