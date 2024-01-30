package org.zerock.ex2.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity //반드시 @Entity 추가해야만 해당 클래스의 인스턴스들이 jpa로 관리되는구나~ 인식함
@Table(name="tblMemo")  //생성되는 테이블의 이름 설정
@ToString
@Getter //getter 메소드 생성
@Builder //객체를 생성할 수 있도록 처리
@AllArgsConstructor //@Builder 를 사용하기 위한 처리,@NoArgsConstructor와 항상 함께 사용해야 함
@NoArgsConstructor //@Builder 를 사용하기 위한 처리,@AllArgsConstructor와 항상 함께 사용해야 함
public class Memo {


    @Id //pk에 해당하는 특정 필드에 지정
    @GeneratedValue(strategy = GenerationType.SEQUENCE) //자동으로 생성되는 번호를 사용하기 위해 활용 == pk를 자동으로 생성(키 생성 전략)
    private Long mno;

    //추가적인 필드(컬럼) 필요한 경우, 속성 중 columnDefinition 사용하면 기본값 지정 가능
    //테이블에 칼럼으로 생성되지 않는 필드의 경우 @Transient 어노테이션을 적용
    @Column(length = 200, nullable = false)
    private String memoText;
}
