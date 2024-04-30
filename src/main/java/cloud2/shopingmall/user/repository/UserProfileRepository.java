package cloud2.shopingmall.user.repository;

import cloud2.shopingmall.user.entity.User;
import cloud2.shopingmall.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByEmail(String email);

    UserProfile findByPhoneNumber(String phoneNumber);

    UserProfile findByUserUsername(String userName);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmailAndUser(String email, User user);

    Boolean existsByPhoneNumberAndUser(String phoneNumber, User user);

}
