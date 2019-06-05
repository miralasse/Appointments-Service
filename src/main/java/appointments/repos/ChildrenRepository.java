package appointments.repos;

import appointments.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Класс-репозиторий, реализующий действия с объектами таблицы Дети:
 * сохранение, удаление, получение списка, поиск по id и по номеру свидетельства о рождении
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface ChildrenRepository extends JpaRepository<Child, Integer> {

    Optional<Child> findOneByBirthCertificateNumber(Integer birthCertificateNumber);
}
