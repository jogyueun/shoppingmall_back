package cloud2.shopingmall.order.entity;

import cloud2.shopingmall.common.entity.BaseEntity;
import cloud2.shopingmall.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    public enum OrderStatus {
        결제완료(0),
        배송준비(1),
        배송중(2),
        배송완료(3),
        주문취소(4);

        private final int index;

        OrderStatus(int index){
            this.index = index;
        }
        public  int getIndex() {
            return index;
         }

        public static OrderStatus findByIndex(int index){
            for (OrderStatus status : OrderStatus.values()) {
                if (status.index == index) {
                    return status;
                }
            }
            throw new IllegalArgumentException("No constant with index " + index + " found");
        }
    }
    public Orders(Long id,OrderStatus orderStatus){
        this.id = id;
        this.status =orderStatus;
    }
}
