package mate.carsharing.service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.user.UserRegistrationRequestDto;
import mate.carsharing.dto.user.UserResponseDto;
import mate.carsharing.dto.user.UserUpdateDto;
import mate.carsharing.exception.EntityNotFoundException;
import mate.carsharing.exception.RegistrationException;
import mate.carsharing.mapper.UserMapper;
import mate.carsharing.model.Role;
import mate.carsharing.model.User;
import mate.carsharing.repository.RoleRepository;
import mate.carsharing.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationDto)
            throws RegistrationException {
        if (userRepository.existsByFirstName(registrationDto.getFirstName())) {
            throw new RegistrationException(
                    "Email already exist: " + registrationDto.getFirstName());
        }
        Role role = roleRepository.findByRoleName(Role.RoleName.CUSTOMER);

        User userModel = userMapper.toUserModel(registrationDto, Collections.singleton(role));
        userRepository.save(userModel);

        return userMapper.toDto(userModel);
    }

    @Override
    public UserResponseDto getUser(User user) {
        return userMapper.toDto(findUserById(user.getId()));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find user by id: " + id));
    }

    @Override
    public List<User> findManagerUser() {
        return userRepository.findByUserRole_RoleName(Role.RoleName.MANAGER);
    }

    @Override
    public UserResponseDto updateRole(Long id, Role role) {
        User user = findUserById(id);
        user.setUserRole(Collections.singleton(role));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateProfile(User user, UserUpdateDto userUpdateDto) {
        User userById = findUserById(user.getId());
        userMapper.updateUser(userById, userUpdateDto);
        userRepository.save(userById);
        return userMapper.toDto(userById);
    }

    @Override
    public Optional<User> findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Override
    public String registerUserFromTelegram(Update update) {
        String password = generatePassword();
        UserRegistrationRequestDto telegramRegistration =
                userMapper.toTelegramRegistration(update.getMessage().getFrom().getUserName(),
                        update.getMessage().getFrom().getLastName(),
                        update.getMessage().getChatId(),
                        password,
                        password);
        try {
            register(telegramRegistration);
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }
        return password;
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            password.append(digit);
        }

        return password.toString();
    }
}
