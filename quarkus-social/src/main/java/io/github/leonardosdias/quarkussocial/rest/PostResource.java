package io.github.leonardosdias.quarkussocial.rest;

import io.github.leonardosdias.quarkussocial.domain.model.Post;
import io.github.leonardosdias.quarkussocial.domain.model.User;
import io.github.leonardosdias.quarkussocial.domain.repository.FollowerRepository;
import io.github.leonardosdias.quarkussocial.domain.repository.PostRepository;
import io.github.leonardosdias.quarkussocial.domain.repository.UserRepository;
import io.github.leonardosdias.quarkussocial.rest.dto.CreatePostRequest;
import io.github.leonardosdias.quarkussocial.rest.dto.PostResponse;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository,
            FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;

    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerId);
        boolean follows = followerRepository.follows(follower, user);

        if (followerId == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Você nao enviou o header followerId.")
                    .build();
        }

        if (follower == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Follower inexistente.")
                    .build();
        }

        if (!follows) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Você nao pode ver esse post.")
                    .build();
        }

        var query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        var list = query.list();

        var postResponseList = list.stream()
                .map(post -> PostResponse.fromEntity(post))
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }

}
