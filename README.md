# Showbee
## Capstone3 Project

## DB table

| User | 타입 | 설명 |
| --- | --- | --- |
| id(key) | long | id |
| email | String | 이메일 |
| password | String | 비밀번호 |
| name | String | 닉네임 |

| Shared | 타입 | 설명 |
| --- | --- | --- |
| uId | long | 참가자 |
| id(key) | long |  |
| id(Schedule) | long | 일정 id 값 |

| Schedule | 타입 | 설명 |
| --- | --- | --- |
| sId(key) | long | 고유 id값 |
| price | Integer | 금액 |
| content | String | 내용(사용자 작성) |
| date | Date | 날짜 |
| cycle | Integer | 반복 주기 |
| category | String |  |
| shared | boolean | 공유여부 |
| uId | long | 유저 |

| Financial | 타입 | 설명 |
| --- | --- | --- |
| id(key) | long | 가계부 내역 id값 |
| date | Date | 날짜 |
| price | Integer | 가격 |
| content | String | 내용 |
| category | String |  |


----
## Server
ip address: 117.17.102.143
    
port: 8081
    
주소
117.17.102.143:8081/
    

## User
- Header에 “X-AUTH-TOKEN” value에 로그인 했을 때의 token 값 넣기


### Sign up(회원가입)
- 형식: POST
- Parameter: email, name, password
- 주소: v1/signup
- 117.17.102.143:8081/v1/signup?email={email}&name={name}&password={password}
    
    #### 이메일 중복체크
    - 형식: GET
    - v1/check/{email}
    - 중복이면 true 반환

### Sign in(로그인)
- 형식: POST
- Parameter: email, password
- 주소: v1/signin
- 117.17.102.143:8081/v1/signin?email={}&password={}
- Token 값 반환

### 조회
- 형식: GET
- Parameter: 없음
- 주소: v1/user/get
- user 정보 반환
    #### email로 찾기
    - 형식: GET
    - 주소: v1/user/get/{email}
    - Parameter: email
    - email로 사용자 검색 후 반환

### 삭제
- 형식: DELETE
- Parameter: 없음
- 주소: v1/user/delete
- Header에 토큰 필요
- 현재 로그인된 유저 삭제

### 수정
- 형식: PUT
- Header에 token 필요
- 회원 정보 수정
    #### 닉네임(name) 수정
    - Parameter: name
    - 주소: v1/user/modify/name
    #### 비밀번호(pwd) 수정
    - Parameter: password
    - 주소: v1/user/modify/pwd
    
    
## Schedule
    Date 형식: MM-dd
### post
- 형식: POST
- Parameter: stitle(String), content(String), price(int), date(Date), cycle(int), shared(boolean), participant(List)
- 주소: v1/schedule/post
- Header에 유저 로그인 토큰 필요
- 파라미터 설명 db 테이블 참고
- 참가자는 이메일(중복안되는거)로 넘겨주세요!! 여러명이니까 list가 편할 것 같아서 리스트로 넣었습니다
- shared(boolean)은 참가자 있을 때 true로 넘겨주면 됨
- 아직 예외처리 안함
    
    
### lists
- 형식:GET
- 일정 리스트 반환(모두)
- Parameter 없음
- 주소: v1/schedule/lists
- return: List

### get
- 형식: GET
- Parameter 없음, Header에 로그인 토큰 필요
- 주소: v1/schedule/get
- 로그인한 유저의 일정 조회
- 반환: List

### delete
- 형식: DELETE
- Parameter: fid    
- 주소: v1/schedule/delete/{sid}
- 가계부 고유 키(sid)로 삭제
    
## Financial
    : 가계부
    date 형식: yyyy-MM-dd

### post
- 형식: POST
- Parameter: date, content, price, category
- 주소: v1/financial/post
- Header에 유저 로그인 토큰 필요
- 가계부 등록

### list
- 형식: GET
- Header에 로그인 토큰 필요
- 주소: v1/financial/lists
- 로그인한 유저의 가계부 조회
- 반환: list

### delete
- 형식: DELETE
- Parameter: fid    
- 주소: v1/financial/delete/{fid}
- 가계부 고유 키(fid)로 삭제
