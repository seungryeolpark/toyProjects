package com.example.toyProject.service.member;

import com.example.toyProject.dto.CertTokenDto;
import com.example.toyProject.dto.MemberDto;
import com.example.toyProject.entity.Authority;
import com.example.toyProject.entity.Member;
import com.example.toyProject.entity.MemberAuthority;
import com.example.toyProject.exception.*;
import com.example.toyProject.repository.AuthorityRepository;
import com.example.toyProject.repository.CertTokenRepository;
import com.example.toyProject.repository.MemberRepository;
import com.example.toyProject.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final CertTokenRepository certTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ROLE_USER = "ROLE_USER";

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

    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities(String username) {
        return MemberDto.from(memberRepository
                .findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException("유저를 찾지 못했습니다."))
        );
    }

    private void checkDuplicationAndNotEqual(MemberDto memberDto) {
        duplicationUsername(memberDto.getUsername());
        duplicationEmail(memberDto.getEmail());
        isEqualPassword(memberDto.getPassword(), memberDto.getConfirmPassword());
        isEqualCertToken(memberDto.getEmail(), memberDto.getEmailCert());
    }

    private void duplicationUsername(String username) {
        memberRepository.findOneWithAuthoritiesByUsername(username).ifPresent(s -> {
                    throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
                });
    }

    private void duplicationEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(s -> {
            throw new DuplicateEmailException("이미 가입되어 있는 이메일입니다.");
        });
    }

    private void isEqualPassword(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new NotEqualPassword("비밀번호가 다릅니다.");
        }
    }

    private void isEqualCertToken(String email, Long emailCert) {
        CertTokenDto certTokenDto = certTokenRepository.findById(email).orElse(null);

        if (certTokenDto == null || !Objects.equals(certTokenDto.getCertValue(), emailCert)) {
            throw new NotEqualCertTokenException("인증번호가 틀립니다.");
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
