package io.hhplus.architecture.entity;

import io.hhplus.architecture.dto.LectureApplyHistoryDto;
import io.hhplus.architecture.dto.LectureInfoDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="LECTURE_APPLY_HISTORY")
public class LectureApplyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private LectureInfo lectureInfo;

    private LocalDate applyDate;

}
