package io.hhplus.architecture.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureDto {
    private Long lecturerId;
    private String lecturer;        //  특강자
    private List<LectureInfoDto> lectureInfo;    // 강의정보
}
