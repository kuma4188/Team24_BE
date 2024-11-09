# Team24_BE
24조 백엔드 - 박수빈

## 개발 스프린트 _ 타임리프 생성

# 1차 수정 (로그인문제 + 유저프로파일)
- refactor : 데이터값이 NULL 일때도 작동되도록 수정
  -  CustomAuthenticationPrincipalArgumentResolver 에서 NULL 예외처리를 통해서
  오류 방지
- retactor : 토큰을 러프하게 받아오기
  - 헤더파일 검증이 필요한데 , 일단은 타임리프를 확인하기위해서 스킵
  -  gethandler로 authorization을 받아오지 못하는 문제를 발견
  -  TokenStorage을 생성해서 러프하게 토큰을 auhtorizaion에다가 주입
- retactor : 로그인후 리디렉션 오류떠서 Getmapping(/) 추가
  - 로그인을 성공했을때 404 오류가 떠서 homeController 생성
  -  Getmapping("/") 생성
- feat : 로그인 성공시 home.html으로 리디렉션
  - 로그인 성공시 home.html값 반환.
- refactor : 유저프로파일 조회 ,수정 생성
    - UserProfile.html 생성하고 , 유저정보 수정후 multipart form-data으로 controller에 전송
    - UserProfileRequest 에서 MultipartFile image 항목 추가
    -  UserProfileController 에서 RequestParam값을 하나씩 주입하는걸로 수정
    - UserProfileService 에서 유저정보 수정후 저장하는 방식으로 수정
    - UserProfile.html 생성하고 , 유저정보 수정후 multipart form-data으로 controller에 전송


# 2차 수정 ()
