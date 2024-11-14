package io.github.leonardosdias.quarkussocial.rest;

import java.util.List;
import java.util.stream.Collectors;

import io.github.leonardosdias.quarkussocial.domain.model.Follower;
import io.github.leonardosdias.quarkussocial.domain.model.User;
import io.github.leonardosdias.quarkussocial.domain.repository.FollowerRepository;
import io.github.leonardosdias.quarkussocial.domain.repository.UserRepository;
import io.github.leonardosdias.quarkussocial.rest.dto.FollowerPerUseResponse;
import io.github.leonardosdias.quarkussocial.rest.dto.FollowerRequest;
import io.github.leonardosdias.quarkussocial.rest.dto.FollowerResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PUT;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private UserRepository userRepository;
    private FollowerRepository followerRepository;

    @Inject
    public FollowerResource(UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request) {
        User user = userRepository.findById(userId);

        if (userId.equals(request.getFollowerId())) {
            return Response.status(Response.Status.CONFLICT).entity("Você não pode seguir a si mesmo").build();
        }

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(request.getFollowerId());
        boolean follows = followerRepository.follows(follower, user);

        if (!follows) {
            Follower entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            followerRepository.persist(entity);

        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Follower> list = followerRepository.findByUser(userId);
        FollowerPerUseResponse responseObject = new FollowerPerUseResponse();
        responseObject.setFollowersCount(list.size());

        List<FollowerResponse> followerList = list.stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());

        responseObject.setContent(followerList);
        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Response.Status.NO_CONTENT).build();

    }
}
