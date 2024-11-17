package io.github.leonardosdias.quarkussocial.rest;

import static io.restassured.RestAssured.given;
 
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

    @jakarta.inject.Inject
    FollowerRepository followerRepository;

    @jakarta.inject.Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUP() {
        var user = new User();
        user.setAge(30);
        user.setName("Usuário Teste padrão");
        userRepository.persist(user);
        userId = user.getId();

        var userNotFollower = new User();
        userNotFollower.setAge(40);
        userNotFollower.setName("Usuário Teste não seguidor");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        var userFollower = new User();
        userFollower.setAge(40);
        userFollower.setName("Usuário Teste seguidor");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        var follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);

        var post = new Post();
        post.setUser(user);
        post.setText("Teste");
        postRepository.persist(post);
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

    @Test
    @DisplayName("deve retornar 404 quando o usuário não existir")
    public void listPostUserNotFoundTest() {
        var inexistentUser = 22222;
        given()
                .pathParam("userId", inexistentUser)
                .when()
                .get()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("deve retornar 404 quando não enviar o id do seguidor pelo header pela requisição")
    public void listPostFollowerHeaderNotSendTest() {
        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("deve retornar 404 quando o seguidor não existir")
    public void listPostFollowerNotFoundTest() {

        var inexistentFollower = 22222;
        given()
                .pathParam("userId", userId)
                .header("followerId", inexistentFollower)
                .when()
                .get()
                .then()
                .statusCode(400);

    }

    @Test
    @DisplayName("deve retornar 403 quando o usuário não for um seguidor")
    public void listPostNotAFollowerTest() {

        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403);

    }

    @Test
    @DisplayName("deve retornar os posts de um usuário")
    public void listPostsTest() {
        given()
        .pathParam("userId", userId)
        .header("followerId", userFollowerId)
        .when()
        .get()
        .then()
        .statusCode(200);
    }
}
