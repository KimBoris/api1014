package org.boris.api1014.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.boris.api1014.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
@Order(2)
public class ProductLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("product loader.................");
        log.info("product loader.................");log.info("product loader.................");
        log.info("product loader.................");
        log.info("product loader.................");
        log.info("product loader.................");

    }
}
