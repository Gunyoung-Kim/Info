# INFO [![Build Status](https://travis-ci.com/Gunyoung-Kim/Info.svg?branch=master)](https://travis-ci.com/Gunyoung-Kim/Info)

최신 버전 : ver 0.0.12


## 프로젝트 목적

- 개인 포트폴리오를 작성 할 수 있는 웹 서비스

---

## 스키마

- 프로젝트명: INFO

- 프로젝트 참여 인원: 1명 (김건영)

- 개발 기간: 2021.05.26 ~ 2021.06.09 (첫 버전 배포일)

- 프로젝트 주요 기능: 포트폴리오 열람, 포트폴리오 작성, 로그인기능, 회원가입 기능

- 사용 기술, 개발 환경 : SPRING Boot , SPRING JPA, Spring Security, JUnit5, Thymleaf, Maven, open-jdk8, HTML, CSS, Javascript

- 사용 데이터베이스

   - Prod: Mysql 8.0

   - Test: H2
   
- 접속 URL : [INFO](https://www.info-gun.net)

---

## 코드 배포 구조 

무중단 자동 배포 

![info_deploy_structure2](https://user-images.githubusercontent.com/60494603/123542037-624f0c00-d782-11eb-9519-cce022414f5e.png)

--- 

## Git 브랜치 전략 

### Git-Flow 전략 사용

- master: 실제 배포가 진행되는 브랜치

- develop: 버그 수정, 리팩토링이 진행되는 브랜치 

- feature: 기능 개발이 진행되는 브랜치

- release: 주석 작성, README 수정 등 배포 준비 진행되는 브랜치 

- hotfix: 배포시 문제가 발생했을때 빠른 조치를 위해 사용하는 브랜치

-> master, develop 브랜치만이 계속 유지 나머지는 생성 삭제 반복

---

## DB 테이블 설계

<img width="752" alt="infodb_table_2" src="https://user-images.githubusercontent.com/60494603/120187085-4f165280-c24f-11eb-95f0-e7002e86aa8e.png">

---

## Controller 설계

- 컨트롤러 설계: [컨트롤러 설계 보러가기](https://github.com/Gunyoung-Kim/Info_ABOUT/blob/master/Controller.md)

---

## 화면 설계

- 메인 화면 

<img width="1440" alt="mainView" src="https://user-images.githubusercontent.com/60494603/120885865-15b25e00-c626-11eb-829b-1a32b336597f.png">

- 회원 가입 화면

<img width="1440" alt="join" src="https://user-images.githubusercontent.com/60494603/121858668-cf4db500-cd31-11eb-84a7-3b2072a23e6e.png">

- 로그인 화면

<img width="1440" alt="login614" src="https://user-images.githubusercontent.com/60494603/121858696-d5dc2c80-cd31-11eb-89c3-6f029807598b.png">


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

- 개발일지: [개발일지 보러가기](https://github.com/Gunyoung-Kim/Info_ABOUT/blob/master/Development_Log.md)

---

## 버전 관리

- 버전 관리: [버전관리 보러가기](https://github.com/Gunyoung-Kim/Info_ABOUT/blob/master/Version_Note.md)

---

Info 2021~
