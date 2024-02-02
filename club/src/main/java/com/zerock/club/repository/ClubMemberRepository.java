package com.zerock.club.repository;

import com.zerock.club.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember,String> {
}
