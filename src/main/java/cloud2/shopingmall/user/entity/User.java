package cloud2.shopingmall.user.entity;

import cloud2.shopingmall.common.entity.BaseEntity;
import cloud2.shopingmall.order.entity.Orders;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userRole;

    @Enumerated(EnumType.STRING)
    @Column
    private Status Status;

    @Column
    private String loginType;


    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Orders> ordersList;


    public enum Status {
        ACTIVE,
        DEACTIVE,
        DELETED
    }

}
