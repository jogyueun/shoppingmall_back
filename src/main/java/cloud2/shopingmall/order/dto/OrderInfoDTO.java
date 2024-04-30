package cloud2.shopingmall.order.dto;

import cloud2.shopingmall.order.entity.Orders;
import cloud2.shopingmall.product.entity.Product;
import cloud2.shopingmall.product.entity.ProductDetails;
import cloud2.shopingmall.user.entity.User;
import lombok.*;
import org.hibernate.query.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {

    private Long id;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int statusIndex;
    private Orders.OrderStatus status;
    private Integer totalPrice;
    private OrderDetailInfo Detail;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailInfo extends OrderInfoDTO{

        private List<OrderInfoDTO.ProductInfo> products;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductInfo{
        private Map<String,Integer> productNameAndCount;
        private ProductDetails.Color color;
        private ProductDetails.Size size;
        private Integer price;
    }

}
