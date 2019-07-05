package appointments.repos;

import appointments.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Класс-репозиторий, реализующий действия с объектами справочника Цели обращения(Услуги):
 * сохранение, удаление, получение списка целей обращения
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface ServicesRepository extends JpaRepository<Service, Integer> {

    Optional<Service> findOneByName(String name);
    List<Service> findAllByOrderByName();
    List<Service> findAll();
}
