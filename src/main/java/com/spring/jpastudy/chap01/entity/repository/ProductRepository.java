    package com.spring.jpastudy.chap01.entity.repository;

    import com.spring.jpastudy.chap01.entity.Product;
    import org.springframework.data.jpa.repository.JpaRepository;

    // V 마이바티스의 Mapper 같은 역할
    // V + extends JpaRepository 만 붙여주면 됨.
    // V CRUD가 다들어 있음
    // JpaRepository를 상속한 후 첫번째 제너릭엔 엔터티클래스 타입,
    // 두번째 제너릭엔 PK의 타입 <Product, Long>
    public interface ProductRepository extends JpaRepository<Product, Long> {
        //추상 메서드 알아서 만들어 줌
    }

    // 다음단계는 테스트 진행
