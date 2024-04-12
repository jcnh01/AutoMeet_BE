package com.AutoMeet.domain.follow.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "follow")
@Getter
public class Follow {

    @Id
    private String _id;
    private Long userId;
    private List<Long> follows; // 내가 follow 하는 사람

    private LocalDateTime createdAt;

    @Builder
    public Follow(Long userId, List<Long> follows) {
        this.userId = userId;
        this.follows = follows;
        this.createdAt = LocalDateTime.now();
    }
}
