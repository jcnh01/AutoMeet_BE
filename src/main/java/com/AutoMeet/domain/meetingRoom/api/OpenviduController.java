package com.AutoMeet.domain.meetingRoom.api;

import com.AutoMeet.domain.meetingRoom.dto.request.ConnectMeetingRequest;
import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.meetingRoom.dto.response.CreateMeetingResponse;
import com.AutoMeet.domain.meetingRoom.exception.SessionNotExistException;
import com.AutoMeet.domain.meetingRoom.service.MeetingRoomService;
import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.openvidu.java.client.Connection;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class OpenviduController {

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    private final MeetingRoomService meetingRoomService;

    private Map<String, String> meetingConnection = new HashMap<>();

    // meetingId와 Session을 매칭
    private Map<String, String> sessionRoomConvert = new HashMap<>();

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @PostMapping("")
    public ResponseEntity<CreateMeetingResponse> initializeSession(@RequestBody CreateMeetingRequest createMeetingRequest)
            throws OpenViduJavaClientException, OpenViduHttpException {

        meetingRoomService.createMeeting(createMeetingRequest.getMeetingId(), createMeetingRequest);
        String meetingId = createMeetingRequest.getMeetingId();
        String meetingPw = createMeetingRequest.getPassword();

        // meetingRoom마다 비밀번호를 저장해서 관리
        this.meetingConnection.put(meetingId, meetingPw);

        String StringUUID = UUID.randomUUID().toString();
        String RoomUUID = StringUUID.replaceAll("[^a-zA-Z0-9]", "");

        this.sessionRoomConvert.put(meetingId, RoomUUID);

        Map<String, Object> mettingMap = new HashMap<>();
        mettingMap.put("customSessionId", this.sessionRoomConvert.get(meetingId));

        SessionProperties properties = SessionProperties.fromJson(mettingMap).build();

        Session session = openvidu.createSession(properties);

        return new ResponseEntity<>(new CreateMeetingResponse(meetingId, meetingPw), HttpStatus.OK);
    }


}