package ru.yandex.practicum.filmorate.dto.dtoclasses;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DirectorDto {
    private Long id;
    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;


}