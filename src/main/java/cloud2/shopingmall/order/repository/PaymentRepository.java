package cloud2.shopingmall.order.repository;

import cloud2.shopingmall.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findById(Payment payment);;
}
