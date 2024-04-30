package cloud2.shopingmall.product.repository;

import cloud2.shopingmall.product.entity.ProductBody;
import cloud2.shopingmall.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProduct_Id(Long id);
    void deleteByProduct_Id(Long id);
}
