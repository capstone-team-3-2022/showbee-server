# Showbee
## Capstone3 Project

## DB table
| User | 타입 | 설명 |
| --- | --- | --- |
| id(key) | long | id |
| email | String | 이메일 |
| pw | String | 비밀번호 |
| name | String | 닉네임 |

| Shared | 타입 | 설명 |
| --- | --- | --- |
| uId | long | 참가자 |
| id(key) | long |  |
| sId | long | 일정 id 값 |

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
| inoutcome | boolean | 수입/지출 여부 |

| Financial | 타입 | 설명 |
| --- | --- | --- |
| fid(key) | long | 가계부 내역 id값 |
| date | Date | 날짜 |
| price | Integer | 가격 |
| content | String | 내용 |
| category | String |  |
| bank | String | 은행 |
| memo | String | 메모 (사용자추가) |
| inoutcome | boolean | 수입/지출 여부 |


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
- Query: email, name, password
- 주소: v1/signup
    
    #### 이메일 중복체크
    - 형식: GET
    - PathVar: email
    - v1/check/{email}
    - 중복이면 true 반환

### Sign in(로그인)
- 형식: POST
- Query: email, password
- 주소: v1/signin
- Token 값 반환

### 조회
- 형식: GET
- Parameter: 없음
- 주소: v1/user/get
- user 정보 반환
    #### email로 찾기
    - 형식: GET
    - 주소: v1/user/get/{email}
    - PathVar: email
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
- 반환: boolean(성공 시 true)
    #### 닉네임(name) 수정
    - Query: name
    - 주소: v1/user/modify/name
    #### 비밀번호(pwd) 수정
    - Query: password
    - 주소: v1/user/modify/pwd
    
---
    
## Schedule
    Date 형식: yyyy-MM-dd
    Body로 넘겨주세요
    필수 요소: participant 제외 모두
    
### post
- 형식: POST
- Body: stitle(String), content(String), price(int), date(Date), cycle(int), shared(boolean), participant(List), inoutcome(boolean)
- 주소: v1/schedule/post
- Header에 유저 로그인 토큰 필요
- 파라미터 설명 db 테이블 참고
- 참가자는 이메일로 추가
- shared(boolean)은 참가자 있을 때 true로 넘겨주면 됨 


### get
- 형식: GET
- Parameter 없음, Header에 로그인 토큰 필요
- 주소: v1/schedule/get
- 로그인한 유저의 일정 조회
- 반환: List\<Schedule\>
    
    ### getMonthly
    - 형식: GET
    - Query: nowDate(String) - 형식 ("yyyy-MM")
    - 주소: v1/schedule/getMonthlyTotal
    - 반환: int[] 
        - int[0]: 수입
        - int[1]: 지출
    - 해당하는 달의 고정 수입, 지출 총 금액
    
    ### getMonthly
    - 형식: GET
    - Query: nowDate(String) - 형식 ("yyyy-MM")
    - 주소: v1/schedule/getMonthlyCategory
    - 반환: Map\<String, List\<String\>\>
    - 해당 달의 날짜별 카테고리(아이콘 표시용)
    - 같은 날에 고정 일정이 여러 개면 카테고리도 여러 개 반환됨!
    
    ### getShared
    - 형식: GET
    - Parameter 없음
    - Header에 로그인 토큰 필요
    - 주소: v1/schedule/getShared
    - 반환: List\<Schedule\> 
    - 로그인한 유저의 일정 중 공유된 일정 반환
    
    
### modify
- 형식: PUT
- Body: post와 동일 + "sid": {변경할 일정의 sid}
- 주소: v1/schedule/modify
- Header에 로그인 토큰 필요
- 변경하지 않은 것도 그대로 받아와서 등록과 똑같은 Body 요소로 넘겨주세요


### delete
- 형식: DELETE
- PathVar: fid    
- 주소: v1/schedule/delete/{sid}
- 가계부 고유 키(sid)로 삭제
- 현재 공유된 일정은 삭제 안 됨 
    
---
## Financial
    : 가계부
    date 형식: yyyy-MM-dd
    Body로 넘겨주세요
    필수 요소: price, date, content, inoutcome


### post
- 형식: POST
- Body: date, content, price, category, bank, memo, inoutcome
- 주소: v1/financial/post
- Header에 유저 로그인 토큰 필요
- 가계부 등록
- 반환: Long(등록된 일정의 sid값)

### list
- 형식: GET
- Header에 로그인 토큰 필요
- 주소: v1/financial/lists
- 로그인한 유저의 가계부 조회
- 반환: List\<Financial\>

### get
- 형식: GET
- Query: fid
- 주소: v1/financial/get
- 반환: Optional\<Financial\>
- 해당 가계부 내용 반환
    
### getMonthly
- 형식: GET
- 주소: v1/financial/getMonthly
- Query: String nowDate("yyyy-MM")
- nowDate에 해당하는 달의 가계부 내역(날짜, 수입, 지출)
- 반환: Map<Date, int[]>
- int[][0]: 수입 \/ int[][1]: 지출
- Header에 유저 로그인 토큰 필요
    
### getMonthlyTotal
- 형식: GET
- 주소: v1/financial/getMonthlyTotal
- Query: String nowDate("yyyy-MM")
- nowDate에 해당하는 달의 총 수입, 지출
- 반환: int[]
    - int[][0]: 총수입
    - int[][1]: 총지출
- Header에 유저 로그인 토큰 필요

### getlist
- 형식: GET
- 주소: v1/financial/getlist
- Query: String nowDate("yyyy-MM")
- nowDate에 해당하는 달의 가계부
- 반환: Map<String, List<>>
- 로그인 토큰 필요

    
### modify
- 형식: PUT
- Body: date, content, price, category, **fid**, bank, memo, inoutcome
- Header에 로그인 토큰 필요
- 주소: v1/financial/modify
- 기존 내용은 유지한 채로 넘겨주기
- fid는 해당 가계부의 id 값

### delete
- 형식: DELETE
- PathVar: fid    
- 주소: v1/financial/delete/{fid}
- 가계부 고유 키(fid)로 삭제
