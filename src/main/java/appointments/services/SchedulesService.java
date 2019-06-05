package appointments.services;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.repos.SchedulesRepository;
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

        return schedulesRepository.save(schedule);
    }

    /** Метод для удаления расписания по идентификатору */
    @Transactional
    public void removeSchedule(final Long id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Schedule schedule = schedulesRepository
                .findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE));

        schedulesRepository.delete(schedule);
    }

    /** Метод для поиска расписания по идентификатору */
    @Transactional(readOnly = true)
    public Schedule findScheduleById(final Long id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }

        return schedulesRepository
                .findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE));
    }

    /** Метод для получения списка расписаний */
    @Transactional(readOnly = true)
    public List<Schedule> getSchedules() {
        return schedulesRepository.findAll();
    }
}
