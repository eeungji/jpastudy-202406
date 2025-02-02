package com.spring.jpastudy.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(exclude = "employees") //employees 제외
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_dept")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id; // 부서번호

    @Column(name = "dept_name", nullable = false)
    private String name;

    /*
           - 양방향 매핑은 데이터베이스와 달리 객체지향 시스템에서 가능한 방법으로
            1대N관계에서 1쪽에 N데이터를 포함시킬 수 있는 방법이다.

            - 양방향 매핑에서 1쪽은 상대방 엔터티 갱신에 관여 할 수 없고
            (리스트에서 사원을 지운다고 실제 디비에서 사원이 삭제되지는 않는다는 말)
            단순히 읽기전용 (조회전용)으로만 사용하는 것이다.
            - mappedBy에는 상대방 엔터티에 @ManyToOne에 대응되는 필드명을 꼭 적어야 함

        - CascadeType
        * PERSIST : 부모가 갱신되면 자식도 같이 갱신된다
        - 부모의 리스트에 자식을 추가하거나 제거하면 데이터베이스에도 반영된다.
        * REMOVE : 부모가 제거되면 자식도 같이 제거된다.
        - ON DELETE CASCADE
        * ALL : 위의 내용을 전부 포함

     */

    // 부서 : 사원 = 1 : N
    @OneToMany(mappedBy = "department", //상대방은 나를 뭐라고 매핑했니? => employee 에 department로 매핑되어 있다.
                fetch = FetchType.LAZY,//삭제 안되고 업데이트만 된 이유 아래 cascadetype 때문
                cascade = {CascadeType.REMOVE, CascadeType.REMOVE})
    private  List<Employee> employees = new ArrayList<>(); //nullpointexception 방지를 위해 초기화시켜줌. 보통

    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setDepartment(null);
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setDepartment(this); //갱신이 일어날 때 사원쪽에서도(반대편) 세팅
    }


    //@ManyToOne 에서는 기본값이 EAGER (무조건 조인)
    //@OneToMany 에서는 기본값이 LAZE (조인 안함)

    /*
        - 양방향 매핑은 데이터베이스와 달리 객체지향 시스템에서 가능한 방법으로
        1대N관계에서 1쪽에 N데이터를 포함시킬 수 있는 방법이다.

        - 양방향 매핑에서 1쪽은 상대방 엔터티 갱신에 관여 할 수 없고
           (리스트에서 사원을 지운다고 실제 디비에서 사원이 삭제되지는 않는다는 말)
           단순히 읽기전용 (조회전용)으로만 사용하는 것이다.
        - mappedBy에는 상대방 엔터티에 @ManyToOne에 대응되는 필드명을 꼭 적어야 함

        - CascadeType
        * PERSIST : 부모가 갱신되면 자식도 같이 갱신된다
        - 부모의 리스트에 자식을 추가하거나 제거하면 데이터베이스에도 반영된다.
        * REMOVE : 부모가 제거되면 자식도 같이 제거된다.
        - ON DELETE CASCADE
        * ALL : 위의 내용을 전부 포함


    //1대다(employees) / 상대방은 나를 뭐라고 매핑했니(department)로 연결되어 있음.
    //LAZY가 기본값
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Employee> employees = new ArrayList<>();

    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setDepartment(null);
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setDepartment(this);
    }

     */

}
//데이터베이스였으면 사원한테 부서번호를 줌
//