package io.github.leonardosdias.quarkussocial.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
// import java.util.List;
// import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
// import jakarta.ws.rs.core.Response;
import io.github.leonardosdias.quarkussocial.rest.dto.CreateUserRequest;
import io.github.leonardosdias.quarkussocial.rest.dto.ResponseError;
// import io.github.leonardosdias.quarkussocial.domain.model.*;
import org.hamcrest.Matchers;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {
    @TestHTTPResource("/users")
    URL apiURL;

    @Test
    @DisplayName("deve criar um usuário com sucesso")
    @Order(1)
    public void createUserTest() {
        var user = new CreateUserRequest();
        user.setName("Teste");
        user.setAge(30);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiURL)
                .then()
                .extract().response();

        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));

    }

    @Test
    @DisplayName("deve dar falhar ao criar o usuário sem requisição")
    @Order(2)
    public void createUserValidationErrorTest() {
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(apiURL)
                .then()
                .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.getStatusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        // List<Map<String, String>> errors = response.jsonPath().getList("errors");
        // assertNotNull( errors.get(0).get("message"));
        // assertNotNull( errors.get(1).get("message"));

        // assertEquals("Age is Required", errors.get(0).get("message"));
        // assertEquals("Name is Required", errors.get(1).get("message"));
    }

    @Test
    @DisplayName("deve listar todos os usuários")
    @Order(3)
    public void listAllUsersTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiURL)
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));
    }

}
