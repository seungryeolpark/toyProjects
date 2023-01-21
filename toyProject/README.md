📚 사용한 기술 스택  
<div align=center>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white">
  <img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=Oracle&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
</div>

📖 기능
+ Spring Boot Security 와 JWT 를 이용한 로그인 기능
+ Spring Data JPA 와 Oracle DB 를 이용한 회원 CURD 기능
+ Mail 라이브러리와 Redis 를 이용한 이메일 인증 기능
+ Validation 라이브러리를 이용한 유효성 검사 기능
+ REST API 기능
+ AOP 를 이용한 중복체크 로직 기능
+ Interceptor 를 이용한 로그 기능
+ Exception 오류를 Response 에 json 형식으로 응답 기능

# Refresh Token
Refresh Token 을 어떻게 구현할지 많은 고민을 하였다.

### Refresh Token 에 무엇을 넣고 어떻게 저장할지
+ JWT TOKEN 은 Headers Authorization 에 Bearer Token 으로 넣었습니다. (만료기한을 5분으로 정한다)
+ Refresh Token 을 서버에서는 Redis 에 저장하고 클라이언트에는 Headers 에 Refresh Token 인덱싱을 저장할 것이다.
+ Refresh Token 에는 Client Ip, Client Username, jwt 로 구성되어 있는 Access Token 을 저장한다
+ Redis 에는 Key : Refresh Token(UUID), Value : Access Token 을 넣어 저장할 것이다.
(이때 2주 정도 기한만 저장한다)

### 동작 과정
1. JwtFilter 를 이용해 Token 을 검사중 EXPIRED_JWT_TOKEN Exception 이 발생한다.
2. 클라이언트가 EXPIRED_JWT_TOKEN Exception 을 받았을 때 /api/retoken 을 보낸다.
3. 서버는 /api/retoken 을 받을 때 Redis 에 Access Token 위치를 알 수 있는 RefreshTokenId 를 받는다.
    + RefreshTokenId 값을 못받았을때 EMPTY_REFRESH_TOKEN Exception 을 보낸다.
4. RefreshTokenId 값으로 Access Token 을 찾아내고 Access Token 값과 비교한다.
    + Redis 에서 값을 못찾을 때 Refresh Token 기한이 만료되었다고 가정해 EXPIRED_REFRESH_TOKEN Exception 
   을 보낸다.
    + Redis 에서 찾아낸 값과 Access Token 값이 다를 경우 해커가 공격했다고 가정해 Redis 에서 Refresh Token 을 
   삭제한다. (오직 다시 로그인을 해야지만 접속할 수 있다)
    + Redis 에서 찾아낸 값과 Access Token 값이 같을 경우 사용자의 Access Token 을 새로 발급하며 
   Redis 에서 찾아낸 값을 새로 발급한 Access Token 으로 갱신한다.

