package com.example.toyProject.service.member;

import com.example.toyProject.annotation.DuplicationEmailCheck;
import com.example.toyProject.dto.ErrorResponseDto;
import com.example.toyProject.dto.LoginDto;
import com.example.toyProject.dto.TokenDto;
import com.example.toyProject.entity.redis.refreshToken.AccessToken;
import com.example.toyProject.entity.redis.CertToken;
import com.example.toyProject.dto.MemberDto;
import com.example.toyProject.dto.enums.ErrorCode;
import com.example.toyProject.entity.db.Authority;
import com.example.toyProject.entity.db.Member;
import com.example.toyProject.entity.db.MemberAuthority;
import com.example.toyProject.entity.redis.refreshToken.RefreshToken;
import com.example.toyProject.exception.duplication.DuplicateMemberException;
import com.example.toyProject.exception.jwt.EmptyRefreshTokenException;
import com.example.toyProject.exception.jwt.ExpiredRefreshTokenException;
import com.example.toyProject.exception.notEqual.NotEqualCertTokenException;
import com.example.toyProject.exception.notEqual.NotEqualClientIpException;
import com.example.toyProject.exception.notEqual.NotEqualPasswordException;
import com.example.toyProject.exception.notEqual.NotFoundMemberException;
import com.example.toyProject.filter.JwtFilter;
import com.example.toyProject.jwt.TokenProvider;
import com.example.toyProject.repository.AuthorityRepository;
import com.example.toyProject.repository.CertTokenRepository;
import com.example.toyProject.repository.MemberRepository;
import com.example.toyProject.repository.RefreshTokenRepository;
import com.example.toyProject.service.redis.RedisService;
import com.example.toyProject.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    private final RedisService redisService;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final String ROLE_USER = "ROLE_USER";

    @DuplicationEmailCheck
    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        checkDuplicationAndNotEqual(memberDto);

        Authority authority = findAuthority(ROLE_USER);
        Member member = saveMember(memberDto);

        MemberAuthority memberAuthority = MemberAuthority.builder()
                .member(member)
                .authority(authority).build();

        member.setAuthority(memberAuthority);

        return MemberDto.from(member);
    }

    @Transactional
    public ResponseEntity login(
            LoginDto loginDto,
            HttpServletRequest request) {
        Authentication authentication = getAuthentication(loginDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        String uuid = UUID.randomUUID().toString();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);
        httpHeaders.add(JwtFilter.REFRESH_TOKEN_HEADER, uuid);

        AccessToken accessToken = AccessToken.builder()
                        .ip(getRemoteAddr(request))
                        .username(loginDto.getUsername())
                        .jwt(jwt).build();
        redisService.refreshTokenSave(uuid, accessToken);

        return new ResponseEntity<>(TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(uuid.toString()).build(), httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity checkRefreshToken(
            String refreshTokenId,
            HttpServletRequest request) {
        if (refreshTokenId.isEmpty())
            throw new EmptyRefreshTokenException("Refresh Token ??? ????????????", ErrorCode.EMPTY_REFRESH_TOKEN);

        RefreshToken refreshToken = redisService.refreshTokenFindById(refreshTokenId);

        String clientIp = getRemoteAddr(request);
        String username = refreshToken.getAccessToken().getUsername();
        String jwt = SecurityUtil.resolveToken(request);

        isEqualError(refreshToken.getAccessToken().getIp(), clientIp, "client IP??? ?????? ????????????", "IP");
        ResponseEntity response = isEqualError(refreshToken.getAccessToken().getJwt(), jwt, "", "JT");

        // jwt ????????? ?????? ??????
        if (response != null) {
            redisService.refreshTokenDeleteById(refreshTokenId);
            return response;
        }

        // ?????? ?????? ?????? jwt ?????? ?????? ??? Refresh Token ??? Access Token ??????
        String authorities = getAuthorities(username);
        String newJwt = tokenProvider.createToken(username, authorities);
        AccessToken accessToken = AccessToken.builder()
                        .ip(clientIp)
                        .username(username)
                        .jwt(newJwt).build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer "+newJwt);

        redisService.refreshTokenDeleteById(refreshTokenId);
        redisService.refreshTokenSave(refreshTokenId, accessToken);
        return new ResponseEntity<>(TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenId).build(), httpHeaders, HttpStatus.OK);
    }

    public MemberDto getUserWithAuthorities(String username) {
        return MemberDto.from(memberRepository
                .findOneWithAuthoritiesByUsername(username).orElseThrow(
                        () -> new NotFoundMemberException(
                                "????????? ?????? ???????????????.",
                                ErrorCode.NOT_FOUND_MEMBER))
        );
    }

    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException(
                        "????????? ?????? ???????????????.",
                        ErrorCode.NOT_FOUND_MEMBER))
        );
    }

    // private ?????????

    private String getAuthorities(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException("????????? ?????? ???????????????", ErrorCode.NOT_FOUND_MEMBER))
                .getAuthorities().stream()
                .map(m -> m.getAuthority().getAuthorityName())
                .collect(Collectors.joining(","));
    }

    private ResponseEntity isEqualError(
            Object o1, Object o2, String errorMessage, String errorType) {
        switch (errorType) {
            case "IP" :
                isNotEqualIP((String) o1, (String) o2, errorMessage);
                break;
            case "JT" :
                isNotEqualJWT((String) o1, (String) o2, errorMessage);
                break;
        }

        return null;
    }

    private ResponseEntity isNotEqualJWT(String s1, String s2, String errorMessage) {
        if (Objects.equals(s1, s2)) return null;
        ErrorCode errorCode = ErrorCode.NOT_EQUAL_JWT_TOKEN;
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        return new ResponseEntity<>(responseDto,
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }
    private void isNotEqualIP(String s1, String s2, String errorMessage) {
        if (!Objects.equals(s1, s2)) throw new NotEqualClientIpException(errorMessage, ErrorCode.NOT_EQUAL_CLIENT_IP);
    }
    private String getRemoteAddr(HttpServletRequest request) {
        return (request.getHeader("X-FORWARDED-FOR") != null) ?
                request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr();
    }
    private Authentication getAuthentication(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    private void checkDuplicationAndNotEqual(MemberDto memberDto) {
        duplicationUsername(memberDto.getUsername());
        isEqualPassword(memberDto.getPassword(), memberDto.getConfirmPassword());
        isEqualCertToken(memberDto.getEmail(), memberDto.getEmailCert());
    }

    private void duplicationUsername(String username) {
        memberRepository.findOneWithAuthoritiesByUsername(username).ifPresent(s -> {
                    throw new DuplicateMemberException(
                            "?????? ???????????? ?????? ???????????????.",
                            ErrorCode.DUPLICATION_MEMBER);
                });
    }

    private void isEqualPassword(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new NotEqualPasswordException("??????????????? ????????????.", ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }

    private void isEqualCertToken(String email, Long emailCert) {
        CertToken certToken = redisService.certTokenFindByEmail(email);
        if (!Objects.equals(certToken.getCertValue(), emailCert)) {
            throw new NotEqualCertTokenException("??????????????? ????????????.", ErrorCode.NOT_EQUAL_CERT_TOKEN);
        }
    }

    private Authority findAuthority(String authorityName) {
        Authority authority = authorityRepository.findById(authorityName).orElse(null);

        if (authority == null) {
            Authority temp = Authority.builder()
                    .authorityName(authorityName).build();
            return authorityRepository.save(temp);
        }

        return authority;
    }

    private Member saveMember(MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .passwd(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .join(LocalDateTime.now()).build();

        return memberRepository.save(member);
    }
}
