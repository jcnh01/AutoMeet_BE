package com.AutoMeet.domain.user.service;

import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.dto.request.UserResponseDto;

public interface UserService {
    public UserResponseDto join(UserRequestDto request);
}
