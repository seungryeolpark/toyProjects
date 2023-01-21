package com.example.toyProject.dto;

import com.example.toyProject.entity.db.Member;
import com.example.toyProject.entity.db.MemberAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    @NotNull
    @Email
    @Size(min = 3, max = 50)
    private String email;

    @JsonIgnore
    private List<MemberAuthority> authorities = new ArrayList<>();

    private String img;

    // no DB
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String confirmPassword;

    @NotNull
    private Long emailCert;

    @Builder
    public MemberDto(String username, String password, String nickname, String email,
                     List<MemberAuthority> authorities, String confirmPassword, Long emailCert) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.authorities = authorities;
        this.confirmPassword = confirmPassword;
        this.emailCert = emailCert;
    }

    public static MemberDto from(Member member) {
        if (member == null) return null;

        return MemberDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .authorities(member.getAuthorities()).build();
    }
}
