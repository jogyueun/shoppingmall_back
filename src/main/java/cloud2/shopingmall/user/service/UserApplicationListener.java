package cloud2.shopingmall.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class UserApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private UserService userService;

    public UserApplicationListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 실행하고자 하는 코드 작성
        userService.createAdminIfNotExists();
    }
}
