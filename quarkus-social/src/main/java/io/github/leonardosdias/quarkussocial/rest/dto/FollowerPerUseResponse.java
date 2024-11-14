package io.github.leonardosdias.quarkussocial.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class FollowerPerUseResponse {

    private Integer followersCount;
    private List<FollowerResponse> content;

}
