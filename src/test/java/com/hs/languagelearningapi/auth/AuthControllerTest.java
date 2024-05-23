package com.hs.languagelearningapi.auth;


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
class AuthControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15.6"));

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void init(){
        RestAssured.port = port;
    }

    @Test
    void shouldRegisterUserGivenValidUserDetails() {
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "firstName": "Smith",
                                "lastName": "Wesson",
                                "email": "swesson@gmail.com",
                                "password": "p@ssw0Rd"
                            }
                          """
                )
                .post("/api/v1/auth")
                .then()
                .statusCode(201)
                .header("Location", matchesRegex(".*/api/v1/users/[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$"))
                .body("id", matchesRegex("[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$"))
                .body("name", equalTo("Smith Wesson"))
                .body("email", equalTo("swesson@gmail.com"));
    }

    @Test
    void shouldReturnHTTP400IfUserDetailsAreInvalid(){
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "firstName": "",
                                "lastName": "Wesson",
                                "email": "jdoe",
                                "password": "p@ssw0Rd"
                            }
                          """
                )
                .post("/api/v1/auth")
                .then()
                .statusCode(400)
                .body("title", equalTo("Validation Error"))
                .body("errors.firstName.first()", equalTo("First name is required"))
                .body("errors.email.first()", equalTo("Email should be valid"));
    }

    @Test
    void shouldLoginUserAndReturnJwtTokenIfCredentialsAreCorrect() {
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "email": "jbloch@java.com",
                                "password": "p@55w0Rd"
                            }
                          """
                )
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("token", matchesRegex("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"))
                .body("type", equalTo("Bearer"));
    }

    @Test
    void shouldReturnHTTP401IfLoginCredentialsAreWrong(){
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "email": "unknown@gmail.com",
                                "password": "12345"
                            }
                          """
                )
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401)
                .body("title", equalTo("Authentication Error"))
                .body("detail", equalTo("Failed to authenticate user! Username or password is incorrect"));
    }

    @Test
    void shouldUpdateUserPasswordIfCurrentPasswordIsCorrect() {
        var authToken = authenticateRequest();
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "currentPassword": "p@55w0Rd",
                                "newPassword": "12345"
                            }
                          """
                )
                .header("Authorization", "Bearer " + authToken)
                .put("/api/v1/auth/password")
                .then()
                .statusCode(204);
    }

    @Test
    void shouldReturnHTTP401IfUserTriesToUpdatePasswordUsingWrongCurrentPassword(){
        var authToken = authenticateRequest();
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "currentPassword": "wrongCurrentPassword",
                                "newPassword": "12345"
                            }
                          """
                )
                .header("Authorization", "Bearer " + authToken)
                .put("/api/v1/auth/password")
                .then()
                .statusCode(401)
                .body("title", equalTo("Authentication Error"))
                .body("detail", equalTo("Current password does not match with the password you provided!"));
    }

    private String  authenticateRequest() {
        return given().contentType(ContentType.JSON)
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
    void shouldSendResetTokenGivenAValidUserEmail() {
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "email": "admin@llm.com"
                            }
                          """
                )
                .post("/api/v1/auth/get-reset-token")
                .then()
                .statusCode(200)
                .body("success", equalTo("Ok"))
                .body("message", equalTo("A password reset link has been sent to admin@llm.com"));
    }

    @Test
    void shouldReturnHTTP401WhenRequestingForPasswordResetTokenUsingEmailThatIsNotRegistered() {
        given().contentType(ContentType.JSON)
                .body("""
                            {
                                "email": "anonymous@gmail.com"
                            }
                          """
                )
                .post("/api/v1/auth/get-reset-token")
                .then()
                .statusCode(401)
                .body("title", equalTo("Authentication Error"))
                .body("detail", equalTo("Email: anonymous@gmail.com does not exist"));
    }

    @Test
    void shouldResetPasswordGivenValidPasswordResetToken() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                                {
                                    "token": "ba24e2aa-2987-496c-9240-2f3b8fb48f91",
                                    "newPassword": "p@ssw0Rd"
                                }
                            """
                )
                .when()
                .post("/api/v1/auth/reset-password")
                .then()
                .statusCode(200)
                .body("success", equalTo("Ok"))
                .body("message", equalTo("Password reset successfully! Proceed to login page to login with your new password!"));
    }

    @Test
    void shouldReturnHTTP401IfPasswordResetTokenIsNotValid() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                                {
                                    "token": "ba24e2aa-2987-496c-9900-2f3b8fb48f91",
                                    "newPassword": "p@ssw0Rd2"
                                }
                            """
                )
                .when()
                .post("/api/v1/auth/reset-password")
                .then()
                .statusCode(401)
                .body("title", equalTo("Authentication Error"))
                .body("detail", equalTo("The reset token: ba24e2aa-2987-496c-9900-2f3b8fb48f91 is not valid!"));
    }

    @Test
    void shouldReturnHTTP401IfPasswordResetTokenIsExpiredOrUsed() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                                {
                                    "token": "3d1fb1a7-91bb-46c9-842f-5bfe2b53d32f",
                                    "newPassword": "p@ssw0Rd2"
                                }
                            """
                )
                .when()
                .post("/api/v1/auth/reset-password")
                .then()
                .statusCode(401)
                .body("title", equalTo("Authentication Error"))
                .body("detail", equalTo("The token: 3d1fb1a7-91bb-46c9-842f-5bfe2b53d32f is expired or already used!"));
    }

}