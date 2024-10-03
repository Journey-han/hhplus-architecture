package io.hhplus.architecture.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@Table(name="LECTURE")
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long lecturerId;

    private String lecturer;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LectureInfo> lectureInfo;

    public static Lecture toEntity(Lecture dto) {
        return Lecture.builder()
                .id(dto.getId())
                .lecturer(dto.getLecturer())
                .build();
    }

}
