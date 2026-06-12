package mate.carsharing.conroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.user.UserLoginRequestDto;
import mate.carsharing.dto.user.UserLoginResponseDto;
import mate.carsharing.dto.user.UserRegistrationRequestDto;
import mate.carsharing.dto.user.UserResponseDto;
import mate.carsharing.exception.RegistrationException;
import mate.carsharing.service.AuthenticationService;
import mate.carsharing.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User registration and login api")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "User registration")
    @PostMapping("/registration")
    public UserResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto registrationDto)
            throws RegistrationException {
        return userService.register(registrationDto);
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
