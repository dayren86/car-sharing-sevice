package mate.carsharing.service;

import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.user.UserLoginRequestDto;
import mate.carsharing.dto.user.UserLoginResponseDto;
import mate.carsharing.repository.UserRepository;
import mate.carsharing.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getFirstName(), requestDto.getPassword())
        );

        String token = jwtUtil.generateToken(requestDto.getFirstName());
        return new UserLoginResponseDto(token);
    }
}
