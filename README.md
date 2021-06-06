# INFO

## 프로젝트 목적

- 개인 포트폴리오를 작성 할 수 있는 웹 서비스

---

## 스키마

- 프로젝트명: INFO

- 프로젝트 참여 인원: 1명 (김건영)

- 프로젝트 주요 기능: 포트폴리오 열람, 포트폴리오 작성, 로그인기능, 회원가입 기능

- 사용 기술 스택: JAVA SPRING , SPRING JPA, Spring Security, JUnit5, Thymleaf, HTML, CSS, Javascript

- 사용 데이터베이스

   - Prod: Mysql

   - Test: H2

---

## DB 테이블 설계

<img width="752" alt="infodb_table_2" src="https://user-images.githubusercontent.com/60494603/120187085-4f165280-c24f-11eb-95f0-e7002e86aa8e.png">

---


## Controller 설계

<img width="886" alt="SpaceControllerAPI" src="https://user-images.githubusercontent.com/60494603/120898420-c4c25a00-c665-11eb-96a8-5fb1841a74b0.png">


<img width="879" alt="ContentController" src="https://user-images.githubusercontent.com/60494603/120898424-c724b400-c665-11eb-9a24-7a353915a5d8.png">


<img width="883" alt="PersonController" src="https://user-images.githubusercontent.com/60494603/120898425-c855e100-c665-11eb-93cd-05f59780e1db.png">

---

## 테스트

- 테스트 단위: 통합 단위 테스트

- 전용 데이터베이스: H2 - 임베디드 데이터베이스

- ContentController Test 

<img width="737" alt="ContentCotrollerTest" src="https://user-images.githubusercontent.com/60494603/120661338-21394400-c4c3-11eb-92d0-0a88a3eb81e5.png">



- SpaceController Test

<img width="641" alt="spacecontrollertest" src="https://user-images.githubusercontent.com/60494603/120682681-b21a1a80-c4d7-11eb-996a-aae5b3c2c54a.png">


- PersonController Test

<img width="612" alt="personcontrollertest" src="https://user-images.githubusercontent.com/60494603/120689662-4dfb5480-c4df-11eb-9df7-e619c2387341.png">

- RestfulController Test

<img width="626" alt="restfulcontrollertest" src="https://user-images.githubusercontent.com/60494603/120881239-b561f300-c60a-11eb-8fb6-0a4b2c4fb237.png">


- SpaceService Test

<img width="573" alt="spaceservicetest" src="https://user-images.githubusercontent.com/60494603/120897430-15838400-c661-11eb-9bf0-93f7c1a4e996.png">


- ContentService Test

<img width="607" alt="contentServiceTest" src="https://user-images.githubusercontent.com/60494603/120897419-03a1e100-c661-11eb-9da5-0f70c8d1e6fd.png">


- PersonService Test

<img width="586" alt="personServiceTest" src="https://user-images.githubusercontent.com/60494603/120897414-000e5a00-c661-11eb-8e4d-e2bf783f58a6.png">


---

## 화면 설계

- 메인 화면 

<img width="1440" alt="mainView" src="https://user-images.githubusercontent.com/60494603/120885865-15b25e00-c626-11eb-829b-1a32b336597f.png">

- 회원 가입 화면

<img width="1440" alt="Join" src="https://user-images.githubusercontent.com/60494603/120502631-8e82a180-c3fd-11eb-8969-f413acafd575.png">

- 로그인 화면

<img width="1440" alt="Info_login" src="https://user-images.githubusercontent.com/60494603/119880334-1c2a4100-bf67-11eb-9a36-0bda996bb1c6.png">

- 포트폴리오-프로필 화면

<img width="1440" alt="profile" src="https://user-images.githubusercontent.com/60494603/120186150-12962700-c24e-11eb-959e-a31490d95617.png">

- 포트폴리오-프로젝트 화면

<img width="1440" alt="portfolio" src="https://user-images.githubusercontent.com/60494603/120502626-8d517480-c3fd-11eb-81dc-c1f916997f24.png">

