package cloud2.shopingmall.product.repository;

import cloud2.shopingmall.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT distinct p FROM Product p left join fetch p.productDetails where p.id = :id")
    Product findProductWithDetails(@Param("id") Long id);

    @Query("SELECT distinct p FROM Product p left join fetch p.productBodies where p.id = :id")
    Product findProductWithBodies(@Param("id") Long id);

    @Query("SELECT distinct p FROM Product p left join fetch p.productImages where p.id = :id")
    Product findProductWithImages(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT p \s
            FROM Product p
            LEFT JOIN FETCH p.productBodies pb
            LEFT JOIN FETCH p.productDetails pd
            WHERE p.id = :id""")
    Product findProductWithDetailsAndBodies(@Param("id") Long id);

    Product findByName(String name);

    @Query("SELECT distinct p FROM Product p inner join CategoryProduct cp ON cp.product = p inner join cp.category c WHERE c.id = :id")
    List<Product> findProductsWithCategoryId(@Param("id") Long id);

    Page<Product> findProductsByNameContaining(String keyword, Pageable pageable);


    @Query("SELECT distinct p FROM Product p inner join CategoryProduct cp ON cp.product = p WHERE cp.category.id = :categoryId AND p.name LIKE CONCAT('%', :keyword, '%')")
    Page<Product> findProductsByNameContainingByCategoryId(@Param("keyword") String keyword, Pageable pageable, @Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT p FROM Product p WHERE (CAST(:nameOrId AS LONG) IS NULL AND p.name LIKE CONCAT('%', :nameOrId, '%')) OR (CAST(:nameOrId AS LONG) IS NOT NULL AND (p.name LIKE CONCAT('%', :nameOrId, '%') OR p.id = CAST(:nameOrId AS INTEGER)))")
    List<Product> findByNameOrId(@Param("nameOrId") String nameOrId);
    List<Product> findProductsByNameContaining(String keyword);
}
