package io.github.leonardosdias.quarkussocial.rest.dto;

import lombok.Data;
import java.time.LocalDateTime;

import io.github.leonardosdias.quarkussocial.domain.model.Post;

@Data
public class PostResponse {
    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post) {
        PostResponse response = new PostResponse();
        
        response.setText(post.getText());
        response.setDateTime(post.getDateTime());

        return response;
    }

}
