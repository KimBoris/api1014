package org.boris.api1014.cart.repository;

import org.boris.api1014.cart.domain.CartDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartDetailRepository extends JpaRepository<CartDetails, Long> {
    //join이 많이 걸릴것이다.
    // 그럼 오브젝트배열

    //email은 where조건에 들어가야하므로 @Param
    //회원 > 장바구니 > 장바구니 상세
    //sql문은 5개 단위로 끊는다. ( 편의상 )
    @Query("""
            SELECT  
            new org.boris.api1014.cart.dto.CartDetailsListDTO(
                p.pno, p.pname, p.price, count(r), attach.filename,
                cd.qty
                )
            FROM 
                    MemberEntity m 
                    left join Cart c ON c.member = m
                    left join CartDetails cd ON cd.cart = c
                    join Product p on p = cd.product
                    left join p.attachFiles attach
                    left join Review r on r.product = p
                where m.email = :email
                and attach.ord = 0
                group by p 
            """)
    Page<Object[]> listOfMember(@Param("email") String email, Pageable pageable);
}
