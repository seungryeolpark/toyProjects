package com.example.toyProject.repository;

import com.example.toyProject.entity.Authority;
import com.example.toyProject.entity.Member;
import com.example.toyProject.entity.MemberAuthority;
import com.example.toyProject.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    private final Logger log = Logger.getLogger(MemberRepositoryTest.class.getName());

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @DisplayName("password 인코딩 테스트")
    @Test
    public void passwordTest() throws Exception {
        // given
        String password = "admin";

        // when
        String result = bCryptPasswordEncoder.encode(password);

        // then
        log.info("result : "+result);
    }

    @DisplayName("save 테스트")
    @Test
    public void saveTest() throws Exception {
        // given
        String username = "memberA";
        String password = "1234";

        Member member = Member.builder()
                .username(username)
                .passwd(bCryptPasswordEncoder.encode(password))
                .build();

        // when
        memberRepository.save(member);

        // then
        log.info("[log]member : "+member.toString());
    }

    @DisplayName("findByUsername 테스트")
    @Test
    public void findByUsernameTest() throws Exception {
        // given
        String username = "memberA";
        Member member = Member.builder()
                .username(username).build();

        // when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findByUsername(username);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
    }

    @DisplayName("findOneWithAuthoritiesByUsername 테스트")
    @Test
    public void findOneWithAuthoritiesByUsernameTest() throws Exception {
        // given
        String username = "memberA";
        String authorityName1 = "ROLE_USER";
        String authorityName2 = "ROLE_ADMIN";

        Member member = Member.builder()
                .username(username).build();

        member = memberRepository.save(member);

        Authority authority1 = Authority.builder()
                .authorityName(authorityName1).build();
        Authority authority2 = Authority.builder()
                .authorityName(authorityName2).build();

        authority1 = authorityRepository.save(authority1);
        authority2 = authorityRepository.save(authority2);

        // when
        MemberAuthority memberAuthority1 = MemberAuthority.builder()
                .member(member)
                .authority(authority1)
                .build();
        MemberAuthority memberAuthority2 = MemberAuthority.builder()
                .member(member)
                .authority(authority2)
                .build();

        member.setAuthority(memberAuthority1, memberAuthority2);

        Optional<Member> member2 = memberRepository.findOneWithAuthoritiesByUsername(username);

        // then
        log.info("[log]Authority : "+member2.get()
                .getAuthorities().stream()
                .map(s -> s.getAuthority().getAuthorityName())
                .collect(Collectors.joining(", ")));
    }

    @DisplayName("findByEmail 테스트")
    @Test
    public void findByEmailTest() throws Exception {
        // given
        String email = "test@test.com";

        Member member = Member.builder()
                .email(email).build();

        // when
        memberRepository.save(member);
        Member result = memberRepository.findByEmail(email).orElse(null);

        // then
        log.info("[log]result email : " +result.getEmail());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }
}