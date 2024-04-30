package cloud2.shopingmall.user.mapper;

import cloud2.shopingmall.common.mapper.EntityMapper;
import cloud2.shopingmall.user.dto.UserDTO;
import cloud2.shopingmall.user.dto.UserProfileDTO;
import cloud2.shopingmall.user.entity.User;
import cloud2.shopingmall.user.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMainMapper {

    @Mapper(componentModel = "spring")
    interface UserJoinMapper extends EntityMapper<User, UserDTO.Join> {
    }
    @Mapper(componentModel = "spring")
    interface UserProfileJoinMapper extends EntityMapper<UserProfile, UserProfileDTO.Join> {
    }

    @Mapper(componentModel = "spring")
    interface UserShowMapper extends EntityMapper<User, UserDTO.Show> {
    }

    @Mapper(componentModel = "spring")
    interface UserShowAllMapper extends EntityMapper<User, UserDTO.ShowAllUser> {
    }

    @Mapper(componentModel = "spring")
    interface UserProfileShowMapper extends EntityMapper<UserProfile, UserProfileDTO.Show> {
    }

    @Mapper(componentModel = "spring")
    interface UserProfileChangeMapper extends EntityMapper<UserProfile, UserProfileDTO.ChangeInfo> {
    }

}
