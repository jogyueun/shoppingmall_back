package cloud2.shopingmall.order.service;

import cloud2.shopingmall.common.exception.OrderException;
import cloud2.shopingmall.order.entity.Delivery;
import cloud2.shopingmall.order.entity.Orders;
import cloud2.shopingmall.order.entity.Payment;
import cloud2.shopingmall.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderUpdateService {
    private final OrderRepository orderRepository;
    private final OrderManagementService orderManagementService;

    @Transactional
    public void updateOrder(Long orderId,int status){
        Orders order = orderRepository.findById(orderId).orElseThrow(()-> new OrderException.OrderNotFoundException(orderId));
        Orders.OrderStatus byIndex = Orders.OrderStatus.findByIndex(status);
        if(byIndex == Orders.OrderStatus.배송완료||
                byIndex == Orders.OrderStatus.배송준비 ||
                byIndex == Orders.OrderStatus.배송중) {
            Delivery delivery = order.getDelivery();
            delivery.setStatus(Delivery.SenderStatus.findByIndex(status-1)); // enum 직접 설정
        } else if (byIndex == Orders.OrderStatus.주문취소) {
            Payment payment = order.getPayment();
            payment.setPayStatus(Payment.PayStatus.PAYMENT_CANCELED);
            orderManagementService.canceledOrder(order.getUser().getUsername(),orderId);
        }
        order.setStatus(byIndex);
    }
}
