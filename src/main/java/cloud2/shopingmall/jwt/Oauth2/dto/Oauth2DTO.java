package cloud2.shopingmall.jwt.Oauth2.dto;

import lombok.Data;


public class Oauth2DTO {

    @Data
    public static class Access {

        private String authorizationCode;

        private String state;
    }

    @Data
    public static class Join {
        private String username;
        private String name;
        private String email;
        private String birth;
        private String gender;
    }



}
