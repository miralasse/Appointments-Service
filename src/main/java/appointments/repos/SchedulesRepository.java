package appointments.repos;

import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


/**
 * Класс-репозиторий, реализующий действия с объектами Расписание:
 * сохранение, удаление, получение списка расписаний
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findAllByDateOrderByStartTime(Pageable pageable, LocalDate date);

    Optional<Schedule> findOneBySpecialistAndDate(Specialist specialist, LocalDate date);

    boolean existsBySpecialist(Specialist specialist);

    boolean existsByServices(Service service);

}
