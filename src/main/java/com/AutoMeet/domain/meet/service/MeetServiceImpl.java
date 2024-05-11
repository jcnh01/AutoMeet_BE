package com.AutoMeet.domain.meet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

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
}
