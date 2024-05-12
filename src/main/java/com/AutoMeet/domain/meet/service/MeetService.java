package com.AutoMeet.domain.meet.service;

import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.dto.response.MeetingResponse;
import com.AutoMeet.domain.meet.model.Meet;

import java.util.List;

public interface MeetService {
    public String textSummarization(String recordingUrl);
    public void save(String summarization, List<String> userNames);
    public List<MeetListResponse> findMeets(Long userId);
    public MeetingResponse findOne(String meetingId, Long userId);
}
