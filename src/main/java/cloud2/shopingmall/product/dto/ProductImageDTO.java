package cloud2.shopingmall.product.dto;

import cloud2.shopingmall.product.entity.ProductBody;
import cloud2.shopingmall.product.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {

    private Long id;
    private Integer sequence;
    private String url;
    private Integer sizeKB;
    private ProductImage.ImageFormat imageFormat;

}
