package ru.yandex.practicum.filmorate.model;

import lombok.Data;


@Data
public class MpaWithId {
    Integer id;

    public MpaWithId(Integer id) {
        this.id = id;
    }

    public MpaWithId() {
        super();
    }
}
