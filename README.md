# 🗺️ 대동덕지도 - backend

<p align="center">
  <br>
  <img src="https://github.com/duck-map-project/duck-map-be/assets/100250055/0eb7e837-69f5-4ea5-b96b-d34553d9d00b">
  <br>
</p>


## ​Contents

[1️⃣ 프로젝트 소개](#프로젝트-소개)<br>
[2️⃣ Server](#Server)<br>
[3️⃣ Design](#Design)<br>
[4️⃣ ERD](#ERD)<br>
[5️⃣ Specification](#Specification)<br>
[6️⃣ 핵심 구현 기능](#핵심-구현-기능)<br>
[7️⃣ 팀 소개](#팀-소개)<br>

<br>

## 프로젝트 소개

<p align="justify">
대동덕지도는 덕질 및 응원 이벤트 장소 조회 서비스 웹 사이트입니다. 생일 카페, 전광판 광고 등 다양한 이벤트가 덕질과 응원을 기반으로 활발히 진행되고 있습니다. 그런 이벤트들의 정보를 한 곳에서 덕질과 응원의 대상인 아티스트를 기반으로 편리하게 검색 및 조회할 수 있는 서비스를 만들고 싶어 시작한 프로젝트입니다. 
</p>

<br>

## Server
| Back-end | Front-end |
| :--------: | :--------: |
| [API 서버](https://duckmap.shop:8080/swagger-ui/index.html#) | [배포 서버](https://d14wwtcgrsz6oh.cloudfront.net/)|

<br>

## Design
 <img src="https://img.shields.io/badge/figma-F24E1E?style=flat&logo=figma&logoColor=white"/>

[대동덕지도 디자인](https://www.figma.com/file/LNisCtmO4ope7kllZgEUpq/대동덕지도-디자인-진행?type=design&node-id=0-1&mode=design&t=hAtoFo31fsfAWLna-0)

<br>

## ERD

- [현재 ERD Link (🛠️ 신규 기능 추가중)](https://dbdiagram.io/d/64ba1f6c02bd1c4a5e73fbc5)
- 기존 ERD
<p align="center">

  <br>
<img src="https://github.com/duck-map-project/duck-map-be/assets/100250055/29ca9587-9181-40af-a89a-4d269612d7b9">
  <br>
</p>

<br>

## Specification
- Server
    - Spring Boot 2.7.x
    - Java 17 (Open JDK)
- DataBase
    - MySQL 8.x.x
    - JPA (Spring Data JPA)
    - QueryDSL
    - Redis
- Test
    - Junit5
    - h2 database
- CI/CD
    - Github Actions
    - Docker
- API Docs
    - Swagger
- SCM
    - GitHub
- Authentication
    - JWT
    - Spring Security
- Dev Tool
    - IntelliJ
- Cloud
  - AWS EC2


<br>

## 핵심 구현 기능
### 가입 및 인증
- 로그인 시 Access Token(AT)과 Refresh Token(RT)을 발행, RT는 Redis에 저장
- AT를 AUTHORIZATION 헤더에 RT를 Cookie에 담아 프론트에 전달
- 로그아웃시 Redis에 저장되어 있는 RT 삭제하고 로그아웃 처리한 AT 저장
- 비밀번호 재설정시 회원의 이메일로 24시간 동안 유효한 비밀번호 재설정 링크를 발송
  
### 아티스트로 이벤트 생성 및 조회
- 이벤트의 주최인 아티스트를 기반으로 이벤트를 생성하고 조회
- 이벤트의 상호명, 해시태그, 주소, 카테고리, 이미지 등을 등록 
- 대부분의 이벤트의 특성을 고려하여 홍보글인 트위터 URL 링크를 등록
- 이벤트의 상세 정보에서 원본 트윗을 임베디드, 주소를 통해 카카오맵으로 장소 확인 

### 이벤트의 장소를 기반으로 카카오맵에 표기
- 메인 페이지에서 현재 진행중인 이벤트를 카카오맵에 표기(인기순, 리뷰순으로 상위 20개를 정렬)

### 방문자들의 리뷰 및 별점
- 방문자의 리뷰를 이벤트의 주최인 아티스트를 기반으로 검색 및 조회
- 리뷰에 이미지와 리뷰 내용, 평점(별점)을 등록
  
### 북마크, 좋아요, 북마크 폴더
- 원하는 이벤트를 북마크 및 좋아요 
- 북마크 폴더에 북마크한 이벤트를 모아 볼 수 있음
- 북마크 폴더를 사용자의 취향대로 생성 가능(아이콘, 색상)
- 북마크 폴더를 외부로 공유  

### 마이페이지
- 회원 정보 수정 (프로필, 닉네임) 
- 좋아요 한 이벤트 모아보기
- 북마크 한 이벤트 모아보기
- 작성한 이벤트와 리뷰를 모아보기 및 수정, 삭제
- 비밀번호 변경 
- 회원 탈퇴 
  
### 아티스트 타입, 아티스트, 카테고리 CRUD
- 이벤트의 주최인 아티스트를 아티스트 타입으로 크게 구분
  - 아티스트 타입 예시: 아이돌, 솔로, 배우, 예능인 등
- '그룹' 개념을 정의하여 그룹 내의 멤버를 추가할 수 있도록 설계
- 이벤트 특성에 맞는 카테고리를 정의 (예: 카페, 전시회, 전광판, 럭키드로우 등)
- 아티스트의 경우 이미지 등록이 가능

<br>

## 팀 소개
| <img src="https://github.com/duck-map-project/duck-map-be/assets/100250055/83aa0ce2-69a8-4afd-8fee-9146a0269e55" width="100"><br>[@busymidnight](https://github.com/busymidnight) | [@LeeRim](https://github.com/LeeRim) | [@sabit1997](https://github.com/sabit1997) |  [@vnfdusdl](https://github.com/vnfdusdl)   | <woneee_10@kakao.com> | [@shaykk](https://github.com/shaykk) | 
| :--------: | :--------: | :------: | :-----: |:-----: | :-----: |
| Back-end | Back-end | Front-end  | Front-end  | Design | 기획 단계 참여 |

<br>
