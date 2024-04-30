package cloud2.shopingmall.order.controller;

import cloud2.shopingmall.common.exception.OrderException;
import cloud2.shopingmall.jwt.CustomUserDetails;
import cloud2.shopingmall.order.dto.*;
import cloud2.shopingmall.order.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderQueryService orderQueryService;
    private final OrderManagementService orderManagementService;
    private final PaymentService paymentService;
    private final DeliveryService deliveryService;
    private final OrderUpdateService orderUpdateService;
    @GetMapping("/user")
    public ResponseEntity<List<OrderInfoDTO>> findOrderByUser (@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<OrderInfoDTO> orderDetailList = orderQueryService.findByUser(customUserDetails.getUsername());
        return ResponseEntity.ok(orderDetailList);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderInfoDTO.OrderDetailInfo> findOrderById(@PathVariable Long orderId){
        OrderInfoDTO.OrderDetailInfo orderDetail = orderQueryService.findByOrderId(orderId);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                @PathVariable Long orderId){

        OrderDTO  orderDTO = orderManagementService.canceledOrder(customUserDetails.getUsername(), orderId);
        return ResponseEntity.ok(orderDTO);
    }
    @GetMapping("/{orderId}/modify")
    public ResponseEntity<String> modifyDelivery(@PathVariable("orderId") Long orderId){
        //상품 주문지 변경

        deliveryService.verifyModify(orderId);
        log.info("검증완료");
        return ResponseEntity.ok("배송지 변경 가능");
    }

    @PostMapping("/{orderId}/modify")
    public ResponseEntity<DeliveryDTO> modifyDelivery(@PathVariable("orderId") Long orderId,
                                                      @RequestBody @Validated DeliveryDTO deliveryDTO){
        DeliveryDTO modifyDelivery = deliveryService.modifyDelivery(deliveryDTO,orderId);
        return ResponseEntity.ok(modifyDelivery);
    }
    //밑에서부터 테스트

    @PostMapping("/create/cart")
    public ResponseEntity<OrderDTO> createOrderByCart(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @RequestBody @Validated OrderCreateRequest.OrderByCart orderCreateRequest,
                                                      BindingResult bindingResult){
        //장바구니 주문
        if(bindingResult.hasErrors()){
            throw new OrderException.DeliveryException();
        }

        OrderDTO orderDTO = orderManagementService.createOrderForCart(customUserDetails.getUsername(),orderCreateRequest.getTotalPrice());
        orderManagementService.createOrderProduct(orderCreateRequest.getOrderProductDTOS(),orderDTO.getId(),customUserDetails.getUsername(),orderCreateRequest.getTotalPrice());
        deliveryService.createDelivery(orderCreateRequest.getDeliveryDTO(),orderDTO.getId(),customUserDetails.getUsername(),orderCreateRequest.getTotalPrice());
        paymentService.createPayment(customUserDetails.getUsername(), orderCreateRequest.getTotalPrice(),orderDTO.getId());

        return ResponseEntity.ok(orderDTO);

    }
    @PostMapping("/create/product")
    public ResponseEntity<OrderDTO> createOrderByProduct(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @RequestBody @Validated OrderCreateRequest.OrderByProduct orderByProduct,
                                                         BindingResult bindingResult){
        //상품 주문
        if(bindingResult.hasErrors()){
            throw new OrderException.DeliveryException();
        }
          OrderDTO orderDTO = orderManagementService.createOrderForProduct(customUserDetails.getUsername(),orderByProduct.getTotalPrice());
          orderManagementService.createOrderProduct(orderByProduct.getOrderProductDTOS(),orderDTO.getId(),customUserDetails.getUsername(),orderByProduct.getTotalPrice());
          deliveryService.createDelivery(orderByProduct.getDeliveryDTO(),orderDTO.getId(),customUserDetails.getUsername(),orderByProduct.getTotalPrice());
          paymentService.createPayment(customUserDetails.getUsername(), orderByProduct.getTotalPrice(),orderDTO.getId());

        return ResponseEntity.ok(orderDTO);

    }

    @GetMapping("/{orderId}/delivery")
    public ResponseEntity<DeliveryDTO> findDelivery(@PathVariable("orderId") Long orderId){
        DeliveryDTO delivery = deliveryService.findDelivery(orderId);
        return ResponseEntity.ok(delivery);
    }

}
