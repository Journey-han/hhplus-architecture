package io.hhplus.architecture.service;

import io.hhplus.architecture.dto.LectureApplyHistoryDto;
import io.hhplus.architecture.dto.LectureDto;
import io.hhplus.architecture.dto.LectureInfoDto;
import io.hhplus.architecture.entity.Lecture;
import io.hhplus.architecture.entity.LectureApplyHistory;
import io.hhplus.architecture.repository.LectureApplyHistoryRepository;

import io.hhplus.architecture.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LectureApplyHistoryRepository lectureApplyHistoryRepository;

    public LectureService(LectureRepository lectureRepository, LectureApplyHistoryRepository lectureApplyHistoryRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureApplyHistoryRepository = lectureApplyHistoryRepository;
    }

    /***
     * 선택 가능한 특강 조회
     * @return
     */
    public List<LectureDto> getAvailableLectures() {
        LocalDate today = LocalDate.now();
        List<Lecture> lectures = lectureRepository.findByLectureInfo_AppliedCntLessThanAndLectureInfo_LectureDateAfter(30, today);
        List<LectureDto> result = new ArrayList<>();

        for (Lecture lecture : lectures) {
            LectureDto lectureDto = new LectureDto();
            lectureDto.setLecturerId(lecture.getLecturerId());
            lectureDto.setLecturer(lecture.getLecturer());

            List<LectureInfoDto> lectureInfoDtoList = lecture.getLectureInfo().stream()
                    .map(info -> {
                        LectureInfoDto infoDto = new LectureInfoDto();
                        infoDto.setLectureInfoId(info.getLectureInfoId());
                        infoDto.setLectureDate(info.getLectureDate());
                        infoDto.setAppliedCnt(info.getAppliedCnt());
                        return infoDto;
                    })
                    .collect(Collectors.toList());

            lectureDto.setLectureInfo(lectureInfoDtoList);
            result.add(lectureDto);
        }

        return result;
    }

    /***
     * 선택한 특강 조회
     * @param userId
     * @return
     */
    public LectureApplyHistoryDto getAppliedLectures(Long userId) {
        List<LectureApplyHistory> histories = lectureApplyHistoryRepository.getUserByUserId(userId);

        List<LectureDto> lectureDtoList = histories.stream()
                .collect(Collectors.groupingBy(history -> history.getLectureInfo().getLecture()))  // 그룹화
                .entrySet().stream()
                .map(entry -> {
                    Lecture lecture = entry.getKey();  // 그룹화된 특강(Lecture)
                    List<LectureApplyHistory> groupedHistories = entry.getValue();  // 신청 내역

                    LectureDto lectureDto = new LectureDto();
                    lectureDto.setLecturerId(lecture.getId());
                    lectureDto.setLecturer(lecture.getLecturer());

                    List<LectureInfoDto> lectureInfoDtoList = groupedHistories.stream()
                            .map(history -> {
                                LectureInfoDto infoDto = new LectureInfoDto();
                                infoDto.setLectureInfoId(history.getLectureInfo().getLectureInfoId());
                                infoDto.setLectureDate(history.getLectureInfo().getLectureDate());  // 특강 날짜 설정
                                infoDto.setAppliedCnt(history.getLectureInfo().getAppliedCnt());
                                return infoDto;
                            })
                            .collect(Collectors.toList());

                    lectureDto.setLectureInfo(lectureInfoDtoList);
                    return lectureDto;
                })
                .collect(Collectors.toList());

        // 특정 userId의 신청 내역 최종 반환
        LectureApplyHistoryDto historyDto = new LectureApplyHistoryDto();
        historyDto.setUserId(userId);  // userId 설정
        historyDto.setLecture(lectureDtoList);  // 신청 내역 설정

        return historyDto;
    }
}
