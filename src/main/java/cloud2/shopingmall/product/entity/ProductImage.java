package cloud2.shopingmall.product.entity;

import cloud2.shopingmall.order.entity.Orders;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer sequence;

    @Column
    private String url;


    @Column
    private Integer sizeKB;

    @Column
    private ProductBody.ImageFormat imageFormat;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(String url) {
        this.url = url;
    }

    public ProductImage(Integer sequence, String url){
        this.sequence = sequence;
        this.url = url;
    }

    public enum ImageFormat {
        JPG("jpg"), JPEG("jpeg"), PNG("png"), PDF("pdf");

        private final String format;

        ImageFormat(String format){
            this.format = format;
        }
        public String getFormat() {
            return format;
        }
        public static ImageFormat fromFormat(String format) {
            for (ImageFormat imageFormat : ImageFormat.values()) {
                if (imageFormat.getFormat().equals(format)) {
                    return imageFormat;
                }
            }
            throw new IllegalArgumentException("No constant with imageFormat " + format + " found");
        }

    }
}