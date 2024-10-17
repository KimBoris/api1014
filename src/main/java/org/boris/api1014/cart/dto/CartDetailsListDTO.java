package org.boris.api1014.cart.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CartDetailsListDTO {

    private Long pno;
    private String name;
    private int price;
    private long reviewCnt;
    private String fileName;
    private int qty;

    public CartDetailsListDTO(Long pno, String name, int price, long reviewCnt, String fileName, int qty) {
        this.pno = pno;
        this.name = name;
        this.price = price;
        this.reviewCnt = reviewCnt;
        this.fileName = fileName;
        this.qty = qty;
    }
}
