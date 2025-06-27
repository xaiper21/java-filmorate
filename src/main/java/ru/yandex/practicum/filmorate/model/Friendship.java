package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private long senderId;
    private long receiverId;
    private boolean senderApproved;
    private boolean receiverApproved;
}
