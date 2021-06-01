# INFO

## 프로젝트 목적

- 개인 포트폴리오를 작성 할 수 있는 웹 서비스

---

## 스키마

- 프로젝트명: INFO

- 프로젝트 참여 인원: 1명 (김건영)

- 프로젝트 주요 기능: 포트폴리오 열람, 포트폴리오 작성, 로그인기능, 회원가입 기능

- 사용 기술 스택: JAVA SPRING , SPRING JPA, Thymleaf, Spring Security

- 사용 데이터베이스: Mysql

---

## 1. DB 테이블 설계

<img width="752" alt="infodb_table_2" src="https://user-images.githubusercontent.com/60494603/120187085-4f165280-c24f-11eb-95f0-e7002e86aa8e.png">

---


## 2. API 설계

---

## 3. 화면 설계

- 메인 화면 

<img width="1440" alt="info_main" src="https://user-images.githubusercontent.com/60494603/119880342-1d5b6e00-bf67-11eb-8b98-ee4b03e5f4f8.png">

- 회원 가입 화면

<img width="1440" alt="info_join" src="https://user-images.githubusercontent.com/60494603/119880332-1a607d80-bf67-11eb-9b19-766937fb4e3d.png">

- 로그인 화면

<img width="1440" alt="Info_login" src="https://user-images.githubusercontent.com/60494603/119880334-1c2a4100-bf67-11eb-9a36-0bda996bb1c6.png">

- 포트폴리오-프로필 화면

<img width="1440" alt="profile" src="https://user-images.githubusercontent.com/60494603/120186150-12962700-c24e-11eb-959e-a31490d95617.png">

- 포트폴리오-프로젝트 화면

<img width="1440" alt="content_view" src="https://user-images.githubusercontent.com/60494603/120186145-1164fa00-c24e-11eb-9f33-d6434cb2525c.png">

- 프로젝트 생성 화면

<img width="1440" alt="create_content" src="https://user-images.githubusercontent.com/60494603/120186156-145fea80-c24e-11eb-820e-ba6e58ff1c0a.png">

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

---

### To do List

- <pre> 태그 폰트 

- 뷰 별 권한 제한

- junit


