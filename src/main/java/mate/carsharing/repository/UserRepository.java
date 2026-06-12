package mate.carsharing.repository;

import java.util.List;
import java.util.Optional;
import mate.carsharing.model.Role;
import mate.carsharing.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"userRole"})
    Optional<User> findByFirstName(String firstName);

    boolean existsByFirstName(String firstName);

    Optional<User> findByTelegramId(Long telegramId);

    List<User> findByUserRole_RoleName(Role.RoleName roleName);
}
