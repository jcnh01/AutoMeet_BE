package com.AutoMeet.domain.meet.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "meet")
@Getter
public class Meet { // 회의의 결과를 저장

    @Id
    private String _id;
    private String title;
    private String content;
    private List<String> userNames; // 참여자들을 리스트로 저장

    private LocalDateTime finishedTime;

    @Builder
    public Meet(String title, String content, List<String> userNames, LocalDateTime finishedTime) {
        this.title = title;
        this.content = content;
        this.userNames = userNames;
        this.finishedTime = finishedTime;
    }

    public void updateMeeting(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
