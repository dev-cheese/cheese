# User Domain 작성

## @MappedSuperclass

* 부모 클래스는 테이블과 매핑하지 않고 부모 클래스를 상속받는 자식 클래스에게 매핑 정보만 제공하고 싶으면 `@MappedSuperclass`를 사용하면된다.
* `@MappedSuperclass`를 비유하자면 추상 클래스와 비슷한데 누가 `@Entity`는 실제 테이블과 매핑 되지만 `@MappedSuperclass`는 실제 테이블과 매핑 되지 않는다 이것은 단순히 매핑 정보를 상속할 목적으로만 사용된다.