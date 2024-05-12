package com.AutoMeet.domain.meet.service;

import java.util.List;

public interface MeetService {
    public String textSummarization(String recordingUrl);
    public void save(String summarization, List<String> userNames);
}
