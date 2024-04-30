package cloud2.shopingmall.order.dto;

import cloud2.shopingmall.product.entity.ProductDetails;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDTO {
    private Long id;
    private String name;
    private Integer price;
    private Integer Count;
    private ProductDetails.Size size;
    private ProductDetails.Color color;
}
