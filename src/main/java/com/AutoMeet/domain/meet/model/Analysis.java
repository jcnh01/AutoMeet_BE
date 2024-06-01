package com.AutoMeet.domain.meet.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class Analysis {

    @Id
    private String id;
    private Long userId;
    private Long sentimentScore;
    private Long concentrationScore;

    @Builder
    public Analysis(Long userId, Long sentimentScore) {
        this.userId = userId;
        this.sentimentScore = sentimentScore;
        this.concentrationScore = null;
    }

    public void changeAnalysis(Long score, Long concentration) {
        this.sentimentScore = (sentimentScore + score) / 2;
        this.concentrationScore = concentration;
    }
}
