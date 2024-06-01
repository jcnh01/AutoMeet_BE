package com.AutoMeet.domain.meet.model;

import com.AutoMeet.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "meet")
@Getter
public class Meet { // 회의의 결과를 저장

    @Id
    private String _id;
    private String title;
    private String content;
    private List<Analysis> analysisList; // 참여자들의 집중도, 만족도 점수

    private List<Comment> comments;

    private LocalDateTime finishedTime;

    @Builder
    public Meet(String title, String content, LocalDateTime finishedTime) {
        this.title = title;
        this.content = content;
        this.finishedTime = finishedTime;
    }

    public void updateMeeting(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
            comments.add(comment);
        }
        else {
            comments.add(comment);
        }
    }

    public void updateComment(String commentId, String newContent) {
        for (Comment comment : comments) {
            if (comment.getId().equals(commentId)) {
                comment.updateContent(newContent);
                break;
            }
        }
    }

    public void deleteComment(String commentId) {
        comments.removeIf(comment -> comment.getId().equals(commentId));
    }

    public void addAnalysis(Analysis analysis) {
        if (this.analysisList == null) {
            this.analysisList = new ArrayList<>();
            analysisList.add(analysis);
        }
        else {
            analysisList.add(analysis);
        }
    }
}
