# Uvod u kontejnere (Docker)

Video materijali uz ovaj repozitorij se nalaze [ovdje](https://www.youtube.com/playlist?list=PLy0T81VDh93awNP1X91-WPNgOykME_2CI).

## Osnovne naredbe za korištenje Dockera

```sh
docker images
curl https://registry.hub.docker.com/v1/repositories/<repozitorij slika>/tags | json_pp | grep name
docker pull imeslike
```

## Demo - uvod u Docker

```sh
docker run openjdk:8u212-jre-alpine3.9 java -version
docker ps
docker ps -a
docker rmi openjdk:8u212-jre-alpine3.9
docker run openjdk:8u212-jre-alpine3.9 java -version
docker rm openjdk:8u212-jre-alpine3.9
docker pull openjdk:8u212-jre-alpine3.9
docker logs a2eef3c166c3
docker inspect -f "{{ .Config.Env }}" a2eef3c166c3
docker run openjdk:11.0.5-jdk-slim jshell
docker run -it openjdk:11.0.5-jdk-slim jshell
```

- u drugom terminalu

```sh
docker exec cds5rew java -version
docker attach cds5rew
```

## SpringBoot aplikacija - students

```sh
git checkout 0.0.1-SNAPSHOT
gradle clean build
java -jar build/libs/students-0.0.1-SNAPSHOT.jar
curl localhost:8080/health
curl localhost:8080/stats
```

## Ručna izrada slike

```sh
docker run openjdk:11.0.5-jre-slim java -version
docker ps -a
docker container cp build/libs/students-0.0.1-SNAPSHOT.jar 561b6be4802d:/opt/app.jar
docker commit -c 'CMD ["java", "-jar", "/opt/app.jar"]' -c 'EXPOSE 8080' 561b6be4802d students:0.0.1-SNAPSHOT
docker images | grep student
docker run -p 8080:8080 students:0.0.1-SNAPSHOT
curl localhost:8080/health
curl localhost:8080/stats

docker stats
docker run -p 8080:8080 -m 150m students:0.0.1-SNAPSHOT
docker run -p 8080:8080 -m 150m --cpu-quota 50000 students:0.0.1-SNAPSHOT
```

## Izgradnja slike pomoću Dockerfilea

```sh
git checkout dockerfile
gradle clean build

docker build -t students-df:0.0.1-SNAPSHOT .
docker run -p 8080:8080 students-df:0.0.1-SNAPSHOT
```

## Izgradnja slike pomoću plugina za Gradle

```sh
git checkout gradle-dockerimage
gradle clean build

docker run -p 8080:8080 students-gradle:0.0.1-SNAPSHOT
```

## Izgradnja slike pomoću plugina Google JIB

```sh
git checkout jib
gradle clean build

docker run -p 8080:8080 students-jib:0.0.1-SNAPSHOT
```

## Povezivanje više kontejnera

### H2

```sh
git checkout h2
gradle clean build
```

- pokrenuti aplikaciju i pokazati korištenje
  - otvorimo http://localhost:8080/swagger-ui.html
  - pokažemo korisnike
  - dodamo korisnika
  - pokažemo korisnike
- ugasimo aplikaciju
- ponovno pokrenemo aplikaciju
  - pokažemo za više nema novih korisnika

### Postgres

```sh
git checkout postgres
gradle clean build
```

- pokrenemo lokalno postgres
- pokrenemo pgadmin
  - pokažemo baze podataka -> nema students

- pokrenemo aplikaciju i vidimo da se ne može pokrenuti jer nema baze podataka students

```sh
java -jar build/libs/0.0.1-SNAPSHOT
```

- preko pgadmina napravimo bazu podataka `students`
- ponovno pokrenemo aplikaciju

```sh
java -jar build/libs/0.0.1-SNAPSHOT
```

- otvorimo http://localhost:8080/swagger-ui.html
- pokažemo korisnike
- dodamo korisnika
- pokažemo korisnike
- ugasimo aplikaciju
- ponovno pokrenemo aplikaciju
  - pokažemo da sada imamo korisnike koje smo dodali

### Baza u Dockeru, a aplikacija lokalno

```sh
git checkout postgres-docker

docker run --name postgres -e POSTGRES_USER=studentapp -e POSTGRES_PASSWORD=jakosiguranpass -e POSTGRES_DB=students -p 5432:5432 -d postgres:12.1-alpine
```

- otvorimo pgadmin i pokažemo da nema tablica

- `application.properties`:

```props
spring.datasource.url= jdbc:postgresql://localhost:5432/students
spring.datasource.username=studentapp
spring.datasource.password=jakosiguranpass
```

```sh
gradle clean build
```

- pokrenemo aplikaciju

```sh
java -jar build/libs/0.0.1-SNAPSHOT
```

- otvorimo http://localhost:8080/swagger-ui.html
- pokažemo korisnike
- u pgadminu pokažemo da sada imamo tablice

- dodamo korisnika
- pokažemo korisnike
- u pgadminu pokažemo korisnike

- ugasimo aplikaciju
- ponovno pokrenemo aplikaciju
  - pokažemo da sada imamo korisnike koje smo dodali

### Pokretanje baze i aplikacije u dockeru

```sh
git checkout linking
gradle clean build
```

- `application.properties`:

```porps
spring.datasource.url= jdbc:postgresql://${DB_HOSTNAME:localhost}:${DB_PORT:5432}/${DB_NAME:students}
spring.datasource.username=${DB_USERNAME:studentapp}
spring.datasource.password=${DB_PASSWORD:jakosiguranpass}
```

```sh
docker run -p 8080:8080 -e DB_HOSTNAME=postgres students-jib:0.2.0-SNAPSHOT
```

- pokažemo da se aplikacija neće dići jer nema linka

- ponovno pokremeno aplikaciju s opcijom link:

```sh
docker run -d --name students -p 8080:8080 -e DB_HOSTNAME=postgres --link=postgres students-jib:0.2.0-SNAPSHOT
docker logs students -f
```

- otvorimo http://localhost:8080/swagger-ui.html
- pokažemo korisnike
- dodamo novog korisnika
- pokažemo korisnike
- pokažemo u pgadminu da imamo tablice i nove korisnike

- ugasimo postgres `docker stop postgres`
- obrišemo postgres kontejner `docker rm postgres`
- pokrenemo postgres

```sh
docker run --name postgres -e POSTGRES_USER=studentapp -e POSTGRES_PASSWORD=jakosiguranpass -e POSTGRES_DB=students -p 5432:5432 -d postgres:12.1-alpine
```

- u pgadminu pokažemo da nema više tablica
- u swaggeru pokažemo da ne radi aplikacija
- restartamo aplikaciju

```sh
docker stop students
docker container prune
docker run -d --name students -p 8080:8080 -e DB_HOSTNAME=postgres --link=postgres students-jib:0.2.0-SNAPSHOT
```

- ponovno pokažemo u pgadminu tablice, ali su bez novih korisnika

- sve zaustavimo

```sh
docker stop postgres students
docker container prune
```

### Spremanje podataka između pokretanja (*volumes*)

```sh
docker run --name postgres -e POSTGRES_USER=studentapp -e POSTGRES_PASSWORD=jakosiguranpass -e POSTGRES_DB=students -p 5432:5432 -d --volume postgres-db-volume:/var/lib/postgresql/data postgres:12.1-alpine

docker run -d --name students -p 8080:8080 -e DB_HOSTNAME=postgres --link=postgres students-jib:0.2.0-SNAPSHOT
```

- pokažemo da sve radi
- dodamo korisnika
- ugasimo bazu `docker stop postgres`
- obrišemo kontejner `docker rm postgres`
- ponovno pokrenemo bazu

```sh
docker run --name postgres -e POSTGRES_USER=studentapp -e POSTGRES_PASSWORD=jakosiguranpass -e POSTGRES_DB=students -p 5432:5432 -d --volume postgres-db-volume:/var/lib/postgresql/data postgres:12.1-alpine
```

- pokažemo da sve radi

```sh
docker volume ls
```

- sve pogasimo `docker stop postgres students`
- obrišemo volume `docker volume rm postgres-db-volume`
- obrišemo kontejnere `docker container prune`

## Docker-compose

```sh
git checkout compose
gradle clean build
```

- pokažemo `docker-compose.yml`

### Pokretanje i zaustavljanje

```sh
docker-compose up
```

- prbamo se spojiti s pgadminom
- pokažemo da baza nema preslikana vrata (port)
- odkomentiramo u `docker-compose.yml` portove:

```yml
...
  postgres:
...
    ports:
      - "5432:5432"
...
```

- ponovno pokrenemo

- pokažemo da sve radi
- ugasimo

```sh
docker-compose up -d
docker-compose ps
docker ps
docker-compose logs -f
docker-compose stop
docker ps -a
docker-compose rm
docker ps -a
```
