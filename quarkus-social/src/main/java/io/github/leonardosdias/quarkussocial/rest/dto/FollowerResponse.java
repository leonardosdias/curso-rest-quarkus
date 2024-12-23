package io.github.leonardosdias.quarkussocial.rest.dto;

import lombok.Data;
import io.github.leonardosdias.quarkussocial.domain.model.Follower;

@Data
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse() {

    }

    public FollowerResponse(Follower follower) {
        this(follower.getId(), follower.getFollower().getName());
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
