package cloud2.shopingmall.jwt;


import cloud2.shopingmall.user.entity.User;
import cloud2.shopingmall.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 주어진 사용자의 이름으로 DB에서 사용자 정보 조회
        User userData = userRepository.findByUsername(username);

        // DB에 사용자가 존재해 데이터가 있을 경우 '사용자의 인증 및 권한 정보를 제공하는 역할'을 하는 UserDetails 객체 반환
        if (userData != null) {

            return new CustomUserDetails(userData);
        }

        // 사용자의 정보가 조회되지 않을 경우 null 값으로 반환
        return null;
    }
}
