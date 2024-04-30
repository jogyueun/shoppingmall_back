package cloud2.shopingmall.product.repository;

import cloud2.shopingmall.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findBySuperId(Long superId);
    Optional<Category> findByName(String name);
}
