package com.AutoMeet.domain.meet.service;

import com.AutoMeet.domain.comment.dto.response.CommentListResponse;
import com.AutoMeet.domain.meet.dto.request.UpdateMeetRequest;
import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.dto.response.MeetingResponse;
import com.AutoMeet.domain.meet.dto.response.VideoAnalysisResponse;
import com.AutoMeet.domain.meet.exception.NotYourMeetingException;
import com.AutoMeet.domain.meet.model.Analysis;
import com.AutoMeet.domain.meet.model.Meet;
import com.AutoMeet.domain.meet.repository.MeetRepository;
import com.AutoMeet.domain.meetingRoom.exception.MeetingNotExistException;
import com.AutoMeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;
    private final UserRepository userRepository;

    private String flask_url = "http://54.82.4.8:5000";

    @Override
    public String textSummarization(String recordingUrl) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = flask_url + "/api/text_summarization";

        // URL에서 파일을 다운로드
        Path tempFile = Files.createTempFile("recording", ".wav");
        try (InputStream in = new URL(recordingUrl).openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println(recordingUrl);
        System.out.println(tempFile);

        // 파일을 MultiValueMap으로 준비
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource fileResource = new FileSystemResource(tempFile.toFile());
        body.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        // Flask 서버에 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 임시 파일 삭제
        Files.delete(tempFile);

        return response.getBody();
    }

    @Override
    public String summarization(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();
        String url = flask_url + "/api/text_summarization";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, List<String>>> response = restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, List<String>>>() {});

        List<String> summaries = response.getBody().get("summary");

        return summaries.get(0);
    }

    @Override
    public Long audioAnalysis(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();
        String url = flask_url + "/api/audio_analysis";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Long>> response = restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Long>>() {});

        return response.getBody().get("score");
    }

    @Override
    @Transactional
    public void textAnalysis(String meetingId, MultipartFile file, Long userId) {
        Long audioScore = audioAnalysis(file);
        Meet meeting = findMeeting(meetingId);

        Analysis analysis = Analysis.builder()
                .userId(userId).
                sentimentScore(audioScore).build();

        meeting.addAnalysis(analysis);

        saveMeeting(meeting);
    }

    @Override
    public VideoAnalysisResponse videoAnalysis(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();
        String url = flask_url + "/api/video_analysis";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<VideoAnalysisResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, VideoAnalysisResponse.class);

        return response.getBody();
    }

    @Override
    @Transactional
    public void videoAnalysisSave(String meetingId, MultipartFile file, Long userId) {
        VideoAnalysisResponse response = videoAnalysis(file);
        Long score = response.getScore();
        Long concentration = response.getConcentrationRatio();
        Meet meeting = findMeeting(meetingId);

        for (Analysis analysis : meeting.getAnalysisList()) {
            if (analysis.getUserId().equals(userId)) {
                analysis.changeAnalysis(score, concentration);
                break;
            }
        }

        saveMeeting(meeting);
    }

    @Override
    @Transactional
    public String save(String title, String summarization) {

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = ZonedDateTime.of(LocalDateTime.now(), seoulZoneId);

        Meet meet = Meet.builder()
                .title(title)
                .content(summarization)
                .finishedTime(seoulTime.toLocalDateTime())
                .build();

        Meet meeting = meetRepository.save(meet);

        return meeting.get_id();
    }

    @Override
    public List<MeetListResponse> findMeets(Long userId) {
        List<Meet> meets = meetRepository.findByAnalysisListUserId(userId);

        List<MeetListResponse> meetList = meets.stream().map((meet) ->
                        new MeetListResponse(meet.get_id(), meet.getTitle(), meet.getContent(),
                                meet.getFinishedTime()))
                .collect(Collectors.toList());
        return meetList;
    }

    @Override
    public MeetingResponse findOne(String meetingId, Long userId) {
        Meet meeting = findMeeting(meetingId);

        List<Long> userIds = meeting.getAnalysisList().stream().map(a -> a.getUserId()).collect(Collectors.toList());

        if (!userIds.contains(userId)) {
            throw new NotYourMeetingException(meetingId);
        }

        List<String> userNames = userIds.stream()
                .map(id -> userRepository.findNameById(id))
                .collect(Collectors.toList());

        List<CommentListResponse> comments = new ArrayList<>();
        if (meeting.getComments() != null) {
            comments = meeting.getComments().stream()
                    .map(comment -> new CommentListResponse(comment.getId(),
                            userRepository.findNameById(comment.getUserId()),
                            comment.getContent(), comment.getCreatedAt()))
                    .collect(Collectors.toList());
        }

        Analysis userAnalysis = meeting.getAnalysisList().stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElse(null);

        return new MeetingResponse(meeting.get_id(), meeting.getTitle(), meeting.getContent(),
                userNames, meeting.getFinishedTime(), comments, userAnalysis);
    }

    @Override
    @Transactional
    public void updateMeeting(String meetingId, Long userId, UpdateMeetRequest request) {
        Meet meeting = findMeeting(meetingId);

        List<Long> userIds = meeting.getAnalysisList().stream().map(a -> a.getUserId()).collect(Collectors.toList());

        if (!userIds.contains(userId)) {
            throw new NotYourMeetingException(meetingId);
        }

        meeting.updateMeeting(request.getTitle(), request.getContent());

        meetRepository.save(meeting);
    }

    @Override
    public Meet findMeeting(String meetingId) {
        return meetRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotExistException(meetingId));
    }

    @Override
    public void saveMeeting(Meet meeting) {
        meetRepository.save(meeting);
    }
}
