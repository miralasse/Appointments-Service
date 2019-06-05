package appointments.services;

import appointments.domain.Child;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.exceptions.ReservationAlreadyExistsException;
import appointments.exceptions.ReservationNotFoundException;
import appointments.repos.ReservationsRepository;
import appointments.repos.SchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Класс, реализующий действия с объектами Запись на прием (бронь):
 * получение списка записей на прием за промежуток времени (неделю), день, получение одной конкретной записи,
 * осуществление записи на приём (с проверкой занятого времени)
 *
 * @author yanchenko_evgeniya
 */
@org.springframework.stereotype.Service
public class ReservationsService {

    private static final String EMPTY_ID_MESSAGE = "ID записи на прием не должен быть пустым";
    private static final String RESERVATION_NOT_FOUND_MESSAGE = "Запись на прием не найдена. ID: ";
    private static final String INCORRECT_DATETIME_MESSAGE
            = "Дата и время не заполнены или некорректны для указанного расписания";
    private static final String EMPTY_SCHEDULE_MESSAGE
            = "Для записи на прием не указано, к какому расписанию она относится";
    private static final String EMPTY_SERVICE_MESSAGE = "Для записи на прием должна быть указана цель обращения";
    private static final String EMPTY_CHILD_MESSAGE = "Для записи на прием должен быть указан ребенок";

    /** Поле для хранения экземпляра репозитория записей на прием */
    private ReservationsRepository reservationsRepository;

    /** Поле для хранения экземпляра репозитория расписаний */
    private SchedulesRepository schedulesRepository;

    @Autowired
    public ReservationsService(
            ReservationsRepository reservationsRepository,
            SchedulesRepository schedulesRepository
    ) {
        this.reservationsRepository = reservationsRepository;
        this.schedulesRepository = schedulesRepository;
    }


    /** Метод для поиска всех записей на прием в конкретную дату */
    @Transactional(readOnly = true)
    public List<Reservation> findReservationByDate(final LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException(INCORRECT_DATETIME_MESSAGE);
        }

        return schedulesRepository
                .findAll()
                .stream()
                .filter(s -> s.getDate().equals(date))
                .flatMap(s -> s.getReservations().stream())
                .collect(toList());
    }

    /** Метод для поиска всех записей на прием за промежуток времени (например, неделю) */
    @Transactional(readOnly = true)
    public List<Reservation> findReservationByPeriod(final LocalDate startDate, final LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(INCORRECT_DATETIME_MESSAGE);
        }

        return schedulesRepository
                .findAll()
                .stream()
                .filter(s -> (!s.getDate().isAfter(endDate) && !s.getDate().isBefore(startDate)))
                .flatMap(sch -> sch.getReservations().stream())
                .collect(toList());
    }

    /** Метод для поиска конкретной записи на прием по идентификатору */
    @Transactional(readOnly = true)
    public Reservation findReservationById(final Long id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }

        return reservationsRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(RESERVATION_NOT_FOUND_MESSAGE));
    }

    /** Метод для добавления новой записи на прием */
    @Transactional
    public Reservation addReservation(
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

        if (checkIfTimeIsBusy(schedule, wantedDateTime)) {
            throw new ReservationAlreadyExistsException("Это время уже занято");
        }

        final Reservation reservation = new Reservation(null, wantedDateTime, schedule, service, active, child);

        return reservationsRepository.save(reservation);
    }

    /** Служебный метод для проверки занятости указанного времени в расписании */
    @Transactional
    private boolean checkIfTimeIsBusy(Schedule schedule, LocalDateTime wantedDateTime) {

        return schedule
                .getReservations()
                .stream()
                .anyMatch(r -> hasIntersection(r, schedule, wantedDateTime));
    }

    /** Служебный метод для проверки пересечения времени, на которое хотим осуществить новую запись на прием,
     * с интервалами уже существующих записей */
    private boolean hasIntersection(Reservation reservation,
                                    Schedule schedule,
                                    LocalDateTime wantedDateTime) {

        return !wantedDateTime.isBefore(reservation.getDateTime())
                && wantedDateTime.isBefore(
                    reservation
                        .getDateTime()
                        .plus(schedule.getIntervalOfReception(), ChronoUnit.MINUTES)
                );
    }


    /** Метод для получения списка всех записей на прием */
    @Transactional(readOnly = true)
    public List<Reservation> getReservations() {
        return reservationsRepository.findAll();
    }


}
