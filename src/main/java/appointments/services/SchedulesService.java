package appointments.services;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.repos.SchedulesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


/**
 * Класс, реализующий действия с объектами Расписание:
 * сохранение, удаление, получение списка расписаний
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@org.springframework.stereotype.Service
public class SchedulesService {

    private static final String EMPTY_ID_MESSAGE = "ID расписания не должен быть пустым";
    private static final String SCHEDULE_NOT_FOUND_MESSAGE = "Расписание не найдено. ID: ";
    private static final String EMPTY_SPECIALIST_MESSAGE = "Расписание должно быть привязано к специалисту";
    private static final String INCORRECT_DATE_MESSAGE = "Для расписания должна быть указана корректная дата";
    private static final String EMPTY_SERVICES_MESSAGE = "Для расписания должны быть указаны услуги";
    private static final String EMPTY_START_TIME_MESSAGE = "Время начала приема не может быть пустым";
    private static final String EMPTY_END_TIME_MESSAGE = "Время окончания приема не может быть пустым";
    private static final String EMPTY_INTERVAL_MESSAGE = "Интервал приема не может быть пустым";
    private static final String NULL_RESERVATIONS_MESSAGE = "Список броней должен существовать";

    /** Поле для хранения экземпляра репозитория */
    private SchedulesRepository schedulesRepository;

    @Autowired
    public SchedulesService(SchedulesRepository schedulesRepository) {
        this.schedulesRepository = schedulesRepository;
    }

    /** Метод для добавления нового распиания */
    @Transactional
    public Schedule addSchedule(
            final Specialist specialist, final LocalDate date, final List<Service> services,
            final LocalTime startTime, final LocalTime endTime, final Integer interval,
            final List<Reservation> reservations, final boolean active
    ) {

        if (specialist == null) {
            log.error("Parameter 'specialist' is null");
            throw new IllegalArgumentException(EMPTY_SPECIALIST_MESSAGE);
        }
        if (date == null || date.isBefore(LocalDate.now())) {
            log.error("Value of parameter 'date' is incorrect or null: {}", date);
            throw new IllegalArgumentException(INCORRECT_DATE_MESSAGE);
        }
        if (services == null) {
            log.error("Parameter 'services' is null");
            throw new IllegalArgumentException(EMPTY_SERVICES_MESSAGE);
        }
        if (startTime == null) {
            log.error("Parameter 'startTime' is null");
            throw new IllegalArgumentException(EMPTY_START_TIME_MESSAGE);
        }
        if (endTime == null) {
            log.error("Parameter 'endTime' is null");
            throw new IllegalArgumentException(EMPTY_END_TIME_MESSAGE);
        }
        if (interval == null) {
            log.error("Parameter 'interval' is null");
            throw new IllegalArgumentException(EMPTY_INTERVAL_MESSAGE);
        }
        if (reservations == null) {
            log.error("Parameter 'reservation' is null");
            throw new IllegalArgumentException(NULL_RESERVATIONS_MESSAGE);
        }

        final Schedule schedule = schedulesRepository.save(
                new Schedule(
                null, specialist, date, services, startTime, endTime,
                interval, reservations, active
                )
        );

        log.info("Added new schedule: {}", schedule);

        return schedule;
    }

    /** Метод для удаления расписания по идентификатору */
    @Transactional
    public void removeSchedule(final Long id) {

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Schedule schedule = schedulesRepository
                .findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE));

        schedulesRepository.delete(schedule);

        log.info("Schedule with id = {} deleted", id);
    }

    /** Метод для поиска расписания по идентификатору */
    @Transactional(readOnly = true)
    public Schedule findScheduleById(final Long id) {

        log.debug("Finding schedule with id = {}", id);

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }

        return schedulesRepository
                .findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE));
    }

    /** Метод для получения списка расписаний */
    @Transactional(readOnly = true)
    public List<Schedule> getSchedules() {

        log.debug("Getting list of all schedules");

        return schedulesRepository.findAll();
    }
}
