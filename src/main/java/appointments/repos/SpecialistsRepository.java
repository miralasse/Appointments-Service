package appointments.repos;

import appointments.domain.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Класс-репозиторий, реализующий действия с объектами справочника Специалисты:
 * сохранение, редактирование, удаление, получение списка специалистов
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface SpecialistsRepository extends JpaRepository<Specialist, Integer> {

    Optional<Specialist> findOneByName(String name);
}
