package appointments.repos;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  Класс-репозиторий, реализующий действия с объектами Запись на прием (бронь):
 *  добавление, поиск, удаление
 *
 * @author yanchenko_evgeniya
 */
@Repository
public interface ReservationsRepository extends JpaRepository<Reservation, Long> {

    boolean existsBySchedule(Schedule schedule);
}
