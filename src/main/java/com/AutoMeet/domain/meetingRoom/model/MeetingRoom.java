package com.AutoMeet.domain.meetingRoom.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "meetingRoom")
@Getter
public class MeetingRoom {

    @Id
    private String _id;
    private String roomId;
    private String password;

    private LocalDateTime startedTime;

    @Builder
    public MeetingRoom(String roomId, String password, LocalDateTime startedTime) {
        this.roomId = roomId;
        this.password = password;
        this.startedTime = startedTime;
    }
}
