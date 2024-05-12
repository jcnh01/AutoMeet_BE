package com.AutoMeet.domain.meet.service;

import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.dto.response.MeetingResponse;
import com.AutoMeet.domain.meet.exception.NotYourMeetingException;
import com.AutoMeet.domain.meet.model.Meet;
import com.AutoMeet.domain.meet.repository.MeetRepository;
import com.AutoMeet.domain.meetingRoom.exception.MeetingNotExistException;
import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;

    @Value("${flask_url}")
    private String flask_url;

    @Override
    public String textSummarization(String recordingUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String url = flask_url;

        HttpEntity<String> entity = new HttpEntity<>(recordingUrl);

        // flask 서버에 POST 요청 보내기
        // flask 서버에서 stt로 변환한 다음에 summarization 모델을 통한 요약본을 보내줌
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    @Override
    @Transactional
    public void save(String summarization, List<String> userNames) {

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = ZonedDateTime.of(LocalDateTime.now(), seoulZoneId);

        Meet meet = Meet.builder()
                .title("title") // 제목은 나중에 수정할 예정
                .content(summarization)
                .userNames(userNames)
                .finishedTime(seoulTime.toLocalDateTime())
                .build();

        meetRepository.save(meet);
    }

    public List<MeetListResponse> findMeets(Long userId) {
        List<Meet> meets = meetRepository.findByUserIdsContains(userId);
        List<MeetListResponse> meetList = meets.stream().map((meet) ->
                        new MeetListResponse(meet.get_id(), meet.getTitle(), meet.getContent(), meet.getFinishedTime()))
                .collect(Collectors.toList());
        return meetList;
    }

    public MeetingResponse findOne(String meetingId, Long userId) {
        Meet meeting = findMeeting(meetingId);
        if (!meeting.getUserNames().contains(userId)) {
            throw new NotYourMeetingException(meetingId);
        }
        return new MeetingResponse(meeting.get_id(), meeting.getTitle(), meeting.getContent(),
                meeting.getUserNames(), meeting.getFinishedTime());
    }

    public Meet findMeeting(String meetingId) {
        return meetRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotExistException(meetingId));
    }
}
