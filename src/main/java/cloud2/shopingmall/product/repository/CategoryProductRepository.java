package cloud2.shopingmall.product.repository;

import cloud2.shopingmall.product.entity.Category;
import cloud2.shopingmall.product.entity.CategoryProduct;
import cloud2.shopingmall.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct,Long> {

    CategoryProduct findByProduct_Id(Long productId);
}
