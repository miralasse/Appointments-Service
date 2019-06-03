package appointments.services;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.utils.DatabaseEmulator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


/**
 * Класс, реализующий действия с объектами Расписание:
 * сохранение, удаление, получение списка расписаний
 *
 * @author yanchenko_evgeniya
 */
public class SchedulesService {

    /** Строковая константа для передачи в исключение, возникающее, если ID расписания не заполнен */
    private static final String EMPTY_ID_MESSAGE = "ID расписания не должен быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если расписание с указанным ID не найдено */
    private static final String SCHEDULE_NOT_FOUND_MESSAGE = "Расписание не найдено. ID: ";

    /** Строковая константа для передачи в исключение, возникающее, если не указан специалист,
     * для которого формируется расписание */
    private static final String EMPTY_SPECIALIST_MESSAGE = "Расписание должно быть привязано к специалисту";

    /** Строковая константа для передачи в исключение, возникающее, если не указана дата,
     * на которую формируется расписание, или дата некорректная */
    private static final String INCORRECT_DATE_MESSAGE = "Для расписания должна быть указана корректная дата";

    /** Строковая константа для передачи в исключение, возникающее, если не указаны услуги для расписания */
    private static final String EMPTY_SERVICES_MESSAGE = "Для расписания должны быть указаны услуги";

    /** Строковая константа для передачи в исключение, возникающее, если не указано время начала приема */
    private static final String EMPTY_START_TIME_MESSAGE = "Время начала приема не может быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если не указано время окончания приема */
    private static final String EMPTY_END_TIME_MESSAGE = "Время окончания приема не может быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если не указан интервал приема
     * (время на один талон) */
    private static final String EMPTY_INTERVAL_MESSAGE = "Интервал приема не может быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если вместо списка броней NULL */
    private static final String NULL_RESERVATIONS_MESSAGE = "Список броней должен существовать";

    /** Поле для хранения экземпляра эмулятора базы данных */
    private DatabaseEmulator databaseEmulator;

    public SchedulesService() {
        databaseEmulator = DatabaseEmulator.getInstance();
    }

    /** Метод для добавления нового распиания */
    public Long addSchedule(
            final Specialist specialist, final LocalDate date, final List<Service> services,
            final LocalTime startTime, final LocalTime endTime, final Duration interval,
            final List<Reservation> reservations, final boolean active
    ) {

        if (specialist == null) {
            throw new IllegalArgumentException(EMPTY_SPECIALIST_MESSAGE);
        }
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(INCORRECT_DATE_MESSAGE);
        }
        if (services == null) {
            throw new IllegalArgumentException(EMPTY_SERVICES_MESSAGE);
        }
        if (startTime == null) {
            throw new IllegalArgumentException(EMPTY_START_TIME_MESSAGE);
        }
        if (endTime == null) {
            throw new IllegalArgumentException(EMPTY_END_TIME_MESSAGE);
        }
        if (interval == null) {
            throw new IllegalArgumentException(EMPTY_INTERVAL_MESSAGE);
        }
        if (reservations == null) {
            throw new IllegalArgumentException(NULL_RESERVATIONS_MESSAGE);
        }

        final Schedule schedule = new Schedule(
                null, specialist, date, services, startTime, endTime,
                interval, reservations, active
        );

        return databaseEmulator.addSchedule(schedule);
    }

    /** Метод для удаления расписания по идентификатору */
    public boolean removeSchedule(final Long id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Schedule schedule = databaseEmulator.findScheduleById(id);
        if (schedule == null) {
            throw new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE);
        }

        return databaseEmulator.removeSchedule(schedule);
    }

    /** Метод для поиска цели обращения по идентификатору */
    public Schedule findScheduleById(final Long id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Schedule schedule = databaseEmulator.findScheduleById(id);
        if (schedule == null) {
            throw new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE);
        } else {

            return schedule;
        }
    }

    /** Метод для получения списка расписаний */
    public List<Schedule> getSchedules() {
        return databaseEmulator.getSchedules();
    }
}
