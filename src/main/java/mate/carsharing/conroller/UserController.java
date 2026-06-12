package mate.carsharing.conroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.carsharing.dto.user.UserResponseDto;
import mate.carsharing.dto.user.UserUpdateDto;
import mate.carsharing.model.Role;
import mate.carsharing.model.User;
import mate.carsharing.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Api")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Show user profile")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/me")
    public UserResponseDto getUserProfile(@AuthenticationPrincipal User user) {
        return userService.getUser(user);
    }

    @Operation(summary = "Show user information")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PatchMapping("/me")
    public UserResponseDto updateUserProfile(@AuthenticationPrincipal User user,
                                             @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateProfile(user, userUpdateDto);
    }

    @Operation(summary = "Update user")
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}/role")
    public UserResponseDto updateUserRole(@PathVariable Long id, Role role) {
        return userService.updateRole(id, role);
    }
}
