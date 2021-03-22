# ghrepos-api

# Introduction
Application API for listing github repositories details using webscraping techniques.

The project uses technology and libs from the Java ecosystem ;)

      - Java 11;
      - SpringBoot 2.5;
      - Maven;
      - JUnit 5 and Mockito;


# Starting
#### 1. Dependêncies (installation and run):
- Java 11,
- Maven, 
- Git
- docker
	
#### 2.	Running Process:

- Maven    
Open the <b>command prompt</b> and enter the commands as below: 

    Download code: 

    <code>
    git clone https://github.com/laercioVieira/ghrepos-api.git
    </code>
    
    You can run by maven: <br/>
    <code>
      .\mvnw spring-boot:run
    </code>

- Docker 
	The image is available at: https://hub.docker.com/r/laerson/ghrepos-api
 	
 	Pull image
 	<code>
 		docker pull laerson/ghrepos-api
 	<code>
 	
 	The project use redis for caching. Use docker compose for running:
 	<code>
 		docker-compose up
 	<code>
 	
 	After that, the application Api will be available at: http://localhost:8080/api

- For testing, use any tool for http request like <b><i>curl</i></b> ou <b><i>postman</i></b>
	
	The API is available too at Amazon AWS at:
	
<br/>
<br/>

## Usage and api resources examples:
---
##### GET REPOSITORY DETAILS GROUP BY FILE EXTENSION - [GET - /api/repos/{user}/{repo}]	
> <code>
> curl -X GET "http://localhost:8080/api/repos/laercioVieira/devops" -H "accept: application/json" -H "Content-Type: application/json"
</code>

##### CLEAR CACHES - [POST - /api/repos/{user}/{repo}/clearcache]

> <code>curl -X POST "http://localhost:8080/api/repos/laercioVieira/devops/clearcache" -H "accept: application/json"
</code>

<br/>
<br/>

# Build e Tests
   For full building:

<i>Build:</i>

	.\mvnw clean package

# Only tests
   For running only tests:
	
	.\mvnw verify 
         
