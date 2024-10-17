package org.boris.api1014.member.controller;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.boris.api1014.member.dto.MemberDTO;
import org.boris.api1014.member.dto.TokenRequestDTO;
import org.boris.api1014.member.dto.TokenResponseDTO;
import org.boris.api1014.member.exception.MemberExceptions;
import org.boris.api1014.member.service.MemberService;
import org.boris.api1014.security.util.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final JWTUtil jWTUtil;

    @Value("${org.boris.accessTime}")
    private int accessTime;

    @Value("${org.boris.refreshTime}")
    private int refreshTime;

    @Value("${org.boris.alwaysNew}")
    private boolean alwaysNew;


    @PostMapping("makeToken")
    public ResponseEntity<TokenResponseDTO> makeToken(@RequestBody @Validated TokenRequestDTO tokenRequestDTO) {

        log.info("Making token");
        log.info("------------------------");

        MemberDTO memberDTO = memberService.authenticate(
                tokenRequestDTO.getEmail(),
                tokenRequestDTO.getPw());

        log.info(memberDTO);
        Map<String, Object> claimMap = Map.of(
                "email", memberDTO.getEmail(),
                "role", memberDTO.getRole());

        String accesToken = jWTUtil.createToken(claimMap, accessTime);
        String refreshToken = jWTUtil.createToken(claimMap, refreshTime);

        TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
        tokenResponseDTO.setAccessToken(accesToken);
        tokenResponseDTO.setRefreshToken(refreshToken);
        tokenResponseDTO.setEmail(memberDTO.getEmail());

        return ResponseEntity.ok(tokenResponseDTO);
    }


    @PostMapping(value = "refreshToken",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestHeader("Authorization") String accessToken,
                                                         String refreshToken) {

        //만약 accessToken이 없다면? 혹은 refreshToken이 없다면?
        if (accessToken == null || refreshToken == null) {
            throw MemberExceptions.TOKEN_NOT_ENOUGH.get();
        }

        //accessToken에서 Bearer (7글자를) 잘라낼때 문제가 발생한다면?
        if (!accessToken.startsWith("Bearer ")) {
            throw MemberExceptions.ACCESSTOKEN_TOO_SHORT.get();
        }
        String accessTokenStr = accessToken.substring("Bearer ".length());

        //Access토큰 만료 여부 체크
        try {
            //여기서 익셉션 발생하면 catch로 빠진다.
            Map<String, Object> payload = jWTUtil.validateToken(accessTokenStr);

            String email = payload.get("email").toString();

            TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
            tokenResponseDTO.setAccessToken(accessTokenStr);
            tokenResponseDTO.setEmail(email);
            tokenResponseDTO.setRefreshToken(refreshToken);

            return ResponseEntity.ok(tokenResponseDTO);
        } catch (ExpiredJwtException ex) {
            //정상적으로 만료된 경우

            //만일 Refresh토큰마저 만료되었다면??
            try {
                Map<String, Object> payload = jWTUtil.validateToken(refreshToken);
                String email = payload.get("email").toString();
                String role = payload.get("role").toString();
                String newAccesToken = null;
                String newRefreshToken = null;

                if(alwaysNew){
                    //access, refresh토큰 새로만든다.
                    //회원정보가 필요함
                    Map<String, Object> claimMap = Map.of("email", email, "role", role);
                    newAccesToken = jWTUtil.createToken(claimMap, accessTime);
                    newRefreshToken = jWTUtil.createToken(claimMap, refreshTime);
                }
                TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
                tokenResponseDTO.setAccessToken(newAccesToken);
                tokenResponseDTO.setRefreshToken(newRefreshToken);
                tokenResponseDTO.setEmail(email);

                return ResponseEntity.ok(tokenResponseDTO);

            } catch (ExpiredJwtException ex2) {
                throw MemberExceptions.REQUIRE_SIGN_IN.get();
            }
        }

    }

}

