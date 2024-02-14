package com.zerock.where.repository;

import com.zerock.where.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember,String> {
    //이메일 기준으로 조회, 일반 로그인 사용자와 소셜로그인 사용자 구분하기 위한 메소드
    //@EntityGraph를 이용하여 'left outer join'으로 ClubMemberRole이 처리될 수 있도록
    @EntityGraph(attributePaths = {"roleSet"}, type=EntityGraph.EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.fromSocial =:social and m.email =:email")
    Optional<ClubMember> findByEmail(String email, boolean social);
}
