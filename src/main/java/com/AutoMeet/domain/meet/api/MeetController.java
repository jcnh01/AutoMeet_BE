package com.AutoMeet.domain.meet.api;

import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.service.MeetService;
import com.AutoMeet.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meet")
public class MeetController {

    private final MeetService meetService;

    @GetMapping("")
    public ResponseEntity<List<MeetListResponse>> findMeets(@AuthenticationPrincipal PrincipalDetails principal) {
        List<MeetListResponse> meetingList = meetService.findMeet(principal.getUser().getId());

        return ResponseEntity.ok(meetingList);
    }
}
