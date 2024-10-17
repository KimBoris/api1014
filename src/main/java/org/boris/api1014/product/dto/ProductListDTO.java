package org.boris.api1014.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {

    private Long pno;
    private String name;
    private int price;
    private long reviewCnt;
    private String fileName;


    //해시태그 리스트로 출력
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
