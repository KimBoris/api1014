package org.boris.api1014.product.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.boris.api1014.common.dto.PageRequestDTO;
import org.boris.api1014.common.dto.PageResponseDTO;
import org.boris.api1014.common.exception.CommonExceptions;
import org.boris.api1014.product.dto.ProductListDTO;
import org.boris.api1014.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {

        if(pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        return productRepository.listByCno(1L, pageRequestDTO);
    }

}