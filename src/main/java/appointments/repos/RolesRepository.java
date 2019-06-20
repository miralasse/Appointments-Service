package appointments.repos;

import appointments.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Класс-репозиторий, реализующий действия с объектами Роли:
 * сохранение, удаление, получение списка, поиск по названию роли
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findOneByName(String roleName);
}
