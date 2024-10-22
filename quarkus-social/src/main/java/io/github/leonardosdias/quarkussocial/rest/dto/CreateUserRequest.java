package io.github.leonardosdias.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
 
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Age is Required")
    private Integer age;


}
