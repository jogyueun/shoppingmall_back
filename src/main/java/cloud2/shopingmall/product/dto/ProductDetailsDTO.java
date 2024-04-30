package cloud2.shopingmall.product.dto;

import cloud2.shopingmall.product.entity.ProductDetails;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDTO {

    private Long id;
    private ProductDetails.Size size;
    private ProductDetails.Color color;
    private Integer quantity;
    private ProductDetails.ProductStatus status;


}
