package cloud2.shopingmall.order.repository;

import cloud2.shopingmall.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Orders,Long> {
    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.payment order by o.createdAt desc ")
    Page<Orders> findAllWithOrderUserAndPayment(Pageable pageable);



    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.orderProducts op WHERE o.id = :orderId")
    Orders findOrdersByOrderId(@Param("orderId") Long orderId);
}
