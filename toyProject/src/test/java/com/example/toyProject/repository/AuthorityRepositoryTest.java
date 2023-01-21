package com.example.toyProject.repository;

import com.example.toyProject.entity.db.Authority;
import com.example.toyProject.entity.db.Member;
import com.example.toyProject.entity.db.MemberAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class AuthorityRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @DisplayName("member-authority 연관관계 테스트")
    @Test
    public void memberAuthorityTest() {
        // given
        String username = "member1";
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

        // then
    }

}