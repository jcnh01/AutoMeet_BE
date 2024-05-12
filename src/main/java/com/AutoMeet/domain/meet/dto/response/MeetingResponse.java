package com.AutoMeet.domain.meet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingResponse {

    private String meetingId;
    private String title;
    private String content;
    private List<String> userNames;
    private LocalDateTime meetingTime;
}

