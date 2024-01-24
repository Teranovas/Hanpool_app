<center><img src="/images/HANPOOLPOSTER.png" width="100%" height="100%"></center>

# 이앱설레 - 동계 프로젝트
 `팀원` : `팀장` 이창세, 이우진, 박준성, 심재원

## 프로젝트 정보

**프로젝트 명:** 한풀

**개발 기간:** 3개월

## 프로젝트 소개

한풀은 한신대학교 졸업프로젝트인 캡스톤 디자인 프로젝트로써, 회원가입, 게시판, 카풀 지도 확인, 거리 매칭 로직, 카풀 채팅방, 프로필 변경 등 다양한 기능을 포함한 안드로이드 카풀 어플리케이션입니다. 12주 동안의 개발 기간 동안 각 주차별로 구현된 기능들이 포함이 되어있습니다.

## 개발툴 / 사용언어

![Java](https://skillicons.dev/icons?i=java)
![Firebase](https://skillicons.dev/icons?i=firebase)
![Android Studio](https://skillicons.dev/icons?i=androidstudio)

- Java: 안드로이드 앱 개발을 위한 주요 프로그래밍 언어로, 객체지향적 특성과 다양한 라이브러리를 활용하여 다양한 모바일 애플리케이션을 구축하는 데 사용합니다.
- Firebase: 구글의 모바일 플랫폼으로, 실시간 데이터베이스, 백엔드 기능, 사용자 인증 등을 제공하여 앱 개발을 간소화합니다.
- Android Studio: 안드로이드 앱 개발을 위한 공식 통합 개발 환경(IDE)으로, Java와 호환되며 다양한 개발 도구와 시뮬레이션 기능을 제공합니다.

## 기능 / 작업

- 회원가입/로그인 화면구현
- 게시판을 통한 카풀신청구현
- 지도상으로 카풀을 신청하는 기능구현
- 본인만의 경로를 탐색하고, 비슷한 경로를 가진 상대방을 매칭해주는 시스템 구현
- 채팅방 시스템 구현
- 드라이버를 등록하고, 프로필사진을 변경하고, 닉네임변경을 하는 기능 구현
- 화면 꾸미기

# 개발 환경

- Android Studio: Android Studio Giraffe | 2022.3.1
- Java: Uses the latest stable version.
- Firebase: BoM version 31.2.3.

## 주차별 작업 내용

### 1주차 ~ 3주차

- Firebase를 이용한 회원가입 기능 및 화면 구현 (Email, Password 받아오기)
  - 회원가입을 할 때, 한신대학교 전용 이메일로만 인증번호를 받도록하는 기능 구현
  - 6자리 난수를 사용하여 인증번호가 입력한 이메일로 오도록 함.
  - 인증번호가 일치하면 회원가입이 성공이되도록하는 기능 구현
  - 회원 사진, 닉네임, 직위 Firebase에 저장
- 로그인 기능 및 화면 구현
  - 앱을 끄고 다시 키면, 로그인이 된 상태로 넘어가도록 함.
- 로그인 및 로그아웃 구현
- 스플래시 화면, 인트로 화면 꾸미기
- 회원가입 화면 디자인

### 4주차 ~ 6주차

- 게시판 기능인 CommunityFragment구현
  - 학교로가는 게시판, 집으로가는 게시판, 택시카풀 등 3가지로 게시판을 나눔.
  - 게시판을 볼때, 거리순 시간순으로 게시물리스트뷰를 나열하도록 변경
- 게시물 등록기능 구현
  - 운전자가 아닌 탑승자는 게시물등록을 할수 없도록 변경
  - Firebase 서버를 Hosting하기
  - KakaoAPI를 이용하여 출발지와 도착지의 도로명주소를 가져오는 기능구현
  - 출발지와 도착지사이를 TmapAPI를 이용하여 경로설정
- 게시물 신청기능 구현
  - TmapAPI를 이용하여 설정된 경로가 게시판에 보이게됨.
  - 게시판을 통해 카풀신청을하면 채팅이 서로 연결되도록 함.
- 채팅방 시스템 기능 구현
  - 채팅방안에, 카풀초대기능 구현

### 7주차 ~ 9주차

- 지도를 검색하는 화면구현
  - 도로명검색을 통해 주소를 설정하면 해당 지점으로 지도가 이동
- 지도상으로 Community의 게시판들의 출발지가 마커표시되도록 설정
  - 해당 마커를 누르면 해당 게시판으로 이동하여 카풀을 신청할수 있게 구현
- 추천카풀 화면 구현
  - 사용자 본인의 경로를 설정하는 기능 구현
  - 유클리디언 거리 알고리즘을 이용하여 비슷한 경로를 가진 사용자를 본인 추천카풀에 띄우도록 설정

### 10주차 ~ 11주차

- 내 정보 화면 구현
- 드라이버등록 기능 구현
  - 차량번호,운전면허증,차량 사진 3장 Firebase Storage에 저장
- 카풀 일정 구현
  - 해당 날짜에 카풀이 있으면 노란색으로 표시
- 프로필 변경 기능 구현
- 카풀내역 및 탑승객 별점 매기기 기능 구현
- 로그아웃 기능 구현

### 12주차 ~ 13주차

- 어플리케이션 테스트

# 회원 가입화면, 로그인화면
<img src="/images/Singup.png" width="40%" height="40%">

# 회원 가입화면(인증번호 화면)
<img src="/images/auth.png" width="40%" height="40%">

# 게시판 리스트, 등록화면
<img src="/images/Community1.png" width="40%" height="40%">&nbsp;&nbsp;&nbsp;
<img src="/images/Community2.png" width="40%" height="40%">

# 경로설정, 추천카풀화면
<img src="/images/Home1.png" width="40%" height="40%">&nbsp;&nbsp;&nbsp;
<img src="/images/Home2.png" width="40%" height="40%">

# 지도화면
<img src="/images/Map.png" width="40%" height="40%">

# 마이페이지화면(드라이버등록)
<img src="/images/MyPage.png" width="40%" height="40%">&nbsp;&nbsp;&nbsp;
<img src="/images/Driver.png" width="40%" height="40%">

# 마이페이지화면(카풀일정, 카풀내역)
<img src="/images/Date.png" width="40%" height="40%">&nbsp;&nbsp;&nbsp;
<img src="/images/history.png" width="40%" height="40%">

# 마무리
저희 팀은 많은 시간 동안 토론을 통해 학교 교통의 불편함을 해소하고자 학교 전용 카풀 앱 개발을 결정했습니다.<br>
Java 언어를 사용하여 프로젝트를 진행했고, 팀원들과의 원활한 의사소통을 통해 유클리디언 거리 알고리즘을 사용해 비슷한 경로를 매칭시켰습니다.<br>
또한, 개발 과정에서 어댑터의 데이터 연결에 어려움을 겪으며, Firebase에 저장된 데이터를 사용자 인터페이스에 표시하는 어댑터의 중요성을 깨달았습니다.<br>

GitHub를 사용해 실시간으로 commit, merge 및 rebase 작업을 진행하며, branch의 필요성과 README.md 파일의 중요성을 이해했습니다. 프로젝트의 각 주요 기능, 예를 들어 회원가입, 로그인, 게시물, 지도, 카풀 채팅, 카풀 일정 등이 유기적으로 연결되어야 한다는 것도 깨달았습니다.</br>
특히, Tmap API를 사용하면서 두 지점 사이의 경로를 쉽게 탐색하고 지도상에 표시하는 방법을 배웠습니다. 이를 통해 외부 API의 유연성과 중요성을 깊이 이해하게 되었습니다.<br>
마지막으로 사용자의 정보를 저장하는 데이터베이스의 중요성도 깨달았습니다. 여러가지 시행착오를 통해 데이터를 체계적으로 관리해야 사용도 편하다는 것을 알게되었습니다. 비록 이번에는 Firebase를 사용하긴 했으나, 다음에는 다른 데이터베이스도 사용해보고싶습니다.

긴 개발 기간 동안 조금씩 성장하는 것을 느끼며 큰 성취감을 얻었습니다. 이번 프로젝트는 앞으로 다른 프로젝트를 개발할 때 중요한 경험이 될 것입니다.<br>