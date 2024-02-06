package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

//테스트에 앞서 실제로 MemoRepository가 정상적으로 스프링에서 처리되고 의존성 주입에 문제가 없는지 확인
@SpringBootTest
public class MemoRepositoryTests {

    @Autowired // 컨테이너에서 찾아 주입시킴
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

    //페이징,정렬 처리하기 : JPA는 내부적으로 'Dialect'를 이용하여 페이징,정렬 처리
    //-> SQL이 아닌 API의 객체와 메서드를 사용하는 형태로 페이징 처리를 할 수 있게 됨 : findAll()
    //JpaRepository 인터페이스의 상위인 PagingAndSortRepository의 메서드로, 파라미터로 전달되는 Pageable객체에 의해 쿼리 결정
    //Pageable 인터페이스 : 페이지 처리에 필요한 정보를 전달하는 용도
    //PageRequest클래스의 생성자 : protected로 선언 -> new 대신 static한 of()이용해서 처리
    //JPA를 이용한 페이징처리는 반드시 '0'부터 시작한다는 점을 꼭 기억
    @Test
    public void testPageDefault() {
        //1페이지 데이터 10개
        Pageable pageable = PageRequest.of(0, 10);
        //리턴타입 : Page -> 단순 해당 목록만 가져오는 것이 아니고, 실제 페이지 처리에 필요한 전체 데이터 수를 가져오는 쿼리까지 함께 처리.
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        //Page<엔티티타입>을 주로 사용하는 메서드
        System.out.println("---------------------------");
        System.out.println("Total Pages = " + result.getTotalPages()); // 총 페이지
        System.out.println("Total Count = " + result.getTotalElements()); // 전체 수
        System.out.println("Page Number = " + result.getNumber()); // 현재 페이지 번호 0부터 시작
        System.out.println("Page Size = " + result.getSize()); // 페이지 당 데이터 수
        System.out.println("has next Page? " + result.hasNext()); // 다음 페이지 존재 여부
        System.out.println("first page? " + result.isFirst()); // 시작 페이지(0) 여부

        //실제 페이지의 데이터 처리 : getContent()
        System.out.println("----------------------------");
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    //정렬 조건 추가하기 :PageRequest에는 정렬과 관련된 Sort타입을 파라미터로 전달 가능
    @Test
    public void testSort(){
        //Sort는 한 개혹은 여러 개의 필드 값(mno)을 이용하여 순차적(asc)나 역순(desc) 지정 가능,
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); // and()를 이용해 여러 정렬 조건을 다르게 지정

        //결합된 정렬 조건 사용
        Pageable pageable = PageRequest.of(0,10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        //실행결과 : 역순 정렬, 쿼리문에 order by 절이 추가 됨.
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    //쿼리메서드 기능과 JPQL(Java Persistence Query Language,객체지향쿼리)
    //쿼리메서드 : 메서드 이름 자체가 쿼리 구문으로 처리되는 기능 --> 사용하는 키워드에 따라 파라미터 개수 결정
    //select작업 -> List타입이나 배열 이용가능
    //파라미터에 Pageable타입 넣는 경우 -> 무조건 Page<E>타입
    @Test
    public void tsetQuertMethods(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for(Memo memo:list) {
            System.out.println(memo);
        }
    }

    //쿼리메서드와 Pageable의 결합
    @Test
    public void testQueryMethodWithPageable(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L,pageable);
        result.get().forEach((memo -> System.out.println(memo)));
    }

    //deleteBy 특정 조건에 맞는 삭제 처리 : 메모의 번호가 10보다 작은 데이터를 삭제한다면
    //select문으로 해당 엔티티 객체들을 가져오는 작업 우선 -> 각 엔티티 삭제하는 작업이 함께 실행
    @Commit // 최종결과를 커밋하기 위해 사용 --> deleteBy는 기본적으로 롤백처리 되어 결과가 반영되지 않음
    @Transactional // 없다면 오류 " cannot reliably process 'remove' call
    @Test
    public void testDeleteQueryMethods(){
        memoRepository.deleteMemoByMnoLessThan(10L);
        // 실제 개발에서 많이 사용되지 않는 이유 : SQL 이용하듯 한 번에 삭제가 되는 것이 아니라 각 엔티티 객체를 하나씩 삭제하기 때문에 @Query를 이용해 개선
    }

    //쿼리메서드 > 검색기능 등 편리하지만 조인, 복잡한 조건을 처리하는 경우 And, Or등 사용되면서 불편해 간단한 처리만 이용
    
    //@Query: SQL과 유사하게 엔티티 클래스의 정보를 이용해서 쿼리를 작성하는 기능
    //->필요한 데이터만 선별적 추출 기능, 데이터베이스에 맞는 순수한 SQL을 사용하는 기능, insert update delete와 같은 select가 아닌 DML 등을 처리하는 기능(+@Modifying)을 실행 가능
    //JPQL 객체지향쿼리: 데이터베이스의 테이블 대신 엔티티 클래스와 멤버 변수를 이용해 SQL과 비슷하게 작성
    
    //@Query의 파라미터 바인딩
    //?1, ?2 -> 1부터 시작하는 파라미터 순서 이용
    //:param -> :'파라미터이름' 활용하는 방식, 복잡해 질 경우 :#'객체이름' 사용
    //:#{ } -> 자바빈 스타일
}
