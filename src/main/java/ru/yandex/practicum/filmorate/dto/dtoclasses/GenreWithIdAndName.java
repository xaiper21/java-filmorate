package ru.yandex.practicum.filmorate.dto.dtoclasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreWithIdAndName {
    private Integer id;
    private String name;

    public GenreWithIdAndName() {
    }

    public GenreWithIdAndName(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
