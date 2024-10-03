package io.hhplus.architecture.controller;

import io.hhplus.architecture.service.LectureApplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectureApply")
public class LectureApplyController {

    private final LectureApplyService lectureApplyService;

    public LectureApplyController(LectureApplyService lectureApplyService) {
        this.lectureApplyService = lectureApplyService;
    }

    /***
     * 특강 신청 API
     * @param lectureInfoId
     * @param userId
     * @return
     */
    @PostMapping("/{lectureInfoId}")
    public ResponseEntity<String> applyLecture(
            @PathVariable Long lectureInfoId,
            @RequestParam Long userId) {
        lectureApplyService.applyForLecture(lectureInfoId, userId);
        return ResponseEntity.ok("특강 신청이 완료되었습니다.");
    }
}
