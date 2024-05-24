package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LessonControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15.6"));

    @LocalServerPort
    private Integer port;
    private String authToken;

    @BeforeEach
    void init(){
        RestAssured.port = port;
        authToken = given().contentType(ContentType.JSON)
                .body(
                        """
                                {
                                    "email": "jbloch@java.com",
                                    "password": "p@55w0Rd"
                                }
                            """
                )
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(DTO.LoginResponse.class).token();
    }

    @Test
    void shouldCreateLanguageLesson() {
        given().contentType(ContentType.JSON)
                .body("""
                             {
                                "languageId": "ab6e7aee-f96c-4c4e-a598-f1e5ba792195", 
                                "languageProficiency":  "INTERMEDIATE",
                                "index":  1,
                                "percentagePassMark": 95,
                                "name":  "Ask Someone how they feel",
                                "description": "In this lesson you'll learn how to ask someone how they feel"
                             }
                          """
                )
                .when()
                .header("Authorization", "Bearer " + authToken)
                .post("/api/v1/lessons")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex(".*/api/v1/lessons/[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$"))
                .body("id", matchesRegex("[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$"))
                .body("index", equalTo(1))
                .body("name", equalTo("Ask Someone how they feel"))
                .body("language", equalTo("Swahili"));
    }

    @Test
    void findLanguageLessonById() {
    }

    @Test
    void findAllLanguageLessons() {
    }

    @Test
    void updateLanguageLesson() {
    }

    @Test
    void deleteLanguageLesson() {
    }

    @Test
    void createLessonExercise() {
    }

    @Test
    void findLessonExerciseById() {
    }

    @Test
    void findAllLessonExercises() {
    }

    @Test
    void updateLessonExercise() {
    }

    @Test
    void deleteLessonExercise() {
    }

    @Test
    void getNextExercise() {
    }

    @Test
    void submitAnswer() {
    }
}