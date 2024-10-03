package io.hhplus.architecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureInfoDto {
    private Long lectureInfoId;
    private LocalDate lectureDate;  // 특강 날짜
    private int appliedCnt;
}
