package cloud2.shopingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Integer payTotalPrice;
    private LocalDateTime payCreatedAt;
    private LocalDateTime payModifiedAt;
    private String payStatus;


    public enum PayStatus {
        PENDING_PAYMENT,
        PAYMENT_COMPLETE,
        PAYMENT_CANCELED,


    }
}
