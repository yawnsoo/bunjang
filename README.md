### git
/*pull*/
git pull origin post

/*push*/
git add .
git commit -m '[메세지]'
git push -u origin post


### Spring
sudo ./gradlew clean build
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar


# 2022/09/14
ERD설계 진행중
개발일지

# 2022/09/14 (수)
ERD설계 진행 (40%)

# 2022/09/15 (목)
ERD설계 완료
API 명세서 작성 진행
서버 환경구축 진행

# 2022/09/19 (월)
- "판매글 등록" API

# 2022/09/20 (화)
- "태그 등록" API ()
- "사진 등록" API

# 2022/09/21 (수)
- "카테고리 선택" API

# 2022/09/22 (목)
### getPostsByCategory
- "판매글 카테고리별 조회" API
### Edit createPost
- "사진 등록" 수정 : 1개씩 추가 >>> 여러개 추가
- "태그 등록" 수정 : 1개씩 추가 >>> 여러개 추가
- "판매글 등록" API 수정
  1."사진 등록" 기능 합침
  2."태그 등록" 기능 합침

# 2022/09/23(금)
- 2차 피드백
  
  - API 최소 25개 이상.
  - user 테이블에 point 추가할 것
  - 제출
    27일 12시 까지 제출
    공들인 API, 클라이언트에서 못 보여준 API, 주요기능 API 위주로 발표하면 좋음
    발표 시간 : 5~10분

# 2022/09/24(토)
- S3 구축
- 판매글 상세 정보 조회 API
- 판매중 상품 조회(마이페이지) API
- 판매중 상품 조회(홈) API

- 홈페이지 API
- "옵션 선택" API ()
- "지역 선택" API ()



