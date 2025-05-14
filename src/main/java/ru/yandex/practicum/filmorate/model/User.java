package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    Long id;

    @Email
    String email;

    @NotBlank
    String login;
    String name;

    @Past
    LocalDate birthday;
}
