package com.zerock.club.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public class Member extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEM_SEQ_GEN")
        @SequenceGenerator(name = "MEM_SEQ_GEN", sequenceName = "MEM_SEQ", allocationSize = 1)
        private Long memNo;

        private String memId;

        private String memPw;

        private String memName;

        private String memNickname;

        private boolean fromSocial;

        @ElementCollection(fetch = FetchType.LAZY) //컬렉션 객체임을 JPA가 알 수 있도록, 1:다 관계
        @Builder.Default //빌더 패턴을 통해 인스턴스를 만들 때 특정 필드를 특정 값으로 초기화하고 싶다면
        private Set<MemberRole> roleSet = new HashSet<>();

        public void addMemberRole(MemberRole memberRole) {
            roleSet.add(memberRole);
        }
}
