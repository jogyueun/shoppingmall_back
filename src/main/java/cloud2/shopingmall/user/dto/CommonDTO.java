package cloud2.shopingmall.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;


public class CommonDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRequest {

        @Valid
        private UserDTO.Join userDTO;


        @Valid
        private UserProfileDTO.Join userProfileDTO;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowResponse {

        private UserDTO.Show userDTO;

        private UserProfileDTO.Show userProfileDTO;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeInfoRequest {

        @Valid
        private UserProfileDTO.ChangeInfo userProfileDTO;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowAllUser {

        private List<UserDTO.Show> userDTOList;

        private List<UserProfileDTO.Show> userProfileDTOList;
    }

}
