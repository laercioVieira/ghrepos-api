# ghrepos-api

# Introduction
Application API for listing github repositories details using webscraping techniques.

The project uses technology and libs from the Java ecosystem ;)

- Java 11;
- SpringBoot 2.5;
- Maven;
- JUnit 5 and Mockito;

# Starting
#### 1. Dependêncies (installation and running):
- Java 11,
- Maven, 
- Git
- docker 
- docker-compose (https://docs.docker.com/compose/install/)

#### 2.	Running Process:

- Using Maven:  
Open the <b>command prompt</b> and enter the commands as below:  
  
  Download code:  
  ```
   git clone https://github.com/laercioVieira/ghrepos-api.git 
  ```  
  You can run by maven:  
  ```
   .\mvnw spring-boot:run 
  ```  

   The application Api will be available at: http://localhost:8080/api
   
   Swagger-UI client: http://localhost:8080/api/swagger-ui.html   
      

- Using Docker:  
The image is available at: https://hub.docker.com/r/laerson/ghrepos-api   
   
   Pull image:   
   ```
   docker pull laerson/ghrepos-api 
   ```
 	
 	The project use redis for caching with multiples instances environment. Use docker compose for running:
 	```
    curl https://raw.githubusercontent.com/laercioVieira/ghrepos-api/master/docker-compose-prd.yaml -o ./docker-compose.yaml  

    docker-compose up  

 	```
 	
 	After that, all services needed will starting (redis, backend and nginx loadbalancer). The application Api will be available at: http://localhost/api
   
   Swagger-UI client: \
   http://localhost/api/swagger-ui.html

- For testing, use any tool for http request like <b><i>curl</i></b> ou <b><i>postman</i></b>
	
	The API is available too at Heroku:  
   https://ghrepos-api.herokuapp.com/api/swagger-ui.html
	
<br/>

## Usage and Api resources examples:

#### GET REPOSITORY DETAILS GROUP BY FILE EXTENSION - [GET - /api/repos/{user}/{repo}]	  
   
   
<code>  
 curl -X GET "http://localhost:8080/api/repos/laercioVieira/devops" -H "accept: application/json" -H "Content-Type: application/json"
</code>
   

#### CLEAR CACHES - [DELETE - /api/repos/{user}/{repo}/clearcache]

<code>  
 curl -X DELETE "http://localhost:8080/api/repos/laercioVieira/devops/clearcache" -H "accept: application/json"
</code>

<br/>
<br/>

# Build e Tests
   For full building:

### - Build:   

	.\mvnw clean package

### - Only tests
   For running only tests:
	
	.\mvnw test 
         
