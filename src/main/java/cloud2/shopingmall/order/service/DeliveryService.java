package cloud2.shopingmall.order.service;

import cloud2.shopingmall.common.exception.OrderException;
import cloud2.shopingmall.order.dto.DeliveryDTO;
import cloud2.shopingmall.order.dto.OrderDTO;
import cloud2.shopingmall.order.entity.Orders;
import cloud2.shopingmall.order.mapper.OrderMainMapper;
import cloud2.shopingmall.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cloud2.shopingmall.order.entity.Delivery;
import cloud2.shopingmall.order.repository.DeliveryRepository;
import org.springframework.transaction.annotation.Transactional;
//import cloud2.shopingmall.order.exception.ResourceNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    /**
     * 배송 시작
     * 배송상태업데이트
     */
    private final DeliveryRepository deliveryRepository;
    private final OrderMainMapper.DeliveryMapper deliveryMapper;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Transactional
    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO,Long orderId,String userName,Integer totalPrice) {
        boolean isPaymentSuccessful = paymentService.verifyPayment(userName, totalPrice);
        if (!isPaymentSuccessful) {
            throw new OrderException.CustomException();
        }
        Delivery delivery = deliveryMapper.toEntity(deliveryDTO);
        delivery.setStatus(Delivery.SenderStatus.배송준비);
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException.OrderNotFoundException(orderId));
        delivery.setOrder(order);
        Delivery save = deliveryRepository.save(delivery);
        order.setDelivery(save);
        orderRepository.save(order);
        return  deliveryMapper.toDto(save);
    }

    @Transactional
    public void verifyModify(Long orderId){
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException.OrderNotFoundException(orderId));
        if(!(order.getStatus() == Orders.OrderStatus.결제완료 || order.getStatus() == Orders.OrderStatus.배송준비)) {
            throw new OrderException.OrderCancellationNotAllowedException(order.getId());
        }
    }
    @Transactional
    public DeliveryDTO modifyDelivery(DeliveryDTO deliveryDTO,Long orderId){
        //배송지 변경
        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        delivery.setAddressDetail(deliveryDTO.getAddressDetail());
        delivery.setPostNumber(deliveryDTO.getPostNumber());
        delivery.setAddress(deliveryDTO.getAddress());
        delivery.setName(deliveryDTO.getName());
        delivery.setPhoneNumber(deliveryDTO.getPhoneNumber());
        Delivery save = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(save);
    }
    @Transactional
    public DeliveryDTO findDelivery(Long orderId){
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new OrderException.OrderNotFoundOrderProductException());
        Delivery delivery = orders.getDelivery();
        return deliveryMapper.toDto(delivery);
    }

 /*public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", "id", deliveryId));
    }*/
    /*public Delivery updateDeliveryStatus(Long deliveryId, Delivery.SenderStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", "id", deliveryId));

        delivery.setSenderStatus(newStatus);
        return deliveryRepository.save(delivery);
    }*/

//    @Transactional
//    public OrderDTO modifyDelivery(DeliveryDTO deliveryDTO){
//        //상품이 결제 완료 혹은 배송 준비일때 만 배송지 및 수령인 변경가능
//
//    }
}



