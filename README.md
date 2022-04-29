# Showbee
## Capstone3 Project
## Server

ip address: 117.17.102.143

port: 8081

주소

192.168.0.201:8080/

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


