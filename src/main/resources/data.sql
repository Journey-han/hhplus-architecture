-- lecture 테이블에 데이터 삽입
INSERT INTO lecture (lecturerId, lecturer)
VALUES
    (1, '이둘리'),
    (2, '김또치'),
    (3, '도우너');


-- lecture_info 테이블에 데이터 삽입
INSERT INTO lecture_info (lectureInfoId, lecturerId, appliedCnt, lectureDate)
VALUES
(101, 1, 10, '2024-10-10 10:00:00'),
(102, 1, 5, '2024-10-12 14:00:00'),
(103, 2, 20, '2024-11-01 11:00:00'),
(104, 2, 18, '2024-11-05 13:00:00'),
(105, 3, 25, '2024-12-01 15:00:00');