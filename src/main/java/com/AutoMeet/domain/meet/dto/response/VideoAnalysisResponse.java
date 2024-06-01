package com.AutoMeet.domain.meet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoAnalysisResponse {
    private Long concentrationRatio;
    private Long score;
}
