## 테이블 ERD 설계
![스크린샷 2024-10-04 오전 12 55 38](https://github.com/user-attachments/assets/3f5c7737-d50b-403a-b621-6244256308aa)

1. LECTURE - 특강 테이블
- **lecturerId** : 특강 강연자 ID. Primary Key로 설정
- **lecturer** : 특강 강연자

2. LECTURE_INFO - 특강 정보 테이블
- **lectureInfoId** : 특강 정보 ID. Primary Key로 설정
- **lectureId** : 특강 테이블의 특강 ID. 참조키로 특강테이블의 lectureId와 N:1 관계를 맺고있다.
- **appliedCnt** : 현재까지 신청한 특강 신청자 수
- **lectureDate** : 특강 날짜
 
3. LECTURE_APPLY_HISTORY - 특강 신청 내역 테이블
- **lectureHistoryId** : 특강 신청 내역 ID. Primary Key로 설정
- **userId** : 특강 신청한 유저 ID
- **lectureInfoId** : 특강 정보 테이블의 특강정보 ID
- **applyDate** : 특강 신청한 날짜

## 테이블 설계 이유
- 우선 특강 테이블엔 강연자 ID와 강연자 이름만 저장하여 강연자의 정보가 바뀌어도 특강 정보 테이블에 영향이 가지 않게 설계했다.
- 특강 정보 테이블은 특강 테이블의 강연자 ID를 참조하여 강연자가 여러 개의 특강을 생성해도 그룹으로 관리 할 수있게 설계했다.
- 특강 신청 내역 테이블은 특강을 신청한 유저 ID로 특강 정보를 확인할 수 있다.
- 한 명의 특강 강연자는 여러 개의 특강 정보를 가질 수 있다.
- 한 명의 유저는 한 명의 강연자가 가진 여러 개의 특강 정보를 가질 수 있다.
- 한 명의 유저는 여러 명의 강연자가 가진 여러 개의 특강 정보를 가질 수 있다. 
