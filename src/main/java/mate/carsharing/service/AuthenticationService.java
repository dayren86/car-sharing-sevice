package mate.carsharing.service;

import mate.carsharing.dto.user.UserLoginRequestDto;
import mate.carsharing.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);
}
