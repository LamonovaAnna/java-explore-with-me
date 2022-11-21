package ru.practicum.explore.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    @NotNull
    private Long eventId;

    private String annotation;

    private Long categoryId;

    @Length(max = 7000, min = 20)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private String title;
}