package org.boris.api1014.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.boris.api1014.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
@Order(1)
public class MemberLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("member loader.................");
        log.info("member loader.................");
        log.info("member loader.................");
        log.info("member loader.................");

    }
}