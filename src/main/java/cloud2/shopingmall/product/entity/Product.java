package cloud2.shopingmall.product.entity;

import cloud2.shopingmall.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private String imageUrl;

    @Column
    private ProductDisplayStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductBody> productBodies = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductDetails> productDetails = new ArrayList<>();

    public enum ProductDisplayStatus {
        ON, OFF
    }

    public Product(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
