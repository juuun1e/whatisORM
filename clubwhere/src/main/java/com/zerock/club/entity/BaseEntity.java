package com.zerock.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass //테이블로 생성되지 않는 클래스
@EntityListeners(value = {AuditingEntityListener.class}) // JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할
@Getter                // ㄴ> 활성화 시키기 위해 @EnableJpaAuditing 설정 추가해야 함
abstract class BaseEntity {

    @CreatedDate //JPA에서 엔티티의 생성 시간 처리
    @Column(name = "regdate", updatable = false) // 해당 엔티티 객체를 데이터베이스에 반영할 때 redDate칼랍값은 변경하지 않음
    private LocalDateTime regDate;

    @LastModifiedDate //최종 수정시간을 자동으로 처리하는 용도
    @Column(name = "moddate")
    private LocalDateTime modDate;

}
