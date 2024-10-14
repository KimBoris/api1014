package org.boris.api1014.product.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AttachFile {

    private int ord;

    private String filename;
}
