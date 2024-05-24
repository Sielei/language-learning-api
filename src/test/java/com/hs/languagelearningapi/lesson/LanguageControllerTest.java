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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LanguageControllerTest {
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
    void shouldAddSupportedLanguage() {
        given().contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Zulu"
                            }
                          """
                )
                .when()
                .header("Authorization", "Bearer " + authToken)
                .post("/api/v1/languages")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex(".*/api/v1/languages/[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$"))
                .body("id", matchesRegex("[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$"))
                .body("name", equalTo("Zulu"));
    }

    @Test
    void findSupportedLanguageById() {
    }

    @Test
    void findAllSupportedLanguages() {
    }

    @Test
    void updateSupportedLanguage() {
    }

    @Test
    void deleteSupportedLanguage() {
    }

    @Test
    void startLearning() {
    }
}