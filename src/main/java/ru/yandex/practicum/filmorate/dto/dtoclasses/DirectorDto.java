package ru.yandex.practicum.filmorate.dto.dtoclasses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class DirectorDto {
    private Long id;
    @NotBlank
    @NotNull
    private String name;
}