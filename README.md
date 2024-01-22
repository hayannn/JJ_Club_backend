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
- 테스트는 http://Cap.jjclub.pe.kr:80 으로 진행하실 수 있습니다.
- 예시) 이메일 인증번호 발송 요청을 원하는 경우에 해당 주소로 post해주면 됩니다.
http://Cap.jjclub.pe.kr:80/api/v1/auth/email
<br>
- 성공 응답의 경우 201은 커스텀한 응답값입니다.
- 실패 응답의 경우 서버 에러에 해당하는 500 에러를 최소화하기 위해 400 커스텀 에러(자격 증명에 실패하였습니다)로 통일하고, Exception 형태로 어느 곳에서의 실패인지 구분하는 것으로 작성했습니다.
<br>
<br>
- 테스트 영상


https://github.com/hayannn/JJ_Club_backend/assets/102213509/c7b7fdf2-48c7-43d9-a69c-f93b3711eced



<br>

|       서비스        |     Method     |                 URL                   |  시큐리티 인증   |
| ------------------- | -------------- | ------------------------------------- | --------------- |
| 이메일 인증번호 발송   |     POST       | /api/v1/auth/email                    |        X        |
| 이메일 인증            |      POST       | api/v1/auth/verifyCode               |         X        |
| 이메일 인증번호 재발급 |     POST       | /api/v1/auth/refreshCode               |        X        |
| 회원가입               |      POST      | /api/v1/auth/signup                   |         X        |
| 로그인                 |      POST      | /api/v1/auth/login                    |         X        |
| 로그아웃               |      GET       | /api/v1/auth/user/logout              |         O        |
| 토큰 재발급            |      POST      |/api/v1/auth/reissue                   |         O        |
| 회원정보 조회          |      GET       | /api/v1/auth/user/me                  |         O        |
| 회원정보 수정          |      PUT       |/api/v1/auth/user/update               |         O        |
| 회원탈퇴               |     DELETE     | /api/v1/auth/user/me2                 |         O        |

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

++추가
### 이메일 인증번호 재발급
#### 요청

```json
{
    "email": "dlgkdis801@jj.ac.kr"
}
```

#### 응답

- 성공
- HTTP Status : 200 Ok

