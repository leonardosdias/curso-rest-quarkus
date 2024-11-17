package io.github.leonardosdias.quarkussocial.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import io.github.leonardosdias.quarkussocial.domain.repository.*;
import io.github.leonardosdias.quarkussocial.domain.model.*;
import io.github.leonardosdias.quarkussocial.rest.dto.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
public class PostResourceTest {
    @jakarta.inject.Inject
    UserRepository userRepository;
    Long userId;

    @BeforeEach
    @Transactional
    public void setUP() {
        var user = new User();
        user.setAge(30);
        user.setName("Usuário Teste");
        userRepository.persist(user);
        userId = user.getId();

    }

    @Test
    @DisplayName("deve criar um post para um usuário")
    public void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Teste");

        var userID = 1;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userID)
                .when()
                .post()
                .then()
                .statusCode(201);

    }

    @Test
    @DisplayName("deve retornar 404 quando tentar fazer um post com usuário inexistente")
    public void postForAnInexistentUserTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Teste");

        var inexistentUser = 1222222;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistentUser)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

}
