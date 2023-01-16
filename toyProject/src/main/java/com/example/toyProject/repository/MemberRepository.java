package com.example.toyProject.repository;

import com.example.toyProject.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByUsername(String username);

    Optional<Member> findByEmail(String email);
}
