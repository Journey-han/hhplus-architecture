package io.hhplus.architecture.entity;

import io.hhplus.architecture.dto.LectureDto;
import io.hhplus.architecture.dto.LectureInfoDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="LECTURE_INFO")
public class LectureInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lectureInfoId;

    @ManyToOne
    @JoinColumn(name="lecturer_id")
    private Lecture lecture;

    private LocalDate lectureDate;

    private int appliedCnt;

}
