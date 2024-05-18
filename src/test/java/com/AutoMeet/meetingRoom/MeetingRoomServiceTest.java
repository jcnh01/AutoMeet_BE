package com.AutoMeet.meetingRoom;

import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import com.AutoMeet.domain.meetingRoom.service.MeetingRoomService;
import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MeetingRoomServiceTest {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @DisplayName("회의방 생성")
    void createMeetingRoom() {
        UserRequestDto userDto = new UserRequestDto("email@naver.com", "password", "kim", 20);
        userService.join(userDto);
        User user = userService.findByEmail(userDto.getEmail());

        CreateMeetingRequest request =
                new CreateMeetingRequest("meetingId", "password", "meetingTitle");

        String meetingId = meetingRoomService.createMeeting(request, user);

        MeetingRoom meetingRoom = meetingRoomService.findMeetingRoom(meetingId);

        assertThat(meetingRoom.getMeetingId()).isEqualTo(request.getMeetingId());
        assertThat(meetingRoom.getMeetingTitle()).isEqualTo(request.getMeetingTitle());
        assertThat(meetingRoom.getPassword()).isEqualTo(request.getPassword());
    }
}