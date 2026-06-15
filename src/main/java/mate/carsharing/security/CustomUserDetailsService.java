package mate.carsharing.security;

import lombok.RequiredArgsConstructor;
import mate.carsharing.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {
        return userRepository.findByFirstName(firstName)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Can't find user by firstName: " + firstName));
    }
}
