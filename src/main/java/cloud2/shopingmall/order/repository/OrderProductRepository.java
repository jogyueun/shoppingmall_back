package cloud2.shopingmall.order.repository;

import cloud2.shopingmall.order.dto.OrderInfoDTO;
import cloud2.shopingmall.order.dto.OrderProductDTO;
import cloud2.shopingmall.order.entity.OrderProduct;
import cloud2.shopingmall.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {
    List<OrderProduct> findByProduct_Id(Long productId);

}
