package appointments.services;

import appointments.TestHelper;
import appointments.domain.Child;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ReservationAlreadyExistsException;
import appointments.exceptions.ReservationNotFoundException;
import appointments.repos.ChildrenRepository;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationsServiceTest {

    private static final int YEAR = 2019;
    private static final Month MONTH = Month.AUGUST;
    private static final int DAY = 12;
    private static final LocalDate DATE = LocalDate.of(YEAR, MONTH, DAY);
    private final static LocalDateTime DATE_TIME = LocalDateTime.of(YEAR, MONTH, DAY, 10, 15);
    private final static int BIRTH_CERTIFICATE = 456845;
    private static final String SPECIALIST_NAME = "Специалист 1";
    private static final String SERVICE_NAME = "Получение путевки в ДОО";

    private Service service;
    private Child child;
    private Schedule schedule;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private SpecialistsRepository specialistsRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private ChildrenRepository childrenRepository;

    @Autowired
    private SchedulesRepository schedulesRepository;

    @Autowired
    private ReservationsService reservationsService;

    @Before
    public void setUp() {

        testHelper.clearAll();
        testHelper.initAll();
        service = servicesRepository.findOneByName(SERVICE_NAME).orElse(null);
        child = childrenRepository.findOneByBirthCertificateNumber(BIRTH_CERTIFICATE).orElse(null);
        final Specialist specialist = specialistsRepository.findOneByName(SPECIALIST_NAME).orElse(null);
        schedule = schedulesRepository.findOneBySpecialistAndDate(specialist, DATE).orElse(null);
        System.out.println(schedule.getReservations());
    }

    @Test
    @Transactional
    public void testFindReservationByDate() {
        final int expectedListSize = 2;
        final List<Reservation> reservationsByDate = reservationsService.findReservationByDate(DATE);

        assertThat(reservationsByDate).hasSize(expectedListSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindReservationByDateWithNullDate() {
        reservationsService.findReservationByDate(null);
    }

    @Test
    @Transactional
    public void findReservationByPeriod() {

        final int daysToAdd = 5;
        final int expectedListSize = 4;

        final List<Reservation> reservationsByPeriod
                = reservationsService.findReservationByPeriod(DATE, LocalDate.of(YEAR, MONTH, DAY + daysToAdd));

        assertThat(reservationsByPeriod).hasSize(expectedListSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindReservationByPeriodWithNullStartDate() {
        reservationsService.findReservationByPeriod(null, DATE.plusWeeks(1));
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindReservationByPeriodWithNullEndDate() {
        reservationsService.findReservationByPeriod(DATE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindReservationByPeriodWithBothNullDates() {
        reservationsService.findReservationByPeriod(null, null);
    }

    @Test
    @Transactional
    public void testFindReservationById() {

        final long id = reservationsService
                .addReservation(DATE_TIME, schedule, service, true, child)
                .getId();
        final Reservation expectedReservation = new Reservation(id, DATE_TIME, schedule, service, true, child);

        final Reservation actualReservation = reservationsService.findReservationById(id);

        assertThat(expectedReservation).isEqualTo(actualReservation);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindReservationByIdWithNullId() {
        reservationsService.findReservationById(null);
    }

    @Test(expected = ReservationNotFoundException.class)
    @Transactional
    public void testFindReservationByIdWithWrongId() {
        reservationsService.findReservationById(Long.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testAddReservation() {

        final int expectedSize = reservationsService.getReservations().size() + 1;

        final long id = reservationsService
                .addReservation(DATE_TIME, schedule, service, true, child)
                .getId();

        final int actualSize = reservationsService.getReservations().size();
        final Reservation testReservation = new Reservation(id, DATE_TIME, schedule, service, true, child);

        assertThat(reservationsService.getReservations()).contains(testReservation);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithNullSchedule() {
        reservationsService.addReservation(DATE_TIME, null, service, true, child);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithNullService() {
        reservationsService.addReservation(DATE_TIME, schedule, null, true, child);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithNullChild() {
        reservationsService.addReservation(DATE_TIME, schedule, service, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithNullDateTime() {
        reservationsService.addReservation(null, schedule, service, true, child);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithWrongDate() {

        final int date = 30;
        final int hour = 9;
        final int minutes = 15;
        final LocalDateTime wrongDate = LocalDateTime.of(YEAR, Month.JULY, date, hour, minutes);

        reservationsService.addReservation(wrongDate, schedule, service, true, child);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithWrongTime() {

        final int wrongHour = 16;
        final int wrongMinutes = 30;
        final LocalDateTime dateWithWrongTime = LocalDateTime.of(YEAR, MONTH, DAY, wrongHour, wrongMinutes);

        reservationsService.addReservation(dateWithWrongTime, schedule, service, true, child);
    }

    @Test(expected = ReservationAlreadyExistsException.class)
    @Transactional
    public void testAddReservationWithAlreadyExistedTime() {

        final int reservedHour = 9;
        final int reservedMinutes = 20;

        LocalDateTime existedDateTime = LocalDateTime.of(YEAR, MONTH, DAY, reservedHour, reservedMinutes);

        reservationsService.addReservation(existedDateTime, schedule, service, true, child);
    }
}