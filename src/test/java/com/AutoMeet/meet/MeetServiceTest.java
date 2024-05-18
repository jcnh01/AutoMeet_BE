package com.AutoMeet.meet;

import com.AutoMeet.domain.meet.exception.NotYourMeetingException;
import com.AutoMeet.domain.meet.model.Meet;
import com.AutoMeet.domain.meet.service.MeetService;
import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MeetServiceTest {

    @Autowired
    private MeetService meetService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @DisplayName("회의 보고서 생성 및 조회")
    void saveAndFineMeet() {
        UserRequestDto userDto = new UserRequestDto("email@naver.com", "password", "kim", 20);
        userService.join(userDto);
        User user = userService.findByEmail(userDto.getEmail());

        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());

        UserRequestDto userDto2 = new UserRequestDto("email2@naver.com", "password2", "lee", 30);
        userService.join(userDto2);
        User user2 = userService.findByEmail(userDto2.getEmail());

        UserRequestDto userDto3 = new UserRequestDto("email3@naver.com", "password3", "park", 27);
        userService.join(userDto3);
        User user3 = userService.findByEmail(userDto3.getEmail());

        userIds.add(user2.getId());

        String meetId = meetService.save("title", "content", userIds);

        Meet meeting = meetService.findMeeting(meetId);

        Assertions.assertEquals(meeting.getTitle(), "title");
        Assertions.assertEquals(meeting.getContent(), "content");
        Assertions.assertTrue(meeting.getUserIds().containsAll(userIds));
        Assertions.assertFalse(meeting.getUserIds().contains(user3.getId()));

        // 회의에 참여한 사용자가 아니면 조회 불가능
        assertThrows(NotYourMeetingException.class, () ->
                meetService.findOne(meetId, user3.getId()), "사용자가 참여한 회의가 아닙니다.");
    }
}
