package com.AutoMeet.domain.user.api;

import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.dto.request.UserResponseDto;
import com.AutoMeet.domain.user.service.UserService;
import com.AutoMeet.domain.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody UserRequestDto request) {
        UserResponseDto response = userService.join(request);
        return ResponseEntity.ok(response);
    }
}