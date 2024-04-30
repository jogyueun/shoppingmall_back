package cloud2.shopingmall.order.service;

import cloud2.shopingmall.common.exception.OrderException;
import cloud2.shopingmall.order.entity.Orders;
import cloud2.shopingmall.order.entity.Payment;
import cloud2.shopingmall.order.dto.PaymentDTO;
import cloud2.shopingmall.order.mapper.OrderMainMapper;
import cloud2.shopingmall.order.repository.OrderRepository;
import cloud2.shopingmall.user.entity.User;
import cloud2.shopingmall.user.entity.UserProfile;
import cloud2.shopingmall.user.repository.UserProfileRepository;
import cloud2.shopingmall.user.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cloud2.shopingmall.order.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PaymentService {
    /**
     * 결제 진행
     * 결제 확인및 검증
     * 결제 취소 및 환불 처리
     */

    private final PaymentRepository paymentRepository;
    private final UserProfileRepository userProfileRepository;
    private final OrderRepository orderRepository;
    private final OrderMainMapper.PaymentMapper paymentMapper;

    @Transactional
    public Boolean verifyPayment(String userName,Integer totalPrice){
        UserProfile userProfile = userProfileRepository.findByUserUsername(userName);
        Integer userPoint = userProfile.getPoint();
        if(userPoint<totalPrice){
            return false;
        }
        return true;
    }
    @Transactional
    public PaymentDTO createPayment(String userName, Integer totalPrice,Long orderId) {
        //유저네임으로 유저 프로파일 리파지토리에서 찾아서
        //유저가 가진 적립금이랑 토탈 금액이랑 비교
        //비교후 결제 완료
        //결제 완료 시 주문과 맵핑
        UserProfile userProfile = userProfileRepository.findByUserUsername(userName);
        Integer userPoint = userProfile.getPoint();
        if(userPoint<totalPrice){
            throw new OrderException.CustomException();
        }
        userProfile.setPoint(userPoint -= totalPrice);
        userProfileRepository.save(userProfile);
        Payment payment = new Payment();
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException.OrderNotFoundException(orderId));
        payment.setOrder(order);
        payment.setPayStatus(Payment.PayStatus.PAYMENT_COMPLETE);
        payment.setPayTotalPrice(totalPrice);
        Payment save = paymentRepository.save(payment);
        order.setPayment(save);
        return paymentMapper.toDto(save);

    }
    @Transactional
    public void refundPoint(String userName,Long orderId){
        UserProfile userProfile = userProfileRepository.findByUserUsername(userName);
        Integer userPoint = userProfile.getPoint();
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException.OrderNotFoundException());
        Integer payTotalPrice = order.getPayment().getPayTotalPrice();
        userProfile.setPoint(userPoint += payTotalPrice);
        userProfileRepository.save(userProfile);
    }

    /*public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPayTotalPrice(payment.getPayTotalPrice());
        paymentDTO.setPayCreatedAt(payment.getPayCreatedAt());
        paymentDTO.setPayModifiedAt(payment.getPayModifiedAt());
        paymentDTO.setPayStatus(String.valueOf(payment.getPayStatus()));

        return paymentDTO;
    }

    public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "Id", paymentId));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPayTotalPrice(payment.getPayTotalPrice());
        paymentDTO.setPayCreatedAt(payment.getCreatedAt());
        paymentDTO.setPayModifiedAt(payment.getModifiedAt());
        paymentDTO.setPayStatus(payment.getPayStatus());

        return paymentDTO;
    }

    public Payment updatePaymentStatus(Long paymentId, Payment.PayStatus newStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        payment.setPayStatus(newStatus);
        return paymentRepository.save(payment);
    }*/
}
