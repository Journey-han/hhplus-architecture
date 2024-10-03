-- 특강 테이블
CREATE TABLE lecture (
    lecturerId LONG PRIMARY KEY,       -- 특강 ID
    lecturer VARCHAR(50)                 -- 특강자
);

-- 특강 세부 정보 테이블
CREATE TABLE lecture_info (
    lectureInfoId LONG PRIMARY KEY,    -- 특강 세부 정보 ID
    lecturerId LONG,                   -- 외래 키, 특강 ID
    appliedCnt INTEGER,                  -- 신청자 수
    lectureDate TIMESTAMP,               -- 특강 날짜
    CONSTRAINT fk_lecture FOREIGN KEY (lecturerId) REFERENCES lecture(lecturerId)  -- 외래 키 설정
);

-- 특강 신청 내역 테이블
CREATE TABLE lecture_apply_history (
    lectureHistoryId BIGINT PRIMARY KEY, -- 신청 내역 ID
    userId BIGINT,                       -- 신청자 ID
    lectureInfoId BIGINT,                -- 외래 키, 특강 세부 정보 ID
    applyDate TIMESTAMP,                 -- 신청 날짜
    CONSTRAINT fk_lecture_info FOREIGN KEY (lectureInfoId) REFERENCES lecture_info(lectureInfoId) -- 외래 키 설정
);