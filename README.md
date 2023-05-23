# JJ_Club_backend
JJ Club 백엔드 - 인증/인가 서버(이하얀)

## 기술 스택
- 인증/인가 서버는 JWT 토큰을 기반으로 Spring Security를 이용하여 개발했습니다.

> **🔐 인증인가 관련 서버**
> * `Java 11`
> * `Spring Boot 2.6.8`
> * `Spring Data JPA`
> * `Spring Security`
> * `JWT 0.11.2`
> * `Lombok`
> * `MySQL 10.4.24`
> * `Redis`
>
> #
> 

## 인증/인가 서버 API
- 성공 응답의 경우 201은 커스텀한 응답값입니다.
- 실패 응답의 경우 서버 에러에 해당하는 500 에러를 최소화하기 위해 400 커스텀 에러(자격 증명에 실패하였습니다)로 통일하고, Exception 형태로 어느 곳에서의 실패인지 구분하는 것으로 작성했습니다.

|       서비스        |     Method     |                 URL                   |  시큐리티 인증   |
| ------------------- | -------------- | ------------------------------------- | --------------- |
| 이메일 인증번호 발송 |     POST       | /api/v1/auth/email                    |        X        |
| 이메일 인증         |      POST       | api/v1/auth/verifyCode               |         X        |
| 회원가입            |      POST      | /api/v1/auth/signup                   |         X        |
| 로그인              |      POST      | /api/v1/auth/login                    |         X        |
| 로그아웃            |      GET       | /api/v1/auth/user/logout              |         O        |
| 토큰 재발급         |      POST      |/api/v1/auth/reissue                   |         O        |
| 회원정보 조회       |      GET       | /api/v1/auth/user/me                  |         O        |
| 회원정보 수정       |      PUT       |/api/v1/auth/user/update               |         O        |
| 회원탈퇴            |     DELETE     | /api/v1/auth/user/me2                 |         O        |

---

### 1. 이메일 인증번호 발송
#### 요청

```json
{
    "email": "dlgkdis801@jj.ac.kr" //인증받을 이메일 주소 입력
}
```

#### 응답

- 성공
- HTTP Status : 200 Ok

```json
{
    "result": "success",
    "data": "send mail"
}
```

- 실패 시
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T11:36:10.674+00:00",
    "code": "4001102",
    "error": "org.springframework.http.converter.HttpMessageNotReadableException",
    "message": "자격 증명에 실패하였습니다."
}
```
---

### 2. 이메일 인증
![image](https://github.com/hayannn/JJ_Club_backend/assets/102213509/2fc59655-7b18-4ba8-8057-42eeea22eec4)

#### 요청

```json
{
    "email": "dlgkdis801@jj.ac.kr",
    "code": "254453" //실제 전송받은 코드 입력
}
```

#### 응답

- 성공
- HTTP Status : 200 Ok

```json
{
    "result": "success",
    "data": "true" //DB의 코드와 입력받은 코드 일치 여부
}
```

- 실패
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T11:36:35.273+00:00",
    "code": "4001102",
    "error": "org.springframework.http.converter.HttpMessageNotReadableException",
    "message": "자격 증명에 실패하였습니다."
}
```
---

### 3. 회원가입
#### 요청

```json
{
    "email":"dlgkdis801@jj.ac.kr",
    "password":"1234",
    "userName":"이하얀",
    "nickName" : "티티"
}
```

#### 응답
- 성공
- HTTP Status : 201 Created

```json
{
    "result": "success",
    "data": {
        "email": "dlgkdis801@jj.ac.kr",
        "userName": "이하얀",
        "nickName": "티티",
        "mbti": null
    }
}
```

- 실패
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T11:20:51.083+00:00",
    "code": "4001102",
    "error": "java.lang.RuntimeException",
    "message": "자격 증명에 실패하였습니다."
}
```
---
<br>
<br>

### 4. 로그인
#### 요청

```json
{
    "email":"dlgkdis801@jj.ac.kr", //가입한 이메일
    "password":"1234" //가입한 비밀번호
}
```

#### 응답
- 성공
- HTTP Status : 201 Created
```json
{
    "result": "success",
    "data": {
        "grantType": "bearer",
        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4NDg0MDMzNX0.gpVBL1r1wkXNRq2wOt5Rkw6yBJg9KNPf24R3ZS1qqGwbKGCsLleTSjUulh15heam2eMKPTFG-jxee9KvJXePlA",
        "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODU0NDMzMzV9.hlcdr9s63070tB9BLmk9t3_8_UDLiKvqh2n_JjqgQporL6V7Z1Un3tHG8_vceWfPLTOWVebA1-pM30giI0PvnA",
        "accessTokenExpiresIn": 1684840335244
    }
}
```

- 실패
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T10:47:39.557+00:00",
    "code": "4001102",
    "error": "org.springframework.security.authentication.BadCredentialsException",
    "message": "자격 증명에 실패하였습니다."
}
```
---
<br>
<br>

