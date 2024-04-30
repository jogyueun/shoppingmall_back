// package cloud2.shopingmall.order.entity;

// import cloud2.shopingmall.ShopingmallApplication;
// import cloud2.shopingmall.order.repository.OrderRepository;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.transaction.annotation.Transactional;
// import java.time.LocalDateTime;
// import java.util.Optional;
// import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// @Transactional
// @SpringBootTest(classes = ShopingmallApplication.class)
// class OrdersTest {
//     @Autowired
//     private OrderRepository orderRepository;

//    @Test
//     void save(){
//        Orders order = new Orders();
//        order.setStatus(Orders.OrderStatus.PAYMENT_COMPLETED);
//        order.setCreatedAt(LocalDateTime.now().minusHours(1));
//        order.setModifiedAt(LocalDateTime.now());
//        orderRepository.save(order);

//        assertThat(orderRepository.findById(order.getId())).isEqualTo(Optional.of(order));
//    }

// }