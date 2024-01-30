package org.zerock.ex2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;
//JpaRepository 사용하기 : Spring Data JPA는 상속하는 인터페이스를 선언하는 것만으로도 자동으로 스프링의 빈으로 등록된다.
public interface MemoRepository extends JpaRepository<Memo, Long> {
    // JpaRepository를 사용할 때 엔티티의 타입 정보(Memo 클래스 타입)와 @Id의 타입을 지정
}
