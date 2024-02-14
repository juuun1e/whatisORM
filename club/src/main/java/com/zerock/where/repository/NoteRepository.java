package com.zerock.where.repository;

import com.zerock.where.entity.Notices;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Notices,Long> {

    @EntityGraph(attributePaths = "writer", type=EntityGraph.EntityGraphType.LOAD) // 작성자에 대한 처리
    @Query("select n from Note n where n.num = :num")
    Optional<Notices> getWithWriter(Long num);

    @EntityGraph(attributePaths = {"writer"}, type=EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.writer.email = :email")
    List<Notices> getList(String email);

}
