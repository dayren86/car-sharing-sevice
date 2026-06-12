package mate.carsharing.dto.user;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.carsharing.model.Role;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<Role> role;
}

