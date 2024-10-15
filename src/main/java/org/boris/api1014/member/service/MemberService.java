package org.boris.api1014.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.boris.api1014.common.exception.CommonExceptions;
import org.boris.api1014.member.domain.MemberEntity;
import org.boris.api1014.member.dto.MemberDTO;
import org.boris.api1014.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


//서비스 계층에서 가장 중요한것 = Transactional
@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;

    //사용자의 정보를 가져온다.
    private final PasswordEncoder passwordEncoder;


    //반환해주는것
    public MemberDTO authenticate(String email, String password) {
        //사용자 이름, 이메일, 패스워드
        Optional<MemberEntity> result = memberRepository.findById(email);


        //"이런아이디 없어" 예외 던진다.
        MemberEntity member = result.orElseThrow(() -> CommonExceptions.READ_ERROR.get());

        //여기까지 내려온건 아이디는 존재한다는 것
        //pw없으면 처리해줘야한다. 원래는 추가적인 예외를 만들어서 써야한다.
        String enPw = member.getPw();

        boolean match = passwordEncoder.matches(password, enPw);

        if (!match) {
            throw CommonExceptions.READ_ERROR.get();
        }

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(email);
        memberDTO.setPw(enPw);
        memberDTO.setRole(member.getRole().toString());

        return memberDTO;

        //사용자 정보가 왔다갔다 하니까 get방식은 위험하다
        //Post방식 사용

    }
}
