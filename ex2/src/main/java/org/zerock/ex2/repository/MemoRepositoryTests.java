package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.Optional;
import java.util.stream.IntStream;

//테스트에 앞서 실제로 MemoRepository가 정상적으로 스프링에서 처리되고 의존성 주입에 문제가 없는지 확인
@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        //MemoRepository 인터페이스 타입의 실제 객체가 어떤 것인지 확인
       //스프링이 내부적으로 해당 클래스를 자동으로 생성(AOP기능) : 동적 프록시 방식 jdk.proxy2.$Proxy109
       System.out.println(memoRepository.getClass().getName());
    }

    //등록 작업 테스트 : 한 번에 여러 개의 엔티티 객체 작성
    @Test
    public void testInsertDummies(){
        //100개의 새로운 Memo객체를 생성하고 MemoRepository를 이용해서 insert하는 과정
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    //조회 작업 테스트 : findById()를 이용하여 처리
    // -> java.utill패키지의 Optional타입으로 반환되기에 한번 더 결과가 존재하는지 체크
    @Test
    public void testSelect(){
        // 데이터베이스에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("==============================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    //이전에는 getOne()도 지원햇지만 @Transactional 어노테이션이 추가로 필요
    // -> 리턴 값은 해당 객체지만, 실제 객체가 필요한 순간까지 SQL의 실행을 미룸. : "====" 가 먼저 실행 됨.
    @Transactional
    @Test
    public void testSelect2(){
        // 데이터베이스에 존재하는 mno
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("==============================");

        System.out.println(memo);
    }

    //수정작업 : 등록과 동일하게 save()이용하여 처리
    @Test
    public void testUpdate(){
        //100번의 Memo객체를 만들고 .save()메서드 호출
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        //호출결과 : 내부적으로 select쿼리를 이용해 해당 번호의 Memo객체를 확인하고 update함.
        System.out.println(memoRepository.save(memo));

        //JPA는 엔티티 객체들을 메모리상에 보관하려고 하기에 특정 엔티티객체가 존재하는지 확인하는 select가 먼저 실행되고
        //해당 @Id를 가진 엔티티 객체가 있다면 update, 그렇지 않다면 insert를 실행.

    }

    //삭제작업 : 삭제하려는 번호의 엔티티객체가 있는지 먼저 확인하고 삭제하려고 함
    @Test
    public void testDelete(){
        //데이터베이스에 존재하는 mno
        Long mno = 100L;
        //해당 데이터가 존재하지 않으면 EmptyResultDataAccessException 예외를 발생
        //실행결과 : select 실행한 이후 delete구문이 실행됨.
        memoRepository.deleteById(mno);
    }
}
