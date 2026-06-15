package mate.carsharing.service;

import java.util.List;
import java.util.Optional;
import mate.carsharing.dto.user.UserRegistrationRequestDto;
import mate.carsharing.dto.user.UserResponseDto;
import mate.carsharing.dto.user.UserUpdateDto;
import mate.carsharing.exception.RegistrationException;
import mate.carsharing.model.Role;
import mate.carsharing.model.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto registrationDto)
            throws RegistrationException;

    UserResponseDto getUser(User user);

    User findUserById(Long id);

    List<User> findManagerUser();

    UserResponseDto updateRole(Long id, Role role);

    UserResponseDto updateProfile(User user, UserUpdateDto userUpdateDto);

    Optional<User> findByTelegramId(Long telegramId);

    String registerUserFromTelegram(Update update);
}
