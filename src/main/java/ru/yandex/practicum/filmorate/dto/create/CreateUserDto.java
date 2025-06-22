package ru.yandex.practicum.filmorate.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDto {
    Long id;
    @NotNull
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    @NotNull
    @Past
    LocalDate birthday;
}
