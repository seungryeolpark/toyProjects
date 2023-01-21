package com.example.toyProject.entity.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "authority")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;

    @JsonIgnore
    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL)
    private Set<MemberAuthority> roles = new HashSet<>();

    @Builder
    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
