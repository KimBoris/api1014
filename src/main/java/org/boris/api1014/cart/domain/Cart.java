package org.boris.api1014.cart.domain;


import jakarta.persistence.*;
import lombok.*;
import org.boris.api1014.member.domain.MemberEntity;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne
    private MemberEntity member;
}
