# AmbroBook

[![CircleCI](https://circleci.com/gh/FedericoBonel/recipe-mvc/tree/main.svg?style=svg&circle-token=02cc6eefb0d097c7eb8fc60ef8c4f69ea60ba0a7)](https://circleci.com/gh/FedericoBonel/recipe-mvc/tree/main)

## What is this repository?
There are many things I'm passionate about, and two of them are technology and food. Having worked in kitchens a lot in the past, I always enjoy sharing my knowledge with others. So, I decided to build a simple yet robust web application that allows you to share your recipes with anyone!

This is a full-stack website that you can download and run to have your own responsive recipe-sharing site, right out of the box.

<img src="src/main/resources/static/images/AmbroBook.gif" width="700px">

## What do I need to do to run it and start sharing my recipes?
Not too much, you just need maven. Once you have that, all you have to do is download this source code, go to its root 
folder and package it. You can do it through this command:

    mvn clean install

Once you've done this you will have a /target folder. You need to open your command line to that folder and then run 
the following command:

    java -jar recipe-mvc-0.0.1-SNAPSHOT.jar

Once you've done that you'll have the application running on your ip on port 8080.

**_NOTE:_**  This will run the application with an in memory relational database, this is not what you want if you 
were to deploy the app.

To "deploy" the app, what you'll need to do is create a mySQL database with the SQL script contained in 
src/main/java/resources/scripts in a database called "recipes_prod".
Then you'll need to create a database user called "recipe_prod_user" and set whichever password you want for it.Then 
you'll 
need to set that 
password in the src/main/java/resources/application-prod.properties file, set the user permissions to only data 
management operations (no DDL allowed) in the database and finally run the app.

## Is it only available in English?
Nope! This app allows localization, currently it has Spanish and English support but if you wish to add any other 
language, feel free to fork the repo and request a pull request! I'll make sure to bring it in to the main branch.

## Tech stack
The app is built primarily using the Spring framework stack combined with some other technologies:
* Spring Data JPA
* Thymeleaf
* Spring Web MVC
* Hibernate
* Spring Security
* In memory H2 database
* Lombok
* JUnit 5
* Bootstrap 5
* MySQL (MongoDB in the second branch with reactive programming using webflux)
* CircleCI
* Much more...
