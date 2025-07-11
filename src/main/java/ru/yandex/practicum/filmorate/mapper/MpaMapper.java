package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;
import ru.yandex.practicum.filmorate.model.MpaWithId;

@NoArgsConstructor
public class MpaMapper {

    public static MpaWithId mapToMpaWithId(MpaWithIdAndName mpaWithIdAndName) {
        return new MpaWithId(mpaWithIdAndName.getId());
    }

    public static MpaWithIdAndName mapToMpaWithIdAndName(MpaWithId mpaWithId) {
        return new MpaWithIdAndName(mpaWithId.getId(), null);
    }
}
