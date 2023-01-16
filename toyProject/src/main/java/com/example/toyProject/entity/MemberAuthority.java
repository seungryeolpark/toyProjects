package com.example.toyProject.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "member_authority")
@NoArgsConstructor
public class MemberAuthority {

    @Id
    @GeneratedValue
    @Column(name = "member_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @Builder
    public MemberAuthority(Member member, Authority authority) {
        this.member = member;
        this.authority = authority;
    }
}
