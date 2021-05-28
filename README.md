# INFO

## 프로젝트 목적

- 개인 포트폴리오를 작성 할 수 있는 웹 서비스

---

## 스키마

- 프로젝트명: INFO

- 프로젝트 참여 인원: 1명 (김건영)

- 프로젝트 주요 기능: 포트폴리오 열람, 포트폴리오 작성, 로그인기능, 회원가입 기능

- 사용 기술 스택: JAVA SPRING , SPRING JPA, Thymleaf

- 사용 데이터베이스: Mysql

---

## 1. 테이블 설계

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
 
---

### To do List

- security

- space template view 

- junit
