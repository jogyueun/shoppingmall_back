package cloud2.shopingmall.order.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@Getter
@Setter
public class OrderCreateRequest {
    @Getter
    @Setter
    @Validated
   public static class OrderByCart{
       private DeliveryDTO deliveryDTO;
       private List<OrderProductDTO> orderProductDTOS;
       private Integer totalPrice;
   }
    @Getter
    @Setter
    public static class OrderByProduct{
        private DeliveryDTO deliveryDTO;
        private OrderProductDTO orderProductDTOS;
        private Integer TotalPrice;
    }
}
