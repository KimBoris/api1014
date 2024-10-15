package org.boris.api1014.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    @Min(value =1 , message="over 1")
    private int page = 1;

    @Builder.Default
    @Min(value =10 , message="10보다 크게!!")
    @Max(value=100, message="100보다 작게!!!!")
    private int size = 10;
}