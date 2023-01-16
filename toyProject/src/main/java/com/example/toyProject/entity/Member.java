package com.example.toyProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @JsonIgnore
    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String passwd;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    private LocalDateTime join;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberAuthority> authorities = new LinkedList<>();

    @Builder
    public Member(Long id, String username, String passwd, String nickname, String email,
                  List<MemberAuthority> authorities, LocalDateTime join) {
        this.id = id;
        this.username = username;
        this.passwd = passwd;
        this.nickname = nickname;
        this.email = email;
        this.authorities = authorities;
        this.join = join;
    }

    public void setAuthority(MemberAuthority... memberAuthorities) {
        List<MemberAuthority> authorities = new LinkedList<>();

        Arrays.stream(memberAuthorities).forEach(s -> authorities.add(s));
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwd='" + passwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
