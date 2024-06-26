package com.spring.jpastudy.chap01.entity;

import jdk.jfr.Category;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = "nickName") //닉네임을 제외한다
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

// 테이블을 자동으로 만들어 줌 원리는 엔터티 클래스 먼저 만들면
// 엔터티 클래스를 통해 자동으로 테이블 생성 가능

@Entity
@Table(name = "tbl_product") //실제 테이블명을 써준다.
public class Product {

    @Id //pk설정
    @GeneratedValue(strategy =  GenerationType.IDENTITY) //사용할 수 있는 상수 여러가지 (마리아디비 IDENTITY 오라클 시퀀스)
    @Column(name = "prod_id")
    private Long id; // PK

    @Setter
    @Column(name = "prod_nm", length = 30, nullable = false)
    private String name; // 상풍명

    @Column(name = "price")
    private int price; // 상품 가격

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING) //순차적인의 뜻 => 기본타입 STRING으로 바꿔줘야함.
    private Category category; //상품 카테고리

    @CreationTimestamp // INSERT시에 자동으로 서버시간 저장
    @Column(updatable = false) // 수정불가
    private LocalDateTime createdAt; //상품 등록시간 (자동으로 서버시간 들어감) //캐멀케이스로 쓰면 알아서 스네이크 케이스로 바뀜

    @UpdateTimestamp //UPDATE문 실행시 자동으로 시간이 저장
    private LocalDateTime updatedAt; // 상품 수정시간

    // 데이터베이스에는 저장안하고 클래스 내부에서만 사용할 필드
    @Transient //클래스 내부에서만 사용
    private String nickName;

    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }

    // 컬럼 기본값 설정
    @PrePersist
    public void prePersist() {
        if(this.price == 0) {
            this.price = 10000;
        }

        if(this.category == null) {
            this.category = Category.FOOD;
        }
    }
}


