package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
public class User {
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

    Set<Long> friends = new HashSet<>();
}
