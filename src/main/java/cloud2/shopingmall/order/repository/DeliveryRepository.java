package cloud2.shopingmall.order.repository;

import cloud2.shopingmall.order.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

//    List<Delivery> findById(Delivery delivery);

    Delivery findByOrderId(Long orderId);
}
