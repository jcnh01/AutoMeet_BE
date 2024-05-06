package com.AutoMeet.domain.meetingRoom.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "meetingRoom")
@Getter
public class MeetingRoom {

    @Id
    private String _id;
    private String roomId;
    private String password;
    private List<Long> userIds;

    private LocalDateTime startedTime;

    @Builder
    public MeetingRoom(String roomId, String password, List<Long> userIds, LocalDateTime startedTime) {
        this.roomId = roomId;
        this.password = password;
        this.userIds = userIds;
        this.startedTime = startedTime;
    }
}
