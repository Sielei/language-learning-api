## \[ 🚧 Work in progress 👷‍♀️⛏👷🔧️🚧 \] Zeraki MidLevel Backend Developer Assessment

## Objectives
* API Development: Develop RESTful services to manage lessons, exercises, and user progress.
* Database Design: Design and implement a database schema suitable for the requirements.
* Business Logic: Implement business logic to track and update user progress and manage lesson workflows.
* Authentication: Implement basic authentication to manage access to user-specific data.
* Testing: Write unit tests and integration tests for the API.


## Prerequisites
[Java 17+](https://openjdk.org/projects/jdk/17/)

[Apache Maven 3.9.4+ ](https://maven.apache.org/download.cgi)

## Running Locally


## Application Overview

The system contains 2 main actors: 
* Tutor
* Learner

The tutor manages supported languages, lessons and exercises. The Learner on the other hand manages their own account and can start a language lesson. 

## Understanding the system entities

![Database Schema](/docs/db_schema.png)

The system has 9 entities namely:
* User - Stores user information.
* PasswordReset - Stores password reset information
* SupportedLanguage - Stores a list of languages that users can learn. A supported language can have 1 or more lessons.
* Lesson - Stores a language lesson. Lessons can have 1 or more exercises.
* Exercises - Stores the learning tasks to be performed by the learner. This system supports 4 types of exercises:
    * Multiple choice - single answer
    * Multiple choice - Multiple answers
    * Short written answers
    * Matching exercises where learners match a pair of words in different languages
* MultipleChoice - Stores multiple choices for exercises with multiple choices.
* ExerciseAnswer - Stores the exercise answer.
* AnswerAttempt - Stores all responses from the learner.
* UserProgress - Stores learners progress

## Using the system

Once the system is up and running, navigate to [Swagger UI](http://localhost:8088/swagger-ui/index.html#/)