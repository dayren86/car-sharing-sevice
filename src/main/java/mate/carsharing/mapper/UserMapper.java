package mate.carsharing.mapper;

import java.util.Set;
import mate.carsharing.config.MapperConfig;
import mate.carsharing.dto.user.UserRegistrationRequestDto;
import mate.carsharing.dto.user.UserResponseDto;
import mate.carsharing.dto.user.UserUpdateDto;
import mate.carsharing.model.Role;
import mate.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    @Mapping(qualifiedByName = "encode", target = "password")
    User toUserModel(UserRegistrationRequestDto registration, Set<Role> userRole);

    @Named("encode")
    default String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    UserResponseDto toDto(User user);

    void updateUser(@MappingTarget User user, UserUpdateDto userUpdateDto);

    UserRegistrationRequestDto toTelegramRegistration(String firstName,
                                                      String lastName,
                                                      Long telegramId,
                                                      String password,
                                                      String repeatPassword);
}
