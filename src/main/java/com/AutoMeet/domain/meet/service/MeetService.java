package com.AutoMeet.domain.meet.service;

import com.AutoMeet.domain.meet.dto.response.MeetListResponse;

import java.util.List;

public interface MeetService {
    public String textSummarization(String recordingUrl);
    public void save(String summarization, List<String> userNames);
    public List<MeetListResponse> findMeet(Long userId);
}
