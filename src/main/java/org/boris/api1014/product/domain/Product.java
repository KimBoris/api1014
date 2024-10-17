package org.boris.api1014.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"attachFiles", "tags"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String name;

    private int price;

    @ElementCollection
    Set<AttachFile> attachFiles = new HashSet<>();

    @ElementCollection
    @Builder.Default
    @BatchSize(size = 50)
    private Set<String> tags = new HashSet<>();

    public void adFile(String filename) {
        attachFiles.add(new AttachFile(attachFiles.size(), filename));
    }
    public void clearFiles(){
        attachFiles.clear();
    }
}