### 5. 로그아웃
#### 요청

- 요청은, 로그인 이후 발급된 accessToken으로 진행됩니다.
![Untitled](https://user-images.githubusercontent.com/102213509/218940187-f5ca5b30-bcd3-4dac-a5a5-3dce50adf40d.png)

#### 응답

- 성공
- HTTP Status : 201 Created

```json
{
    "result": "success",
    "data": null
}
```

- 실패
- HTTP Status : 500 Internal Server Error(500에러가 발생하기는 하지만, 로직에는 문제가 없는 상태)
```json
{
    "timestamp": "2023-05-23T11:22:23.862+00:00",
    "code": "4001102",
    "error": "org.springframework.security.access.AccessDeniedException",
    "message": "자격 증명에 실패하였습니다."
}
```
---
<br>
<br>

### 6. 토큰 재발급
#### 요청

- 로그인 이후 발급받은 accessToken과 refreshToken을 Body에 넣어 요청, 인증이 되면 재발급이 됩니다.

```json
{
     "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4NDg0MDkyN30.Qud3LNhZoycbmoGQEUzBZlbpo_nnCuV4LtB5OO2wnHPUVjGForiAZ05MFVHjErjsGSFy-iHgXk5YlyOJgCmGbA",
        "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODU0NDM5Mjd9.x7zOmplyTZsbSninVlDPcHJ8NYsoW5bES0dG4brSmzj0PcmIhg_fOG4nHFaVgM5EZmv-SSBriy2UOt4lTOItoA"
}
```

#### 응답

- 성공
- HTTP Status : 201 Created
```json
{
    "result": "success",
    "data": {
        "grantType": "bearer",
        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4NDg0MTAwNH0.lEHzQUjyJ-qi0dpCk-985dEZuo5ZQDB5DusWBgzRs56kijKKN3LQU3Ly4_BVi-KRAQtlaZgACZQ_MVsNQ0kA8g",
        "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODU0NDQwMDR9.GeNYUT0kOklpqiqWtI8DoI9upQ5Kdu6SUjqZWW9dMRtAPPIDnfFYoXF-9xiNNBZjxf09HWEMlb6BbrMBOaWJKA",
        "accessTokenExpiresIn": 1684841004370
    }
}
```

- 실패
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T11:23:23.106+00:00",
    "code": "4001102",
    "error": "java.lang.RuntimeException",
    "message": "자격 증명에 실패하였습니다."
}
```
---
<br>
<br>

### 7. 회원정보 조회
#### 요청

- 요청은, 로그인 이후 발급된 accessToken으로 진행됩니다.
![Untitled](https://user-images.githubusercontent.com/102213509/218940539-2ab1cbf3-364b-4f59-ae05-91b55dcde980.png)

#### 응답

- 성공
- HTTP Status : 201 Created

```json
{
    "result": "success",
    "data": {
        "email": "dlgkdis801@jj.ac.kr",
        "userName": "이하얀",
        "nickName": "티티",
        "mbti": null
    }
}
```

- 실패
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T11:25:07.309+00:00",
    "code": "4001102",
    "error": "org.springframework.security.access.AccessDeniedException",
    "message": "자격 증명에 실패하였습니다."
}
```
---
<br>
<br>

### 8. 회원정보 수정
#### 요청

- 요청은 accessToken과 Body로 이루어집니다.(현재 nickName, password 수정 가능)
![Untitled](https://user-images.githubusercontent.com/102213509/218940769-bf27d8e1-3a81-4786-ac59-b55063f89f47.png)
```json
{
    "nickName": "티이",
    "password":"4321"
}
```

#### 응답

- 성공 : 패스워드 정보는 민감 정보이기 때문에 응답에서 보여주지 않으나, DB에서 변경 여부는 확인 가능합니다.
- HTTP Status : 201 Created
```json
{
    "result": "success",
    "data": {
        "email": "dlgkdis801@jj.ac.kr",
        "userName": "이하얀",
        "nickName": "티이",
        "mbti": null
    }
}
```

- 실패
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T11:28:56.126+00:00",
    "code": "4001102",
    "error": "org.springframework.security.access.AccessDeniedException",
    "message": "자격 증명에 실패하였습니다."
}
```
---
<br>
<br>

### 9. 회원탈퇴
#### 요청

- 이 역시, 로그인 시 발급받은 accessToken으로 진행됩니다.
![Untitled](https://user-images.githubusercontent.com/102213509/218940924-b4dfc92d-08bf-4d31-9c2e-4bd57f3e3ecd.png)

#### 응답

- 성공
- HTTP Status : 201 Created
{
    "result": "success",
    "data": null
}

- 실패
- HTTP Status : 400 Bad Request
```json
{
    "timestamp": "2023-05-23T11:30:10.111+00:00",
    "code": "4001102",
    "error": "org.springframework.security.access.AccessDeniedException",
    "message": "자격 증명에 실패하였습니다."
}
```
