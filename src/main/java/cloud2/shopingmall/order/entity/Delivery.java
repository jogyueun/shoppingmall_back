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
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private String postNumber;
    @Column
    private String address;
    @Column
    private String addressDetail;

    @Column
    private SenderStatus status;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveredAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders order;


    public enum SenderStatus {
        배송준비(0),
        배송중(1),
        배송완료(2);

        private final int index;

        SenderStatus(int index){
            this.index = index;
        }
        public  int getIndex() {
            return index;
        }
        public static Delivery.SenderStatus findByIndex(int index){
            for (Delivery.SenderStatus status : Delivery.SenderStatus.values()) {
                if (status.index == index) {
                    return status;
                }
            }
            throw new IllegalArgumentException("No constant with index " + index + " found");
        }

    }
    public Delivery(Long id, SenderStatus status){
        this.id = id;
        this.status = status;
    }


}
