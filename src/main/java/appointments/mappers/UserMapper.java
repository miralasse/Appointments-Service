package appointments.mappers;

import appointments.domain.User;
import appointments.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author yanchenko_evgeniya
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "password", target = "matchingPassword"),
            @Mapping(source = "role.name", target = "roleName")
    })
    UserDTO userToUserDTO(User entity);


    @Mappings({
            @Mapping(source = "roleName", target = "role.name")
    })
    User userDTOToUser(UserDTO dto);



}
