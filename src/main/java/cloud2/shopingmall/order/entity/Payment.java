package cloud2.shopingmall.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer payTotalPrice;

    @Column
    private PayStatus payStatus;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payCreatedAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payModifiedAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    public enum PayStatus {
        PENDING_PAYMENT("결제대기"),
        PAYMENT_COMPLETE("결제완료"),
        PAYMENT_CANCELED("결제취소");

        private final String description;

        PayStatus(String description) {
            this.description = description;
        }
    }
}
