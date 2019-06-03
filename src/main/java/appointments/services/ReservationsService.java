package appointments.services;

import appointments.domain.Child;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.exceptions.ReservationAlreadyExistsException;
import appointments.exceptions.ReservationNotFoundException;
import appointments.utils.DatabaseEmulator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Класс, реализующий действия с объектами Запись на прием (бронь):
 * получение списка записей на прием за промежуток времени (неделю), день, получение одной конкретной записи,
 * осуществление записи на приём (с проверкой занятого времени)
 *
 * @author yanchenko_evgeniya
 */
public class ReservationsService {

    /** Строковая константа для передачи в исключение, возникающее, если ID записи на прием не заполнен */
    private static final String EMPTY_ID_MESSAGE = "ID записи на прием не должен быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если запись на прием с указанным ID не найдена */
    private static final String RESERVATION_NOT_FOUND_MESSAGE = "Запись на прием не найдена. ID: ";

    /** Строковая константа для передачи в исключение,
     * возникающее, если не указана или некорректная дата и время записи на прием */
    private static final String INCORRECT_DATETIME_MESSAGE
            = "Дата и время не заполнены или некорректны для указанного расписания";

    /** Строковая константа для передачи в исключение,
     * возникающее, если не указано расписание,
     * в котором осуществляется эта запись на прием */
    private static final String EMPTY_SCHEDULE_MESSAGE
            = "Для записи на прием не указано, к какому расписанию она относится";

    /** Строковая константа для передачи в исключение,
     * возникающее, если не указана цель обращения (услуга) */
    private static final String EMPTY_SERVICE_MESSAGE = "Для записи на прием должна быть указана цель обращения";

    /** Строковая константа для передачи в исключение,
     * возникающее, если не указан ребенок,
     * в интересах которого осуществляется запись на прием */
    private static final String EMPTY_CHILD_MESSAGE = "Для записи на прием должен быть указан ребенок";

    /** Поле для хранения экземпляра эмулятора базы данных */
    private DatabaseEmulator databaseEmulator;

    public ReservationsService() {
        databaseEmulator = DatabaseEmulator.getInstance();
    }


    /** Метод для поиска всех записей на прием в конкретную дату */
    public List<Reservation> findReservationByDate(final LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException(INCORRECT_DATETIME_MESSAGE);
        }

        return databaseEmulator
                .getSchedules()
                .stream()
                .filter(s -> s.getDate().equals(date))
                .flatMap(sch -> sch.getReservations().stream())
                .collect(toList());
    }

    /** Метод для поиска всех записей на прием за промежуток времени (например, неделю) */
    public List<Reservation> findReservationByPeriod(final LocalDate startDate, final LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(INCORRECT_DATETIME_MESSAGE);
        }

        return databaseEmulator
                .getSchedules()
                .stream()
                .filter(s -> (!s.getDate().isAfter(endDate) && !s.getDate().isBefore(startDate)))
                .flatMap(sch -> sch.getReservations().stream())
                .collect(toList());
    }

    /** Метод для поиска конкретной записи на прием по идентификатору */
    public Reservation findReservationById(final Long id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Reservation reservation = databaseEmulator.findReservationById(id);
        if (reservation == null) {
            throw new ReservationNotFoundException(RESERVATION_NOT_FOUND_MESSAGE);
        } else {

            return reservation;
        }
    }

    /** Метод для добавления новой записи на прием */
    public Long addReservation(
            final LocalDateTime wantedDateTime, final Schedule schedule,
            final Service service, final boolean active, final Child child
    ) {

        if (schedule == null) {
            throw new IllegalArgumentException(EMPTY_SCHEDULE_MESSAGE);
        }
        if (service == null) {
            throw new IllegalArgumentException(EMPTY_SERVICE_MESSAGE);
        }
        if (child == null) {
            throw new IllegalArgumentException(EMPTY_CHILD_MESSAGE);
        }
        if (wantedDateTime == null) {
            throw new IllegalArgumentException(INCORRECT_DATETIME_MESSAGE);
        }

        final LocalDate wantedDate = wantedDateTime.toLocalDate();
        if (!wantedDate.equals(schedule.getDate())) {
            throw new IllegalArgumentException(INCORRECT_DATETIME_MESSAGE);
        }

        final LocalTime wantedTime = wantedDateTime.toLocalTime();
        if (wantedTime.isBefore(schedule.getStartTime()) || wantedTime.isAfter(schedule.getEndTime())) {
            throw new IllegalArgumentException(INCORRECT_DATETIME_MESSAGE);
        }
        final boolean timeIsBusy = schedule
                .getReservations()
                .stream()
                .anyMatch(r -> r.getDateTime().equals(wantedDateTime));
        if (timeIsBusy) {
            throw new ReservationAlreadyExistsException("Это время уже занято");
        }

        final Reservation reservation
                = new Reservation(null, wantedDateTime, schedule, service, active, child);

        return databaseEmulator.addReservation(reservation);
    }

    /** Метод для получения списка всех записей на прием */
    public List<Reservation> getReservations() {
        return databaseEmulator.getReservations();
    }


}
