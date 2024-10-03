package io.hhplus.architecture.dto;

import io.hhplus.architecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureApplyHistoryDto {
    private Long userId;  // 신청자 ID
    private List<LectureDto> lecture;

}