- 프로젝트 생성 화면

<img width="1440" alt="ceate_content" src="https://user-images.githubusercontent.com/60494603/120320356-0b8a1a00-c31d-11eb-99c2-78e12b6193c3.png">

- 프로젝트 수정 화면

<img width="1440" alt="update_content" src="https://user-images.githubusercontent.com/60494603/120320662-64f24900-c31d-11eb-8a3e-c8a56b118b11.png">

- 개인 정보 수정 화면

<img width="1440" alt="update_profile" src="https://user-images.githubusercontent.com/60494603/120186152-13c75400-c24e-11eb-9615-7dec6001681c.png">

---

## 개발일지

### 2021.5.26

1. 프로젝트 설계
 
2. 데이터베이스 테이블 설계
 
3. domain package 완성
 
4. mysql 연결 코드 완성 

### 2021.5.27

1. Domain 유효성 검증 및 검증 메시지 관련 코드 추가

2. 각 종 Controller 구현
   
   - 메인 뷰, 로그인 뷰, 회워 가입 뷰 전용 Controller
   - 모든 회원 정보 가져오는 RestController

3. Domain 관련 Repository, Service 구현 

4. 메인 뷰, 로그인 뷰, 회원 가입 뷰 템플릿 생성
 
### 2021.5.28

1. Security Config, SecurityAuthenticationFilter, UserDetails, UserDetailsService 구현

2. 비밀 번호 암호화로 DB 저장

3. Content 도메인 추가 및 Content Repository, Service 구현

### 2021.5.30

1. 포트폴리오 전용 뷰 생성, 포트폴리오 전용 뷰 컨트롤러 생성

2. Spring Security와 thymeleaf extras Spring Security 를 이용한 로그인, 로그아웃 구현

### 2021.5.31

1. 프로필 수정 뷰 및 관련 뷰 컨트롤러 구현

2. 프로젝트 생성 뷰 및 관련 뷰 컨트롤러 구현

3. Space 엔티티에 필드(SNS 주소, Description) 추가

4. 프로젝트 열람 뷰 구현

### 2021.6.1

1. AuthenticationProvider 추가

2. 개인 정보 수정 URL 변경 ( 기존: url에 해당 email 노출, 해당 email 사용 -> 현재: SecurityContext에서 Authentication 가져와서 이를 활용(url에 email 노출 안되게))

3. Email 중복 확인 컨트롤러 구현

4. content 생성 컨트롤러에 유저 확인 코드 추가, content 생성 뷰 수정(보기 편하게)

5. content 수정 뷰 및 컨트롤러 구현, 해당 뷰에서 사용할 DTO 구현(ContentDTO)

### 2021.6.2

1. ViewController -> 3개의 Controller(ContentController, PersonController, SpaceController)로 분기

2. Controller 설명 주석 추가

3. Error 페이지 및 컨트롤러 구현

4. 이메일 중복확인 프론트 구현

5. 프로젝트 뷰에 프로젝트 기간 추가

### 2021.6.3

1. DbConfig 삭제 -> DB 연결 정보 application.properties로 이동

2. Test 용 데이터베이스 설정 분리 -> application-test.properties, h2 임베디드 데이터베잇 사용

3. ContentController- deleteContent 실패 처리 코드 추가

4. ContentController Test Code 작성

### 2021.6.4

1. DTO 객체들 Validation 적용

2. SpaceController Test Code 작성

3. PersonController Test Code 작성

### 2021.6.5

1. RestfulController Test Code 작성

2. 메인 페이지 리스트 페이지 구현

3. 회원 탈퇴 컨트롤러 및 테스트 코드 구현

### 2021.6.6

1. Domain 관련 Service 클래스들 (PersonService, SpaceService, ContentService) Test Code 작성

---

### To do List

- 프로젝트,회원 삭제는 프론트에서 어떻게 할지 고민

- DB 자동 삭제 for 개인 정보 보호 

- ssl

- content 개수 제한

- 배포 준비
