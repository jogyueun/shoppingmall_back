package cloud2.shopingmall.jwt;


import cloud2.shopingmall.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails  implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {

        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {


                return user.getUserRole();

            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    public String getName() {
        return user.getUserProfile().getName();
    }

    public String getEmail() {
        return user.getUserProfile().getEmail();
    }

    // 계정이 만료 되었는지 나타내는 메서드
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    // 계정이 잠겼는지 나타내는 메서드
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    // 사용자의 인증 자격이 만료되지 않았는지를 나타내는 메서드
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    // 사용자 계정이 활성화되었는지를 나타내는 메서드
    @Override
    public boolean isEnabled() {

        return true;
    }
}
