package io.hhplus.architecture;

import io.hhplus.architecture.entity.LectureApplyHistory;
import io.hhplus.architecture.entity.LectureInfo;
import io.hhplus.architecture.exception.LectureException;
import io.hhplus.architecture.repository.LectureApplyHistoryRepository;
import io.hhplus.architecture.repository.LectureInfoRepository;
import io.hhplus.architecture.service.LectureApplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HhplusArchitectureApplicationTests {

	@InjectMocks
	private LectureApplyService lectureApplyService;

	@Mock
	private LectureInfoRepository lectureInfoRepository;

	@Mock
	private LectureApplyHistoryRepository lectureApplyHistoryRepository;


	@Test
	@DisplayName("중복 신청 방지 테스트")
	void testDuplicateApplication() {
		Long userId = (long) 1;
		Long lectureInfoId = (long) 1;

		// 특강 정보 가정
		LectureInfo lectureInfo = new LectureInfo();
		lectureInfo.setAppliedCnt(10);  // 신청자 수 설정
		lectureInfo.setLectureDate(LocalDate.now().plusDays(1));  // 특강 날짜 설정

		// When
		// 특강 정보 조회 설정
		when(lectureInfoRepository.findByLectureInfoId(lectureInfoId)).thenReturn(Optional.of(lectureInfo));

		// 이미 신청된 내역이 있다고 가정
		when(lectureApplyHistoryRepository.existsByUserIdAndLectureInfo_LectureInfoId(userId, lectureInfoId))
				.thenReturn(true);

		Exception exception = assertThrows(LectureException.class, () -> {
			lectureApplyService.applyForLecture(lectureInfoId, userId);
		});

		// Then
		assertEquals("이미 신청한 특강입니다.", exception.getMessage());
	}

	@Test
	@DisplayName("신청자 정원 초과 방지 테스트")
	void testOverCapacity() {
		Long userId = 1L;
		Long lectureInfoId = 101L;

		// 정원을 초과한 상태
		LectureInfo lectureInfo = new LectureInfo();
		lectureInfo.setLectureInfoId(101L);
		lectureInfo.setAppliedCnt(30);
		lectureInfo.setLectureDate(LocalDate.now().plusDays(1));  // 미래 날짜

		// When
		when(lectureInfoRepository.findByLectureInfoId(lectureInfoId))
				.thenReturn(Optional.of(lectureInfo));

		// 테스트 실행 및 예외 확인
		Exception exception = assertThrows(LectureException.class, () -> {
			lectureApplyService.applyForLecture(lectureInfoId, userId);
		});

		assertEquals("해당 특강은 정원(30명)을 초과하였습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("특강 날짜 검증")
	public void testInvalidLectureDate() {
		Long userId = 1L;
		Long lectureInfoId = 101L;

		// 과거 날짜로 설정
		LectureInfo lectureInfo = new LectureInfo();
		lectureInfo.setLectureDate(LocalDate.now().minusDays(1));  // 과거 날짜

		// Mock 설정
		when(lectureInfoRepository.findByLectureInfoId(lectureInfoId)).thenReturn(Optional.of(lectureInfo));

		// 테스트 실행 및 예외 확인
		Exception exception = assertThrows(LectureException.class, () -> {
			lectureApplyService.applyForLecture(lectureInfoId, userId);
		});

		assertEquals("특강 날짜는 신청 날짜보다 이후여야 합니다.", exception.getMessage());
	}

	@Test
	@DisplayName("정상적인 신청 처리 테스트")
	public void testSuccessfulApplication() {
		Long userId = 1L;
		Long lectureInfoId = 101L;

		// 정상적인 신청 가능한 상태라고 가정
		LectureInfo lectureInfo = new LectureInfo();
		lectureInfo.setAppliedCnt(25);
		lectureInfo.setLectureDate(LocalDate.now().plusDays(1));  // 미래 날짜

		// Mock 설정
		when(lectureInfoRepository.findByLectureInfoId(lectureInfoId)).thenReturn(Optional.of(lectureInfo));
		when(lectureApplyHistoryRepository.existsByUserIdAndLectureInfo_LectureInfoId(userId, lectureInfoId))
				.thenReturn(false);

		// 테스트 실행
		lectureApplyService.applyForLecture(lectureInfoId, userId);

		// 검증: 신청자 수 증가 및 저장 여부 확인
		assertEquals(26, lectureInfo.getAppliedCnt());
		verify(lectureInfoRepository).save(lectureInfo);
		verify(lectureApplyHistoryRepository).save(any(LectureApplyHistory.class));
	}

}
