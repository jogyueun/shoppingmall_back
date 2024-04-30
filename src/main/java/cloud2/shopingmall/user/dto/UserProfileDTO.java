package cloud2.shopingmall.user.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserProfileDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Join {

        @NotEmpty(message = "이름을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣]*$", message = "한글만 입력 가능합니다.")
        private String name;

        @NotEmpty(message = "이메일을 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 주소 형식이 아닙니다.")
        private String email;

        @NotEmpty(message = "핸드폰 번호를 입력해 주세요.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
        private String phoneNumber;

        @NotNull(message = "우편번호를 입력해 주세요.")
        private Integer postNumber;

        @NotEmpty(message = "주소를 입력해 주세요.")
        private String address;

        @NotEmpty(message = "상세주소를 입력해 주세요.")
        private String addressDetail;

        @NotEmpty(message = "성별을 선택해 주세요.")
        private String gender;

        @NotEmpty(message = "생년월일을 입력해 주세요.")
        private String birthDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindUser{

        private String phoneNumber;

        private String email;

        private String name;

        private Source source;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindPassword{

        @NotNull(message = "아이디를 입력해 주세요.")
        @NotEmpty(message = "아이디를 입력해 주세요.")
        private String username;

        private String phoneNumber;

        private String email;

        private Source source;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Show {

        private String name;

        private String email;

        private String phoneNumber;

        private Integer postNumber;

        private String address;

        private String addressDetail;

        private String gender;

        private String birthDate;

        private Integer point;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeInfo {

        @NotEmpty(message = "이름을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣]*$", message = "한글만 입력 가능합니다.")
        private String name;

        @NotEmpty(message = "이메일을 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 주소 형식이 아닙니다.")
        private String email;

        @NotEmpty(message = "핸드폰 번호를 입력해 주세요.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
        private String phoneNumber;

        @NotNull(message = "우편번호를 입력해 주세요.")
        private Integer postNumber;

        @NotEmpty(message = "주소를 입력해 주세요.")
        private String address;

        @NotEmpty(message = "상세주소를 입력해 주세요.")
        private String addressDetail;

        @NotEmpty(message = "성별을 선택해 주세요.")
        private String gender;

        @NotEmpty(message = "생년월일을 입력해 주세요.")
        private String birthDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddPoint {

        @NotNull(message = "충전할 포인트를 선택해주세요.")
        private Integer point;
    }

    @Getter
    public enum Gender {
        MALE("MALE"),
        FEMALE("FEMALE");

        private String key;

        Gender(String key) {
            this.key = key;
        }
    }

    @Getter
    public enum Source {
        PHONE("phone"), EMAIL("email");

        private String key;

        Source(String source) {
            this.key = source;
        }
    }
}
