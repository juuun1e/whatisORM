package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

import java.util.List;

//JpaRepository 사용하기 : Spring Data JPA는 상속하는 인터페이스를 선언하는 것만으로도 자동으로 스프링의 빈으로 등록된다.
//JpaRepository를 사용할 때 엔티티의 타입 정보(Memo 클래스 타입)와 @Id의 타입을 지정
public interface MemoRepository extends JpaRepository<Memo, Long> {
    //mno기준으로 between구문 사용, order by적용
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    //쿼리메서드와 Pageable의 결합 : 정렬조건은 Pageable로 조절할 수 있어 메서드가 간단해짐
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    //deleteBy 특정 조건에 맞는 삭제 처리 : 메모의 번호가 10보다 작은 데이터를 삭제한다면
    void deleteMemoByMnoLessThan(Long num);

    @Override
    void flush();
}
