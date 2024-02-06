package com.zerock.club.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Note extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long num;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY) //Note클래스 : ClubMember클래스 = 다 : 1
    private ClubMember writer;

    //수정 관련
    public void changeTitle(String title){
        this.title=title;
    }

    public void changeContent(String content){
        this.content=content;
    }

}
