package mate.carsharing.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    @NotBlank
    private String firstName;
    @NotBlank
    @Size(min = 5, max = 200)
    private String password;
}
