package org.boris.api1014.member.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {
//응답하는거기 때문에 Validation이 필요없다.

    private String email;
    private String accessToken;
    private String refreshToken;
}
