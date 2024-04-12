package com.AutoMeet.domain.meeting.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "meet")
@Getter
public class Meet {

    @Id
    private String _id;
    private String title;
    private String content;
    private List<Long> userIds; // 참여자들을 리스트로 저장

    private LocalDateTime createdAt;

    @Builder
    public Meet(String title, String content, List<Long> userIds) {
        this.title = title;
        this.content = content;
        this.userIds = userIds;
        this.createdAt = LocalDateTime.now();
    }
}
