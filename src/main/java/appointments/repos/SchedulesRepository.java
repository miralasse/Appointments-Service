package appointments.repos;

import appointments.domain.Schedule;
import appointments.domain.Specialist;
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

    Optional<Schedule> findOneBySpecialistAndDate(Specialist specialist, LocalDate date);
}
