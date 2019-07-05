package appointments.services;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.dto.ReservationDTO;
import appointments.exceptions.ChildNotFoundException;
import appointments.exceptions.ReservationAlreadyExistsException;
import appointments.exceptions.ReservationNotFoundException;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.exceptions.ServiceNotFoundException;
import appointments.mappers.ReservationMapper;
import appointments.repos.ChildrenRepository;
import appointments.repos.ReservationsRepository;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static appointments.utils.Constants.CHILD_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_ID_MESSAGE;
import static appointments.utils.Constants.RESERVATION_INCORRECT_DATETIME_MESSAGE;
import static appointments.utils.Constants.RESERVATION_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SERVICE_NOT_FOUND_MESSAGE;
import static java.util.stream.Collectors.toList;


/**
 * Класс, реализующий действия с объектами Запись на прием (бронь):
 * получение списка записей на прием за промежуток времени (неделю), день, получение одной конкретной записи,
 * осуществление записи на приём (с проверкой занятого времени)
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@org.springframework.stereotype.Service
public class ReservationsService {

    /** Поле для хранения экземпляра репозитория записей на прием */
    private ReservationsRepository reservationsRepository;

    /** Поле для хранения экземпляра репозитория расписаний */
    private SchedulesRepository schedulesRepository;

    /** Поле для хранения экземпляра репозитория детей */
    private ChildrenRepository childrenRepository;

    /** Поле для хранения экземпляра репозитория услуг */
    private ServicesRepository servicesRepository;

    /** Поле для хранения экземпляра маппера записей на прием в DTO */
    private ReservationMapper mapper;

    @Autowired
    public ReservationsService(
            ReservationsRepository reservationsRepository,
            SchedulesRepository schedulesRepository,
            ChildrenRepository childrenRepository,
            ServicesRepository servicesRepository,
            ReservationMapper mapper
    ) {
        this.reservationsRepository = reservationsRepository;
        this.schedulesRepository = schedulesRepository;
        this.childrenRepository = childrenRepository;
        this.servicesRepository = servicesRepository;
        this.mapper = mapper;
    }

    /** Метод для получения списка всех записей на прием */
    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservations(LocalDate date, LocalDate startDate, LocalDate endDate) {

        if (date == null && startDate == null && endDate == null) {

            log.debug("Getting list of all reservations");

            return mapper.reservationListToReservationDTOList(reservationsRepository.findAll());

        } else if (date != null && startDate == null && endDate == null) {

            return findReservationByDate(date);

        } else if (date == null && startDate != null && endDate != null) {

            return findReservationByPeriod(startDate, endDate);

        } else {

            log.error("Date/Period parameters are wrong");
            throw new IllegalArgumentException("Неправильные параметры даты/периода");
        }
    }

    /** Метод для поиска всех записей на прием в конкретную дату */
    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByDate(final LocalDate date) {

        log.debug("Finding reservation on the date: {}", date);

        if (date == null) {
            log.error("Parameter 'date' is null");
            throw new IllegalArgumentException(RESERVATION_INCORRECT_DATETIME_MESSAGE);
        }

        return mapper.reservationListToReservationDTOList(
                schedulesRepository
                        .findAll()
                        .stream()
                        .filter(s -> s.getDate().equals(date))
                        .flatMap(s -> s.getReservations().stream())
                        .collect(toList())
        );
    }

    /** Метод для поиска всех записей на прием за промежуток времени (например, неделю) */
    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationByPeriod(final LocalDate startDate, final LocalDate endDate) {

        log.debug("Finding reservation within period: {} - {}", startDate, endDate);

        if (startDate == null || endDate == null) {
            log.error("Parameter 'startDate' or 'endDate' is null");
            throw new IllegalArgumentException(RESERVATION_INCORRECT_DATETIME_MESSAGE);
        }

        return mapper.reservationListToReservationDTOList(
                schedulesRepository
                        .findAll()
                        .stream()
                        .filter(s -> (!s.getDate().isAfter(endDate) && !s.getDate().isBefore(startDate)))
                        .flatMap(s -> s.getReservations().stream())
                        .collect(toList())
        );
    }

    /** Метод для поиска конкретной записи на прием по идентификатору */
    @Transactional(readOnly = true)
    public ReservationDTO findReservationById(final Long id) {

        log.debug("Finding reservation with id = {}", id);

        if (id == null) {
            log.error("Parameter 'id' is null");
            throw new IllegalArgumentException(RESERVATION_EMPTY_ID_MESSAGE);
        }

        return mapper.reservationToReservationDTO(reservationsRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(RESERVATION_NOT_FOUND_MESSAGE + id))
        );
    }

    /** Метод для добавления новой записи на прием */
    @Transactional
    public ReservationDTO addReservation(final ReservationDTO dto) {

        final Reservation reservation = mapper.reservationDTOToReservation(dto);

        reservation.setId(null);
        reservation.setChild(
                childrenRepository
                        .findById(reservation.getChild().getId())
                        .orElseThrow(() -> new ChildNotFoundException(
                                        CHILD_NOT_FOUND_MESSAGE + reservation.getChild().getId()
                                )
                        )
        );
        reservation.setService(
                servicesRepository
                        .findById(reservation.getService().getId())
                        .orElseThrow(() -> new ServiceNotFoundException(
                                        SERVICE_NOT_FOUND_MESSAGE + reservation.getService().getId()
                                )
                        )
        );
        reservation.setSchedule(
                schedulesRepository
                        .findById(reservation.getSchedule().getId())
                        .orElseThrow(() -> new ScheduleNotFoundException(
                                        SCHEDULE_NOT_FOUND_MESSAGE + reservation.getSchedule().getId()
                                )
                        )
        );

        checkIfTimeInSchedule(reservation.getSchedule(), reservation.getDateTime());

        final Reservation savedReservation = reservationsRepository.save(reservation);
        log.info("Added new reservation: {}", savedReservation);

        savedReservation.getSchedule().getReservations().add(savedReservation);
        schedulesRepository.flush();

        return mapper.reservationToReservationDTO(savedReservation);
    }

    @Transactional
    void checkIfTimeInSchedule(Schedule schedule, LocalDateTime wantedDateTime) {

        final LocalDate wantedDate = wantedDateTime.toLocalDate();

        if (!wantedDate.equals(schedule.getDate())) {
            log.error("Wanted date is not equal to schedule's date");
            throw new IllegalArgumentException(RESERVATION_INCORRECT_DATETIME_MESSAGE);
        }

        final LocalTime wantedTime = wantedDateTime.toLocalTime();

        if (wantedTime.isBefore(schedule.getStartTime())
                || wantedTime.isAfter(schedule.getEndTime())) {
            log.error("Wanted time does not match schedule's reception time");
            throw new IllegalArgumentException(RESERVATION_INCORRECT_DATETIME_MESSAGE);
        }

        boolean timeIsBusy = schedule
                .getReservations()
                .stream()
                .anyMatch(r -> hasIntersection(r, schedule, wantedDateTime));

        if (timeIsBusy) {
            log.error("Wanted dateTime is already busy with another reservation");
            throw new ReservationAlreadyExistsException("Это время уже занято");
        }
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

}
