package ru.practicum.explore.model.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    @NotNull
    @NotBlank
    private Long id;

    @NotNull
    @NotBlank
    private String name;
}
