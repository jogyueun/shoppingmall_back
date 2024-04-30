package cloud2.shopingmall.user.repository;


import cloud2.shopingmall.user.entity.User;
import cloud2.shopingmall.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();
    Boolean existsByUsername(String username);
    User findByUsername(String username);


}
