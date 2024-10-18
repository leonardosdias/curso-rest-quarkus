package io.github.leonardosdias.quarkussocial.rest;

import io.github.leonardosdias.quarkussocial.domain.model.User;
import io.github.leonardosdias.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());
        user.persist();

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        User user = User.findById(id);
        if (user != null) {
            user.delete();
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response ipdateUser(@PathParam("id") Long id, CreateUserRequest userData) {
        User user = User.findById(id);
        if (user != null) {
           user.setAge(userData.getAge());
           user.setName(userData.getName());

            return Response.ok(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
