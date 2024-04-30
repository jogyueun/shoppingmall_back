package cloud2.shopingmall.user.entity;

import cloud2.shopingmall.common.entity.BaseEntity;
import cloud2.shopingmall.user.dto.UserProfileDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private Integer postNumber;

    @Column
    private String address;

    @Column
    private String addressDetail;

    @Column
    private String gender;

    @Column
    private String birthDate;

    @Column
    private Integer point;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
