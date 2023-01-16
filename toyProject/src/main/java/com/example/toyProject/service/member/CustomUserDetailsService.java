package com.example.toyProject.service.member;

import com.example.toyProject.entity.Member;
import com.example.toyProject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByUsername(username)
                .map(member -> createUser(username, member))
                .orElseThrow(() -> new UsernameNotFoundException(username+" : 데이터베이스에서 찾을 수 없습니다."));
    }

    private User createUser(String username, Member member) {
        List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(memberAuthority -> new SimpleGrantedAuthority(memberAuthority.getAuthority().getAuthorityName()))
                .collect(Collectors.toList());

        return new User(member.getUsername(), member.getPasswd(), grantedAuthorities);
    }
}
