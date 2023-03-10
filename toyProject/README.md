๐ ์ฌ์ฉํ ๊ธฐ์  ์คํ  
<div align=center>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white">
  <img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=Oracle&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
</div>

๐ ๊ธฐ๋ฅ
+ Spring Boot Security ์ JWT ๋ฅผ ์ด์ฉํ ๋ก๊ทธ์ธ ๊ธฐ๋ฅ
+ Spring Data JPA ์ Oracle DB ๋ฅผ ์ด์ฉํ ํ์ CURD ๊ธฐ๋ฅ
+ Mail ๋ผ์ด๋ธ๋ฌ๋ฆฌ์ Redis ๋ฅผ ์ด์ฉํ ์ด๋ฉ์ผ ์ธ์ฆ ๊ธฐ๋ฅ
+ Validation ๋ผ์ด๋ธ๋ฌ๋ฆฌ๋ฅผ ์ด์ฉํ ์ ํจ์ฑ ๊ฒ์ฌ ๊ธฐ๋ฅ
+ REST API ๊ธฐ๋ฅ
+ AOP ๋ฅผ ์ด์ฉํ ์ค๋ณต์ฒดํฌ ๋ก์ง ๊ธฐ๋ฅ
+ Interceptor ๋ฅผ ์ด์ฉํ ๋ก๊ทธ ๊ธฐ๋ฅ
+ Exception ์ค๋ฅ๋ฅผ Response ์ json ํ์์ผ๋ก ์๋ต ๊ธฐ๋ฅ

# Refresh Token
Refresh Token ์ ์ด๋ป๊ฒ ๊ตฌํํ ์ง ๋ง์ ๊ณ ๋ฏผ์ ํ์๋ค.

### Refresh Token ์ ๋ฌด์์ ๋ฃ๊ณ  ์ด๋ป๊ฒ ์ ์ฅํ ์ง
+ JWT TOKEN ์ Headers Authorization ์ Bearer Token ์ผ๋ก ๋ฃ์์ต๋๋ค. (๋ง๋ฃ๊ธฐํ์ 5๋ถ์ผ๋ก ์ ํ๋ค)
+ Refresh Token ์ ์๋ฒ์์๋ Redis ์ ์ ์ฅํ๊ณ  ํด๋ผ์ด์ธํธ์๋ Headers ์ Refresh Token ์ธ๋ฑ์ฑ์ ์ ์ฅํ  ๊ฒ์ด๋ค.
+ Refresh Token ์๋ Client Ip, Client Username, jwt ๋ก ๊ตฌ์ฑ๋์ด ์๋ Access Token ์ ์ ์ฅํ๋ค
+ Redis ์๋ Key : Refresh Token(UUID), Value : Access Token ์ ๋ฃ์ด ์ ์ฅํ  ๊ฒ์ด๋ค.
(์ด๋ 2์ฃผ ์ ๋ ๊ธฐํ๋ง ์ ์ฅํ๋ค)

### ๋์ ๊ณผ์ 
1. JwtFilter ๋ฅผ ์ด์ฉํด Token ์ ๊ฒ์ฌ์ค EXPIRED_JWT_TOKEN Exception ์ด ๋ฐ์ํ๋ค.
2. ํด๋ผ์ด์ธํธ๊ฐ EXPIRED_JWT_TOKEN Exception ์ ๋ฐ์์ ๋ /api/retoken ์ ๋ณด๋ธ๋ค.
3. ์๋ฒ๋ /api/retoken ์ ๋ฐ์ ๋ Redis ์ Access Token ์์น๋ฅผ ์ ์ ์๋ RefreshTokenId ๋ฅผ ๋ฐ๋๋ค.
    + RefreshTokenId ๊ฐ์ ๋ชป๋ฐ์์๋ EMPTY_REFRESH_TOKEN Exception ์ ๋ณด๋ธ๋ค.
4. RefreshTokenId ๊ฐ์ผ๋ก Access Token ์ ์ฐพ์๋ด๊ณ  Access Token ๊ฐ๊ณผ ๋น๊ตํ๋ค.
    + Redis ์์ ๊ฐ์ ๋ชป์ฐพ์ ๋ Refresh Token ๊ธฐํ์ด ๋ง๋ฃ๋์๋ค๊ณ  ๊ฐ์ ํด EXPIRED_REFRESH_TOKEN Exception 
   ์ ๋ณด๋ธ๋ค.
    + Redis ์์ ์ฐพ์๋ธ ๊ฐ๊ณผ Access Token ๊ฐ์ด ๋ค๋ฅผ ๊ฒฝ์ฐ ํด์ปค๊ฐ ๊ณต๊ฒฉํ๋ค๊ณ  ๊ฐ์ ํด Redis ์์ Refresh Token ์ 
   ์ญ์ ํ๋ค. (์ค์ง ๋ค์ ๋ก๊ทธ์ธ์ ํด์ผ์ง๋ง ์ ์ํ  ์ ์๋ค)
    + Redis ์์ ์ฐพ์๋ธ ๊ฐ๊ณผ Access Token ๊ฐ์ด ๊ฐ์ ๊ฒฝ์ฐ ์ฌ์ฉ์์ Access Token ์ ์๋ก ๋ฐ๊ธํ๋ฉฐ 
   Redis ์์ ์ฐพ์๋ธ ๊ฐ์ ์๋ก ๋ฐ๊ธํ Access Token ์ผ๋ก ๊ฐฑ์ ํ๋ค.

## ๐บ ๊ฒฐ๊ณผ
[๋ก๊ทธ์ธ ์์ญ ๊ฒฐ๊ณผ](https://github.com/seungryeolpark/toyProjects/blob/master/toyProject/results/login.md)
