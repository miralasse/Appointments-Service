package services;

import appointments.domain.Child;
import appointments.domain.Organization;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ReservationAlreadyExistsException;
import appointments.exceptions.ReservationNotFoundException;
import appointments.services.ReservationsService;
import appointments.utils.DatabaseEmulator;
import appointments.utils.EmulatorInitializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
public class ReservationsServiceTest {

    private static final int THIRD_ID = 3;

    private static final long FOURTH_ID = 4;

    private static final int YEAR = 2019;
    private static final Month MONTH = Month.AUGUST;
    private static final int DAY = 12;

    private final static LocalDateTime DATE_TIME = LocalDateTime.of(YEAR, MONTH, DAY, 10, 15);

    private static final Organization ORGANIZATION = new Organization(
                    3, "Управление культуры Белгородской области",
            "г. Белгород, Гражданский пр-т, 40", "+7(4722)27‑72-52"
    );

    private static final Specialist SPECIALIST
            = new Specialist(4, "Иванова Ольга Викторовна", "25", true, ORGANIZATION);

    private static final LocalDate DATE = LocalDate.of(YEAR, MONTH, DAY);

    private static final List<Service> SERVICES = DatabaseEmulator.getInstance().getServices();

    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(13, 0);
    private static final Duration INTERVAL = Duration.of(15, ChronoUnit.MINUTES);

    private static final List<Reservation> RESERVATIONS = new ArrayList<>();

    private final static Schedule SCHEDULE = new Schedule(
            FOURTH_ID, SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
    );

    private final static Service SERVICE = new Service(THIRD_ID, "Оформление документов", true);
    private final static Child CHILD = new Child();

    private static EmulatorInitializer emulatorInitializer = new EmulatorInitializer();
    private ReservationsService reservationsService = new ReservationsService();

    @BeforeClass
    public static void initCollections() {
        emulatorInitializer.initAll();
    }

    @Before
    public void initTest() {
        reservationsService.getReservations().clear();
        reservationsService.getReservations().addAll(emulatorInitializer.initReservations());
    }

    @Test
    public void testFindReservationByDate() {

        final int expectedListSize = 2;

        final List<Reservation> reservationsByDate = reservationsService.findReservationByDate(DATE);
        assertThat(reservationsByDate).hasSize(expectedListSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindReservationByDateWithNullDate() {
        reservationsService.findReservationByDate(null);
    }

    @Test
    public void findReservationByPeriod() {

        final int daysToAdd = 5;
        final int expectedListSize = 4;

        final List<Reservation> reservationsByPeriod
                = reservationsService.findReservationByPeriod(DATE, LocalDate.of(YEAR, MONTH, DAY + daysToAdd));

        assertThat(reservationsByPeriod).hasSize(expectedListSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindReservationByPeriodWithNullStartDate() {
        reservationsService.findReservationByPeriod(null, DATE.plusWeeks(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindReservationByPeriodWithNullEndDate() {
        reservationsService.findReservationByPeriod(DATE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindReservationByPeriodWithBothNullDates() {
        reservationsService.findReservationByPeriod(null, null);
    }

    @Test
    public void testFindReservationById() {

        final long id = reservationsService.addReservation(DATE_TIME, SCHEDULE, SERVICE, true, CHILD);
        final Reservation expectedReservation = new Reservation(id, DATE_TIME, SCHEDULE, SERVICE, true, CHILD);

        final Reservation actualReservation = reservationsService.findReservationById(id);

        assertThat(expectedReservation).isEqualTo(actualReservation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindReservationByIdWithNullId() {
        reservationsService.findReservationById(null);
    }

    @Test(expected = ReservationNotFoundException.class)
    public void testFindReservationByIdWithWrongId() {
        reservationsService.findReservationById(Long.MIN_VALUE);
    }

    @Test
    public void testAddReservation() {

        final int expectedSize = reservationsService.getReservations().size() + 1;

        final long id = reservationsService.addReservation(DATE_TIME, SCHEDULE, SERVICE, true, CHILD);

        final int actualSize = reservationsService.getReservations().size();
        final Reservation testReservation = new Reservation(id, DATE_TIME, SCHEDULE, SERVICE, true, CHILD);

        assertThat(reservationsService.getReservations()).contains(testReservation);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddReservationWithNullSchedule() {
        reservationsService.addReservation(DATE_TIME, null, SERVICE, true, CHILD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddReservationWithNullService() {
        reservationsService.addReservation(DATE_TIME, SCHEDULE, null, true, CHILD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddReservationWithNullChild() {
        reservationsService.addReservation(DATE_TIME, SCHEDULE, SERVICE, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddReservationWithNullDateTime() {
        reservationsService.addReservation(null, SCHEDULE, SERVICE, true, CHILD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddReservationWithWrongDate() {

        final int date = 30;
        final int hour = 9;
        final int minutes = 15;
        final LocalDateTime wrongDate = LocalDateTime.of(YEAR, Month.JULY, date, hour, minutes);
        reservationsService.addReservation(wrongDate, SCHEDULE, SERVICE, true, CHILD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddReservationWithWrongTime() {

        final int wrongHour = 16;
        final int wrongMinutes = 30;
        final LocalDateTime dateWithWrongTime = LocalDateTime.of(YEAR, MONTH, DAY, wrongHour, wrongMinutes);
        reservationsService.addReservation(dateWithWrongTime, SCHEDULE, SERVICE, true, CHILD);
    }

    @Test(expected = ReservationAlreadyExistsException.class)
    public void testAddReservationWithAlreadyExistedTime() {

        final int existedHour = 12;
        final int existedMinutes = 0;
        RESERVATIONS.add(new Reservation(
                FOURTH_ID, LocalDateTime.of(YEAR, MONTH, DAY, existedHour, existedMinutes),
                SCHEDULE, SERVICE, true, CHILD
        ));

        LocalDateTime existedDateTime = LocalDateTime.of(YEAR, MONTH, DAY, existedHour, existedMinutes);

        reservationsService.addReservation(existedDateTime, SCHEDULE, SERVICE, true, CHILD);
    }
}