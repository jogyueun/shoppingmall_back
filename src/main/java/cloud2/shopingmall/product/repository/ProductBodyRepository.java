package cloud2.shopingmall.product.repository;

import cloud2.shopingmall.product.entity.ProductBody;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductBodyRepository extends JpaRepository<ProductBody,Long> {
    List<ProductBody> findByProduct_Id(Long id);
    void deleteByProduct_Id(Long id);
}
