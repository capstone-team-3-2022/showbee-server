# Showbee
## Capstone3 Project

| User | 타입 | 설명 |
| --- | --- | --- |
| id(key) | long | id |
| email | String | 이메일 |
| pw | String | 비밀번호 |
| name | String | 닉네임 |

| Shared | 타입 | 설명 |
| --- | --- | --- |
| participant | List | 참가자 |
| id(key) | long |  |
| id(Schedule) | long | 일정 id 값 |

| Schedule | 타입 | 설명 |
| --- | --- | --- |
| sId(key) | long | 고유 id값 |
| price | Integer | 금액 |
| content | String | 내용(사용자 작성) |
| date | Date | 날짜 |
| cycle | Integer? | 반복 주기 |
| participant | List<User> | 참가자(공유) |
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

## Server

ip address: 117.17.102.143

port: 8081

주소
117.17.102.143:8081/

## User
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
- Header에 “X-AUTH-TOKEN” value에 로그인 했을 때의 token 값 넣기
- 주소: v1/user
- 117.17.102.143:8081/v1/user
- user 정보 반환

### 삭제
- 형식: DELETE
- Parameter: id
- 주소: v1/user/{id}
- 117.17.102.143:8081/v1/user/{id}
- 해당 id 유저 삭제

### 수정
- 형식: PUT
- 주소: v1/user
- 정보 수정
- 아직 미완료!!!

## Schedule
## post
- 형식: POST
- Parameter: stitle(String), content(String), price(int), date(Date), cycle(int), shared(boolean)
- 주소: v1/schedule/post
- 파라미터 db 참고
    
## lists
- 형식:GET
- 일정 리스트 반환(모두)
- Param 없음
- 주소: v1/schedule/lists

    

