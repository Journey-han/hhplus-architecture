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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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


	@Test
	@DisplayName("동일한 유저가 같은 특강을 5번 신청할 때, 1번만 성공")
	public void testDuplicateLectureApplication() throws InterruptedException {
		Long lectureInfoId = 101L;
		Long userId = 1L;

		// 초기 상태
		LectureInfo lectureInfo = new LectureInfo();
		lectureInfo.setAppliedCnt(25);
		lectureInfo.setLectureDate(LocalDate.now().plusDays(1));  // 미래 날짜

		// Mock 설정
		when(lectureInfoRepository.findByLectureInfoId(lectureInfoId))
				.thenReturn(Optional.of(lectureInfo));

		// 첫 번째 호출만 성공하고, 이후에는 중복 신청이 발생
		when(lectureApplyHistoryRepository.existsByUserIdAndLectureInfo_LectureInfoId(userId, lectureInfoId))
				.thenReturn(false)  // 첫 번째 호출은 신청 가능
				.thenReturn(true);   // 중복 신청으로 실패 처리

		// 동일한 유저(userId)가 같은 특강(lectureInfoId)을 5번 신청 시도
		ExecutorService executor = Executors.newFixedThreadPool(5);  // 5명의 스레드로 동시에 신청 시도
		for (int i = 0; i < 10; i++) {
			executor.submit(() -> {
				lectureApplyService.applyForLecture(lectureInfoId, userId);
			});
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);

		// 1번만 호출되었는지 확인
		verify(lectureInfoRepository, times(1)).save(lectureInfo);

		// 1번만 저장되었는지 확인
		verify(lectureApplyHistoryRepository, times(1)).save(any(LectureApplyHistory.class));
	}


}
