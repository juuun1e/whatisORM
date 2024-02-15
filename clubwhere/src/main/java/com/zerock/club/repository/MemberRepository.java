package com.zerock.club.repository;

import com.zerock.club.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {

    //회원데이터 조회
    @EntityGraph(attributePaths = {"roleSet"}, type=EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.fromSocial =:social and m.memId =:memId")
    Optional<Member> findByMemId(String memId, boolean social);
}
