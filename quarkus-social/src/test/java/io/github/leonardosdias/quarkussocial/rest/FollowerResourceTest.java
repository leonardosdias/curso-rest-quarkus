package io.github.leonardosdias.quarkussocial.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import io.github.leonardosdias.quarkussocial.domain.repository.*;
import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import io.github.leonardosdias.quarkussocial.domain.model.*;
import io.github.leonardosdias.quarkussocial.rest.dto.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class FollowerResourceTest {

    @jakarta.inject.Inject
    UserRepository userRepository;

    @jakarta.inject.Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    public void setUP() {
        var user = new User();
        user.setAge(30);
        user.setName("Usuário Teste padrão");
        userRepository.persist(user);
        userId = user.getId();

        var follower = new User();
        follower.setAge(30);
        follower.setName("Usuário Teste seguidor");
        userRepository.persist(follower);
        followerId = follower.getId();

        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("deve retornar 409 quando o id o seguidor é igual ao do usuário")
    public void sameUserAsFollowerTest() {
        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());

    }

    @Test
    @DisplayName("deve retornar 404 quando não encontrar o usuário ao tentar segui-lo")
    public void userNotFoundWhenTryingToFollowTest() {
        var body = new FollowerRequest();
        body.setFollowerId(userId);

        var inexistentUserId = 22222;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("deve seguir um usuário")
    public void followUserTest() {
        var body = new FollowerRequest();
        body.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    @Test
    @DisplayName("deve retornar 404 quando não encontrar o usuário para listar")
    public void userNotFoundWhenListingFollowersTest() {
        var inexistentUserId = 22222;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("deve listar os seguidores de um usuário")
    public void listFollowersTest() {
        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");


        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());


    }

    @Test
    @DisplayName("deve retornar 404 quando não encontrar o usuário ao deixar de segui-lo")
    public void userNotFoundWhenUnfollowingTest() {
        var inexistentUserId = 22222;

        given()
                .pathParam("userId", inexistentUserId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("deve deixar de seguir um usuário")
    public void unfollowUserTest() {
        given()
                .pathParam("userId", userId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }
}
