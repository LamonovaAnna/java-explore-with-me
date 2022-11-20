package ru.practicum.explore.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Email
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String name;
}
