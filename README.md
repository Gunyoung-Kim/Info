# INFO [![Build Status](https://travis-ci.com/Gunyoung-Kim/Info.svg?branch=master)](https://travis-ci.com/Gunyoung-Kim/Info)

최신 버전 : ver 0.1.4 (2021.10.20)

## 프로젝트 목적

- 개인 포트폴리오를 작성 할 수 있는 웹 서비스

---

## 스키마

- 프로젝트명: INFO

- 프로젝트 참여 인원: 1명 (김건영)

- 개발 기간: 2021.05.26 ~ 2021.06.09 (첫 버전 배포일)

- 프로젝트 주요 기능: 포트폴리오 열람, 포트폴리오 작성, 로그인기능, 회원가입 기능

- 사용 기술, 개발 환경 : SPRING Boot , SPRING JPA, Spring Security, JUnit5, SpringMVC, open-jdk8

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

### 테스트 방식 

- 테스트 커버리지 : 100%

- How to

```bash

# 통합 테스트 + 단위 테스트 -> instructions: 100%, branches: 100%
$ ./mvnw clean package

# 단위 테스트 -> instructions: 100%, branches: 100%
$ ./mvnw clean package -DexcludedGruops=integration
```

<img width="655" alt="infoT_0_1_4" src="https://user-images.githubusercontent.com/60494603/138023639-da5791f8-7bcf-41b2-bb39-8407d2e2950a.png">

---

## DB 테이블 설계

![Info_0_1_0](https://user-images.githubusercontent.com/60494603/129672306-a06123c8-9e21-4df3-a945-03ab5a2672ed.png)

---

## Controller 설계

- 컨트롤러 설계: [컨트롤러 설계 보러가기](https://github.com/Gunyoung-Kim/Info_ABOUT/blob/master/Controller.md)

---

## 화면 설계

- 화면 설계: [화면 설계 보러가기](https://github.com/Gunyoung-Kim/Info_ABOUT/blob/master/View_Design.md)

---

## 개발일지 

- 개발일지: [개발일지 보러가기](https://github.com/Gunyoung-Kim/Info_ABOUT/blob/master/Development_Log.md)

---

## 버전 관리

- 버전 관리: [버전관리 보러가기](https://github.com/Gunyoung-Kim/Info_ABOUT/blob/master/Version_Note.md)

---

Info 2021~
