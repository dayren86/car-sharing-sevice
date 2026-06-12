package mate.carsharing.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.carsharing.validation.Passwords;

@Getter
@Setter
@Passwords
public class UserRegistrationRequestDto {
    @NotBlank
    @Size(min = 5, max = 200)
    private String password;
    @NotBlank
    @Size(min = 8, max = 200)
    private String repeatPassword;
    @NotBlank
    @Size(min = 3, max = 200)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 200)
    private String lastName;
    private Long telegramId;
}
