package com.AutoMeet.domain.meet.service;

import com.AutoMeet.domain.meet.dto.request.UpdateMeetRequest;
import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.dto.response.MeetingResponse;
import com.AutoMeet.domain.meet.dto.response.VideoAnalysisResponse;
import com.AutoMeet.domain.meet.model.Meet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MeetService {
    public String textSummarization(String recordingUrl) throws IOException;
    public Long audioAnalysis(MultipartFile file);
    public VideoAnalysisResponse videoAnalysis(MultipartFile file);
    public void textAnalysis(String meetingId, MultipartFile file, Long userId);
    public void videoAnalysisSave(String meetingId, MultipartFile file, Long userId);
    public String save(String title, String summarization);
    public List<MeetListResponse> findMeets(Long userId);
    public MeetingResponse findOne(String meetingId, Long userId);
    public void updateMeeting(String meetingId, Long userId, UpdateMeetRequest request);
    public Meet findMeeting(String meetingId);
    public void saveMeeting(Meet meeting);
}
