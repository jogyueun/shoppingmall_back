package cloud2.shopingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;



    public ProductDTO(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductWithDetailsAndImagesDTO {

        private Long id;
        private String name;
        private Integer price;
        private String imageUrl;
        private List<ProductImageDTO> productImageDTOList;
        private List<ProductBodyDTO> productBodyDTOList;
        private List<ProductDetailsDTO> productDetailsDTOList;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductWithCategoryDTO {

        private Long id;
        private String name;
        private Integer price;
        private String imageUrl;
        private CategoryDTO categoryDTO;

    }


}
