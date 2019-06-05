package appointments.repos;

import appointments.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Класс-репозиторий, реализующий действия с объектами справочника Организации:
 * сохранение, удаление, получение списка целей обращения
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface OrganizationsRepository extends JpaRepository<Organization, Integer> {

    Optional<Organization> findOneByName(String name);
}
