package cloud2.shopingmall.product.repository;

import cloud2.shopingmall.product.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails,Long> {
    List<ProductDetails> findByProduct_Id(Long productId);

}
