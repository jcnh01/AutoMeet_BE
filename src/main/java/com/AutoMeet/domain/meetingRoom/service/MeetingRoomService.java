package com.AutoMeet.domain.meetingRoom.service;

import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.user.model.User;

import java.util.List;

public interface MeetingRoomService {
    public void createMeeting(String meetingId, final CreateMeetingRequest createMeetingRequest, User user);
    public void joinMeetingRoom(String meetingId, Long userId);
    public void disconnect(String meetingId, User user);
    public Integer userCnt(String meetingId);
    public void deleteMeeting(String meetingId);
    public List<Long> findUsers(String meetingId);
}
