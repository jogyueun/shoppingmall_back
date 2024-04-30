// package cloud2.shopingmall.order.service;

// import cloud2.shopingmall.order.dto.OrderInfoDTO;
// import cloud2.shopingmall.order.entity.Orders;
// import cloud2.shopingmall.order.entity.Payment;
// import cloud2.shopingmall.order.mapper.OrderMainMapper;
// import cloud2.shopingmall.order.repository.OrderRepository;
// import cloud2.shopingmall.user.entity.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;

// import java.util.Arrays;
// import java.util.List;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.Mockito.*;
// @ExtendWith(MockitoExtension.class)
// class OrderQueryTest {
//     @Mock
//     private OrderRepository orderRepository;
//     @Mock
//     private OrderMainMapper.OrderInfoMapper orderInfoMapper;
//     @Mock
//     private OrderMainMapper.OrderDetailMapper orderDetailMapper;
//     private OrderQueryService orderQueryservice;
//     private Orders mockOrder;
//     private User mockUser;
//     private Payment mockPayment;
//     private OrderInfoDTO mockOrderInfoDTO;
//     @BeforeEach
//     void setUp() {
//         orderQueryservice = new OrderQueryService(orderRepository, orderInfoMapper, orderDetailMapper);
//         mockOrder = mock(Orders.class);
//         mockUser= mock(User.class);
//         mockPayment = mock(Payment.class);
//     }

//     @Test
//     void findAllOrder() {
//         mockOrderInfoDTO = Mockito.mock(OrderInfoDTO.class);
//         when(orderInfoMapper.toDto(any(Orders.class))).thenReturn(mockOrderInfoDTO);
//         List<Orders> ordersList = Arrays.asList(mockOrder);
//         Pageable pageable = PageRequest.of(0, 10);
//         Page<Orders> ordersPage = new PageImpl<>(ordersList, pageable, ordersList.size());
//         when(orderRepository.findAllWithOrderUserAndPayment(pageable)).thenReturn(ordersPage);
//         when(mockOrderInfoDTO.getUserName()).thenReturn("user123");
//         when(mockOrderInfoDTO.getTotalPrice()).thenReturn(10000);

//         // When
//         Page<OrderInfoDTO> result = orderQueryservice.findAllOrder(pageable);

//         // Then
//         assertThat(result).isNotEmpty();
//         assertThat(result.getContent().get(0)).isSameAs(mockOrderInfoDTO);
//         assertThat(result.getContent().get(0).getUserName()).isEqualTo("user123");
//         assertThat(result.getContent().get(0).getTotalPrice()).isEqualTo(10000);

//         // Verify
//         verify(orderRepository).findAllWithOrderUserAndPayment(pageable);
//         verify(orderInfoMapper).toDto(any(Orders.class));
//     }

//     @Test
//     void findByUser() {
//         // Given
//         Orders mockOrder = Mockito.mock(Orders.class);
//         User mockUser = Mockito.mock(User.class);
//         Payment mockPayment = Mockito.mock(Payment.class);
//         OrderInfoDTO.OrderDetailInfo mockOrderDetailInfo = new OrderInfoDTO.OrderDetailInfo();
//         mockOrderDetailInfo.setUserName("user123");
//         mockOrderDetailInfo.setTotalPrice(1000);
//         when(orderDetailMapper.toDto(any(Orders.class))).thenReturn(mockOrderDetailInfo);
//         when(orderRepository.findOrdersByUsername("user123")).thenReturn(Arrays.asList(mockOrder));


//         List<OrderInfoDTO.OrderDetailInfo> result = orderQueryservice.findByUser("user123");

//         assertThat(result).isNotEmpty();
//         assertThat(result.get(0).getUserName()).isEqualTo("user123");
//         assertThat(result.get(0).getTotalPrice()).isEqualTo(1000);

//         // Verify
//         verify(orderRepository).findOrdersByUsername("user123");
//         verify(orderDetailMapper).toDto(any(Orders.class));

//     }

// //    @Test
// //    void findByOrderId() { // Given
// //        Orders mockOrder = Mockito.mock(Orders.class);
// //        User mockUser = Mockito.mock(User.class);
// //        Payment mockPayment = Mockito.mock(Payment.class);
// //        OrderProduct mockorderProduct = Mockito.mock(OrderProduct.class);
// //
// //        when(mockOrder.getId()).thenReturn(1L);
// //        when(mockOrder.getStatus()).thenReturn(Orders.OrderStatus.PAYMENT_COMPLETED);
// //        when(mockOrder.getCreatedAt()).thenReturn(LocalDateTime.now());
// //        when(mockOrder.getModifiedAt()).thenReturn(LocalDateTime.now());
// //        when(mockOrder.getUser()).thenReturn(mockUser);
// //        when(mockOrder.getPayment()).thenReturn(mockPayment);
// //        when(mockOrder.getOrderProducts()).thenReturn(Arrays.asList(mockorderProduct));
// //        when(mockUser.getUsername()).thenReturn("user123");
// //        when(mockPayment.getPayTotalPrice()).thenReturn(1000);
// //
// //        when(orderRepository.findOrdersByOrderId(1L)).thenReturn(mockOrder);
// //
// //        // When
// //        OrderInfoDTO.OrderDetailInfo result = orderQueryservice.findByOrderId(1L);
// //
// //        // Then
// //        assertThat(result).isNotNull();
// //        assertThat(result.getTotalPrice()).isEqualTo(1000);
// //    }
// }