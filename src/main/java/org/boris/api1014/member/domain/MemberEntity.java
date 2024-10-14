package org.boris.api1014.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {""})
public class MemberEntity {

    //이메일을 키값으로 사용
    @Id
    private String email;

    private String pw;

    private MemberRole role;
}