package io.hhplus.architecture.controller;

import io.hhplus.architecture.dto.LectureApplyHistoryDto;
import io.hhplus.architecture.dto.LectureDto;
import io.hhplus.architecture.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {


    private final LectureService lectureService;


    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    /***
     * 특강 가능한 목록 조회 API
     * @return
     */
    @GetMapping("/available")
    public ResponseEntity<List<LectureDto>> getAvailableLectures() {
        List<LectureDto> availableLectures = lectureService.getAvailableLectures();
        return ResponseEntity.ok(availableLectures);
    }


    /***
     * 신청 완료 목록 조회 API
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<LectureApplyHistoryDto>> getAppliedLectures(@PathVariable Long userId) {
        LectureApplyHistoryDto history = lectureService.getAppliedLectures(userId);
        return ResponseEntity.ok(Collections.singletonList(history));
    }
}
