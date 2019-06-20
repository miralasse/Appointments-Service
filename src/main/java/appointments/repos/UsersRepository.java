package appointments.repos;

import appointments.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Класс-репозиторий, реализующий действия с объектами Пользователи:
 * сохранение, удаление, получение списка, поиск по имени пользователя
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    Optional<User> findOneByUsername(String username);
}
