package com.AutoMeet.domain.comment;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comment")
@Getter
public class Comment {

    @Id
    private String _id;
    private Long userId; // 작성자
    private String content;
    private String meetingId; // 회의 id

    private LocalDateTime createdAt;

    @Builder
    public Comment(Long userId, String meetingId, String content, LocalDateTime createdAt) {
        this.userId = userId;
        this.meetingId = meetingId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