```json
{
    "result": "success",
    "data": "reissue Email"
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
    "nickName" : "티티",
    "phoneNumber" : "010-1234-5678"
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
        "userName": null,
        "nickName": "티티",
        "phoneNumber": "010-1234-5678",
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
        "userName": null,
        "nickName": "티이",
        "phoneNumber": "010-5678-1234",
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
    "password":"4321",
    "phoneNumber": "010-5678-1234"
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
        "userName": null,
        "nickName": "티이",
        "phoneNumber": "010-5678-1234",
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
```json
{
    "result": "success",
    "data": null
}
```

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

---

<br>

# 시연 영상(Youtube)
[![Video Label](http://img.youtube.com/vi/fapFPGZMdeI/0.jpg)](https://www.youtube.com/watch?v=fapFPGZMdeI)

---
<br>

# 캡스톤디자인 최종발표 자료
https://docs.google.com/presentation/d/1GXi-4NIpPugG0QNEib30JOojum7RPpk9/edit?usp=sharing&ouid=114851464679137805270&rtpof=true&sd=true

![슬라이드1](https://github.com/hayannn/JJ_Club_backend/assets/102213509/42222a99-286f-4961-87bb-7669b35feb45)
![슬라이드2](https://github.com/hayannn/JJ_Club_backend/assets/102213509/ada4bf63-a15a-4f4b-80ff-8b0860e1c38b)
![슬라이드3](https://github.com/hayannn/JJ_Club_backend/assets/102213509/273db19d-682c-45f8-b47c-644c33ccc492)
![슬라이드4](https://github.com/hayannn/JJ_Club_backend/assets/102213509/e34d40ee-fd10-4d13-9173-8ee23419f8be)
![슬라이드5](https://github.com/hayannn/JJ_Club_backend/assets/102213509/b148cf4d-2cea-4cc9-a1a5-7876b88dbde5)
![슬라이드6](https://github.com/hayannn/JJ_Club_backend/assets/102213509/dc1d67fc-abf6-4232-b309-9d5eb698e446)
![슬라이드7](https://github.com/hayannn/JJ_Club_backend/assets/102213509/de181c4d-856a-4205-9967-7cedd5ebd003)
![슬라이드8](https://github.com/hayannn/JJ_Club_backend/assets/102213509/95cd1663-52df-4564-8e17-60d93f5452fe)
![슬라이드9](https://github.com/hayannn/JJ_Club_backend/assets/102213509/9ecff92b-1790-41f4-ac26-5ac104d33979)
![슬라이드10](https://github.com/hayannn/JJ_Club_backend/assets/102213509/e1b817cd-dbaf-4e3f-96c2-27529e7a7720)
![슬라이드11](https://github.com/hayannn/JJ_Club_backend/assets/102213509/d80b6646-38e6-46fa-b15e-7e8edbf85137)
![슬라이드12](https://github.com/hayannn/JJ_Club_backend/assets/102213509/a9403b44-5bee-4eda-81f5-0c99bb2b307b)
![슬라이드13](https://github.com/hayannn/JJ_Club_backend/assets/102213509/d793e9f0-f63b-42b3-95d7-320c01dc64d0)
![슬라이드14](https://github.com/hayannn/JJ_Club_backend/assets/102213509/b0d80c81-7e2d-45d9-9ade-b1d41df0df6b)
![슬라이드15](https://github.com/hayannn/JJ_Club_backend/assets/102213509/ea6dacb0-1d22-4d8d-ac54-e0639aaa5a33)
![슬라이드16](https://github.com/hayannn/JJ_Club_backend/assets/102213509/424b295e-4f16-4b25-a334-ff037bcb1757)
![슬라이드17](https://github.com/hayannn/JJ_Club_backend/assets/102213509/797e5b05-1ebc-4b23-9750-3518fe3f8e2f)
![슬라이드18](https://github.com/hayannn/JJ_Club_backend/assets/102213509/7e10a00c-f6a5-4663-8584-b65b0c5f82d9)
![슬라이드19](https://github.com/hayannn/JJ_Club_backend/assets/102213509/63edd65a-b7fe-4e1e-9703-492b4267e58c)
![슬라이드20](https://github.com/hayannn/JJ_Club_backend/assets/102213509/7a0c673d-579a-43fc-bb3d-afcd74068463)
![슬라이드21](https://github.com/hayannn/JJ_Club_backend/assets/102213509/aabf99f9-fb90-4014-8797-9b336a51a07e)
![슬라이드22](https://github.com/hayannn/JJ_Club_backend/assets/102213509/55d46bd1-1815-4a65-bb69-e636a2cd866c)
![슬라이드23](https://github.com/hayannn/JJ_Club_backend/assets/102213509/b16c080b-8a83-41a8-9e7e-e82b74133045)
![슬라이드24](https://github.com/hayannn/JJ_Club_backend/assets/102213509/f468174b-9d52-4600-b46d-dfc2b2db49b6)
![슬라이드25](https://github.com/hayannn/JJ_Club_backend/assets/102213509/988e362f-262c-4d59-9bae-66f521740f4a)
![슬라이드26](https://github.com/hayannn/JJ_Club_backend/assets/102213509/89461f55-ecaf-4981-bc9c-628b0dc2f286)
![슬라이드27](https://github.com/hayannn/JJ_Club_backend/assets/102213509/154b5363-dca9-4dc5-bea3-0a8febfdb94d)
![슬라이드28](https://github.com/hayannn/JJ_Club_backend/assets/102213509/2817efc2-fefb-4fec-a37e-65fde962daf9)
![슬라이드29](https://github.com/hayannn/JJ_Club_backend/assets/102213509/9163c25c-9a91-447b-b2ac-fa303dde5771)
![슬라이드30](https://github.com/hayannn/JJ_Club_backend/assets/102213509/e09ffe1e-e4d0-4151-981d-7dabce178b4e)
![슬라이드31](https://github.com/hayannn/JJ_Club_backend/assets/102213509/cbb56aa3-8011-4c6e-a8b1-f74c18dadc65)
![슬라이드32](https://github.com/hayannn/JJ_Club_backend/assets/102213509/25b3fe7c-5c7f-408c-af70-8469a9968473)
![슬라이드33](https://github.com/hayannn/JJ_Club_backend/assets/102213509/7b474e26-a9f1-4736-9d71-f02d848c68a7)
![슬라이드34](https://github.com/hayannn/JJ_Club_backend/assets/102213509/2e90ea55-d70b-4d55-8293-44e21b5d86e3)
![슬라이드35](https://github.com/hayannn/JJ_Club_backend/assets/102213509/3119e867-ad1b-4d63-be9f-05631bb8146e)
![슬라이드36](https://github.com/hayannn/JJ_Club_backend/assets/102213509/05b1b9fe-295e-4fed-939f-1253f549dd5b)
![슬라이드37](https://github.com/hayannn/JJ_Club_backend/assets/102213509/98c62091-7c0a-45cd-9c05-0d8353f950d2)
![슬라이드38](https://github.com/hayannn/JJ_Club_backend/assets/102213509/c5e73311-251a-47c6-b068-c1ebe94f3644)
![슬라이드39](https://github.com/hayannn/JJ_Club_backend/assets/102213509/47d4623e-9353-45d1-830c-b8de67826755)
![슬라이드40](https://github.com/hayannn/JJ_Club_backend/assets/102213509/0c7555cc-fc78-40c1-8c9c-7e3d37083b21)
![슬라이드41](https://github.com/hayannn/JJ_Club_backend/assets/102213509/5d80c348-7671-46ce-8c21-7e37375d6adb)
