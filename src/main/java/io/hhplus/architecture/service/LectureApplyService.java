package io.hhplus.architecture.service;


import io.hhplus.architecture.entity.LectureApplyHistory;
import io.hhplus.architecture.entity.LectureInfo;
import io.hhplus.architecture.exception.LectureException;
import io.hhplus.architecture.repository.LectureApplyHistoryRepository;
import io.hhplus.architecture.repository.LectureInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LectureApplyService {

    private final LectureInfoRepository lectureInfoRepository;
    private final LectureApplyHistoryRepository lectureApplyHistoryRepository;

    public  LectureApplyService(LectureInfoRepository lectureInfoRepository,
                                LectureApplyHistoryRepository lectureApplyHistoryRepository) {
        this.lectureInfoRepository = lectureInfoRepository;
        this.lectureApplyHistoryRepository = lectureApplyHistoryRepository;
    }

    /**
     * 특강 신청 - 선착순 30명 이후 신청에 대한 실패 처리
     * @param lectureInfoId
     * @param userId
     */
    @Transactional
    public void applyForLecture(Long lectureInfoId, Long userId) {

        // 비관적 락으로 LectureInfo 조회
        LectureInfo lectureInfo = lectureInfoRepository.findByLectureInfoId(lectureInfoId)
                .orElseThrow(() -> new LectureException("특강 정보를 찾을 수 없습니다."));

        // 특강 날짜 검증: 특강 날짜가 신청 날짜보다 뒤에 있는지 확인
        if (!lectureInfo.getLectureDate().isAfter(LocalDate.now())) {
            throw new LectureException("특강 날짜는 신청 날짜보다 이후여야 합니다.");
        }

        // 중복 신청 여부 확인
        if (lectureApplyHistoryRepository.existsByUserIdAndLectureInfo_LectureInfoId(userId, lectureInfoId)) {
            throw new LectureException("해당 특강은 이미 신청한 특강입니다.");
        }

        // 신청자 수 검증
        if (lectureInfo.getAppliedCnt() >= 30) {
            throw new LectureException("해당 특강은 정원(30명)을 초과하였습니다.");
        }

        // 신청자 수 증가
        lectureInfo.setAppliedCnt(lectureInfo.getAppliedCnt() + 1);
        lectureInfoRepository.save(lectureInfo);

        // 신청 내역 저장
        LectureApplyHistory history = new LectureApplyHistory();
        history.setUserId(userId);
        history.setLectureInfo(lectureInfo);
        history.setApplyDate(LocalDate.now());

        lectureApplyHistoryRepository.save(history);
    }
}
