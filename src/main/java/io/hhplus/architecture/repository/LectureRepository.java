package io.hhplus.architecture.repository;

import io.hhplus.architecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByLectureInfo_AppliedCntLessThanAndLectureInfo_LectureDateAfter(int appliedCnt, LocalDate currentDate);
}
