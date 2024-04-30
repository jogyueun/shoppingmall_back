package cloud2.shopingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private OrderStatus Status;
    private LocalDateTime CreatedAt;
    private LocalDateTime ModifiedAt;
    private List<OrderProductDTO> products;

    public enum OrderStatus {
        결제완료,
        배송준비,
        배송중,
        배송완료,
        주문취소

    }
}
