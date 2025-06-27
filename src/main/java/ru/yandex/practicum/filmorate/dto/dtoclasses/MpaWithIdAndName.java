package ru.yandex.practicum.filmorate.dto.dtoclasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MpaWithIdAndName {
    Integer id;
    String name;

    public MpaWithIdAndName() {
    }

    public MpaWithIdAndName(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
