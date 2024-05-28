package com.AutoMeet.domain.comment.service;

public interface CommentService {
    public void createComment(String meetingId, String content, Long userId);
    public void updateComment(String meetingId, String commentId, String newContent, Long userId);
    public void deleteComment(String meetingId, String commentId, Long userId);
}
