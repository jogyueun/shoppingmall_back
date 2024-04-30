package cloud2.shopingmall.order.service;

import cloud2.shopingmall.order.dto.OrderInfoDTO;
import cloud2.shopingmall.order.entity.OrderProduct;
import cloud2.shopingmall.order.entity.Orders;
import cloud2.shopingmall.order.mapper.OrderMainMapper;
import cloud2.shopingmall.order.repository.OrderRepository;
import cloud2.shopingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderQueryService {
    /**
     * 전체 주문 조회
     * 사용자별 주문 조회
     * 주문 상세 조회
     */
    private final OrderRepository orderRepository;
    private final OrderMainMapper.OrderInfoMapper orderInfoMapper ;
    private final OrderMainMapper.OrderDetailMapper orderDetailMapper;
    private final UserRepository userRepository;
    @Transactional
    public Page<OrderInfoDTO> findAllOrder(Pageable pageable){
        //관리자 입장에서 전체 주문 목록 조회(주문번호, 주문 상태, 주문 날짜, 변경 날짜, 총 금액, 주문자 아이디)
        Page<Orders> allWithOrderUser = orderRepository.findAllWithOrderUserAndPayment(pageable);
        List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>();
        for(Orders order : allWithOrderUser){
            OrderInfoDTO orderInfoDTO = orderInfoMapper.toDto(order);
            orderInfoDTOList.add(orderInfoDTO);
        }
        Page<OrderInfoDTO> orderInfoDTOPage = new PageImpl<>(orderInfoDTOList,
                                                            PageRequest.of(allWithOrderUser.getNumber(),
                                                                            allWithOrderUser.getSize()),
                                                                            allWithOrderUser.getTotalElements());
        return orderInfoDTOPage;
    }
    @Transactional
    public List<OrderInfoDTO> findByUser(String userName){
        //주문자로 주문 목록 검색(주문번호, 주문 상태, 주문 날짜, 변경 날짜, 총 금액, 상품, 수량 , 상품 금액)
        List<Orders> orderDetailsByUsername = userRepository.findByUsername(userName).getOrdersList();
        List<Orders> sortedOrders = orderDetailsByUsername.stream()
                .sorted(Comparator.comparing(Orders::getCreatedAt).reversed())
                .collect(Collectors.toList());
        return sortedOrders.stream()
                .map(orderInfoMapper :: toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public OrderInfoDTO.OrderDetailInfo findByOrderId(Long orderId){
        //주문번호로 검색(주문번호, 주문 상태, 주문 날짜, 변경 날짜, 총 금액, 상품, 수량 , 상품 금액)
        Orders order = orderRepository.findOrdersByOrderId(orderId);
        OrderInfoDTO.OrderDetailInfo orderDetailDto = orderDetailMapper.toDto(order);
        return orderDetailDto;
    }

}
