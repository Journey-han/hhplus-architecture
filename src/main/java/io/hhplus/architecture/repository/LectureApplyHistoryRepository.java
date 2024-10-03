package io.hhplus.architecture.repository;

import io.hhplus.architecture.entity.LectureApplyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureApplyHistoryRepository extends JpaRepository<LectureApplyHistory, Long> {

    boolean existsByUserIdAndLectureInfo_LectureInfoId(Long userId, Long lectureInfoId);

    List<LectureApplyHistory> getUserByUserId(Long userId);
}