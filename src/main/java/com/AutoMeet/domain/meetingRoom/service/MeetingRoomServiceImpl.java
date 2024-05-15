package com.AutoMeet.domain.meetingRoom.service;

import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.meetingRoom.exception.MeetingNotExistException;
import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import com.AutoMeet.domain.meetingRoom.repository.MeetingRoomRepository;
import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingRoomServiceImpl implements MeetingRoomService{

    private final MeetingRoomRepository meetingRoomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createMeeting(String meetingId, final CreateMeetingRequest createMeetingRequest, User user) {

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = ZonedDateTime.of(LocalDateTime.now(), seoulZoneId);

        MeetingRoom meeting = MeetingRoom.builder()
                .meetingId(meetingId)
                .password(createMeetingRequest.getPassword())
                .startedTime(seoulTime.toLocalDateTime())
                .build();

        meeting.getUserIds().add(user.getId());

        meetingRoomRepository.save(meeting);
    }

    @Override
    @Transactional
    public void joinMeetingRoom(String meetingId, Long userId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        meeting.joinUser(userId);
    }

    @Override
    @Transactional
    public void disconnect(String meetingId, User user) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        meeting.deleteUser(user.getId());

        meetingRoomRepository.save(meeting);
    }

    @Override
    public Integer userCnt(String meetingId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        List<Long> userIds = meeting.getUserIds();
        return userIds.size();
    }

    @Override
    @Transactional
    public void deleteMeeting(String meetingId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        meetingRoomRepository.delete(meeting);
    }

    @Override
    public List<String> findUsers(String meetingId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        List<String> userNames = meeting.getUserIds().stream()
                .map(userId -> userRepository.findNameByUserId(userId))
                .collect(Collectors.toList());

        return userNames;
    }

    public MeetingRoom findMeetingRoom(String meetingId) {
        return meetingRoomRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotExistException(meetingId));
    }
}
