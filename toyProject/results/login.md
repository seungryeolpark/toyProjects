## 로그인 영역 결과

+ admin 로그인 성공
![](https://user-images.githubusercontent.com/48073115/217143938-78b58085-8a8f-4486-9082-0b7b6323911a.png)

+ member 로그인 실패
![](https://user-images.githubusercontent.com/48073115/217144039-8e4ebad2-fdef-44a0-b298-6ca06d418441.png)

+ 이메일 인증번호 보내기
![send-cert_email](https://user-images.githubusercontent.com/48073115/217144175-082fa267-64b5-4358-94e7-3de828e8bfe6.png)
![send-cert_email2](https://user-images.githubusercontent.com/48073115/217144217-7de082c4-9d41-4027-a104-7449408467f5.png)

+ 회원가입 인증번호 실패
![signup_cert_fail](https://user-images.githubusercontent.com/48073115/217144595-7b2f6fb3-a92a-4a38-beb1-6604a4e0d279.png)

+ 회원가입 성공
![signup_sucess](https://user-images.githubusercontent.com/48073115/217144779-1d300b52-15e0-43ed-894d-ae53e4fd01a6.png)

+ member 로그인 성공
![member_login](https://user-images.githubusercontent.com/48073115/217145012-76aeb497-6675-4503-b20c-e5423edb0e19.png)

+ jwt 오류
    + 클라이언트가 보낸 jwt 가 형식이 다를경우
      ![jwt_malformed](https://user-images.githubusercontent.com/48073115/217146839-e20a673e-c377-43b5-ab8f-d443f6557a73.png)
    + 클라이언트가 보낸 jwt 가 없을경우
      ![jwt_empty](https://user-images.githubusercontent.com/48073115/217146534-1a5cea8a-5eaa-4769-8a80-0111fde04ae4.png)
    + 클라이언트가 보낸 jwt 가 기한이 지났을 경우
      ![jwt_expired](https://user-images.githubusercontent.com/48073115/217147956-1f1056ff-2ae9-4c75-96f0-8aac3c27c99a.png)

+ Admin 권한에 User 권한 접근 금지
  ![forbidden](https://user-images.githubusercontent.com/48073115/217147312-360c6d94-09c0-4fbc-9a7b-31e18f22580f.png)

+ refreshToken 알고리즘
    + 지금 접속한 ip 와 access token 에 있는 ip 비교
        + 같으면 계속 진행
        + 틀릴경우 json 형식으로 오류 응답
    + refreshToken 인덱스로 redis 에 저장되어 있는 jwt 비교
        + 같으면 계속 진행
        + 틀릴경우 jwt 가 탈취됐다고 판단 redis <refreshToken, jwt> 삭제
          (탈취한 사람도 당한 사람도 다시 그 사용자 권한 이용할려면 로그인해야함)
    + 마지막 단계까지 오면 jwt 기한이 만료됐다고 판단, refreshToken 은 그대로 두고 jwt 만 새로 갱신
      (redis 에서도 jwt 새로 갱신한걸 저장)