package ru.yandex.practicum.filmorate.dto.dtoclasses;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.GenreWithId;

public class GenreWithIdAndName extends GenreWithId {
    @Getter
    @Setter
    String name;

    public GenreWithIdAndName() {
        super();
    }
}
