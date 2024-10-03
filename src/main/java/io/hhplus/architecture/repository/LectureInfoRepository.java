package io.hhplus.architecture.repository;

import io.hhplus.architecture.entity.LectureInfo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface LectureInfoRepository extends JpaRepository<LectureInfo, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LectureInfo> findByLectureInfoId(Long lectureInfoId);

}
