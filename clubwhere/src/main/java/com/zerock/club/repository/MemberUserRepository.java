package com.zerock.club.repository;

import com.zerock.club.entity.MemberUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberUserRepository extends JpaRepository<MemberUser,String> {

    //회원데이터 조회
    @EntityGraph(attributePaths = {"roleSet"}, type=EntityGraph.EntityGraphType.LOAD)
    @Query("select m from MemberUser m where m.fromSocial =:social and m.memId =:memId")
    Optional<MemberUser> findByMemId(String memId, boolean social);
}
