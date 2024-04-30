package cloud2.shopingmall.product.dto;

import cloud2.shopingmall.product.entity.Category;
import cloud2.shopingmall.product.entity.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndCategoryResponseDTO {
    private Product entity;
    private List<Category> categories;
}
