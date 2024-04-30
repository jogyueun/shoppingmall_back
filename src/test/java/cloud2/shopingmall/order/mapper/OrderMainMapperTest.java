// package cloud2.shopingmall.order.mapper;

// import cloud2.shopingmall.order.dto.OrderInfoDTO;
// import cloud2.shopingmall.order.entity.OrderProduct;
// import cloud2.shopingmall.order.entity.Orders;
// import cloud2.shopingmall.order.entity.Payment;
// import cloud2.shopingmall.user.entity.User;
// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.mapstruct.factory.Mappers;
// import org.springframework.beans.factory.annotation.Autowired;

// import java.util.List;

// import static org.assertj.core.api.Assertions.*;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;


// class OrderMainMapperTest {

//     private OrderMainMapper.OrderDetailMapper orderDetailMapper= Mappers.getMapper(OrderMainMapper.OrderDetailMapper.class);
//     private Orders mockOrder = mock(Orders.class);
//     private User mockUser = mock(User.class);
//     private Payment mockPayment = mock(Payment.class);
//     private OrderProduct mockOrderProduct = mock(OrderProduct.class);
//     @Test
//     void OrderToOrderInfo(){
//         //사용자 결제, 주문 상품 목객체 설정
//         when(mockUser.getUsername()).thenReturn("user123");
//         when(mockPayment.getPayTotalPrice()).thenReturn(10000);
//         when(mockOrderProduct.getName()).thenReturn("product");
//         when(mockOrderProduct.getCount()).thenReturn(2);
//         when(mockOrder.getUser()).thenReturn(mockUser);
//         when(mockOrder.getPayment()).thenReturn(mockPayment);
//         when(mockOrder.getOrderProducts()).thenReturn(List.of(mockOrderProduct));

//         OrderInfoDTO.OrderDetailInfo result = orderDetailMapper.toDto(mockOrder);

//         assertThat(result.getUserName()).isEqualTo("user123");
//         assertThat(result.getTotalPrice()).isEqualTo(10000);
//         assertThat(result.getProducts()).isNotEmpty();
//         assertThat(result.getProducts().get("product")).isEqualTo(2);



//     }

// }