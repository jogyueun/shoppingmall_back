//package cloud2.shopingmall.order.service;
//
//import cloud2.shopingmall.order.entity.Delivery;
//import cloud2.shopingmall.order.entity.Orders;
//import cloud2.shopingmall.order.repository.DeliveryRepository;
//import cloud2.shopingmall.order.repository.OrderRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//class OrderManagementServiceTest {
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private DeliveryService deliveryService;
//
//    @Autowired
//    private DeliveryRepository deliveryRepository;
//
//    @Autowired
//    private OrderManagementService orderManagementService;
//
//
//
//    @Test
//    void updateOrder() {
//        Orders order = new Orders(1L, Orders.OrderStatus.IN_TRANSIT);
//        Delivery delivery = new Delivery(1L, Delivery.SenderStatus.IN_TRANSIT);
//        orderRepository.save(order);
//        delivery.setOrder(order);
//        deliveryRepository.save(delivery);
//        orderManagementService.updateOrder(1L,"배송완료");
//         Orders byId = orderRepository.findById(1L).orElseThrow();
//        Delivery findDelivery = deliveryRepository.findByOrderId(1L);
//        Assertions.assertThat(byId.getStatus()).isEqualTo(Orders.OrderStatus.fromDescription("배송완료"));
//        Assertions.assertThat(findDelivery.getStatus()).isEqualTo(Delivery.SenderStatus.fromDescription("배송완료"));
//    }
//}