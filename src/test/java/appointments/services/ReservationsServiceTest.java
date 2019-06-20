package appointments.services;

import appointments.TestHelper;
import appointments.domain.Child;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.dto.ReservationDTO;
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

        testHelper.refill();
        service = servicesRepository.findOneByName(SERVICE_NAME).orElse(null);
        child = childrenRepository.findOneByBirthCertificateNumber(BIRTH_CERTIFICATE).orElse(null);
        final Specialist specialist = specialistsRepository.findOneByName(SPECIALIST_NAME).orElse(null);
        schedule = schedulesRepository.findOneBySpecialistAndDate(specialist, DATE).orElse(null);
    }

    @Test
    @Transactional
    public void testFindReservationByDate() {
        final int expectedListSize = 2;
        final List<ReservationDTO> reservationsByDate = reservationsService.findReservationByDate(DATE);

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

        final List<ReservationDTO> reservationsByPeriod
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
                .addReservation(
                        new ReservationDTO(
                                null,
                                DATE_TIME,
                                schedule.getId(),
                                service.getId(),
                                true,
                                child.getId()
                        )
                ).getId();

        final ReservationDTO expectedReservationDTO = new ReservationDTO(
                id,
                DATE_TIME,
                schedule.getId(),
                service.getId(),
                true,
                child.getId()
        );

        final ReservationDTO actualReservationDTO = reservationsService.findReservationById(id);

        assertThat(expectedReservationDTO).isEqualTo(actualReservationDTO);
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

        final int expectedSize = reservationsService.getReservations(null, null, null).size() + 1;

        final long id = reservationsService
                .addReservation(
                        new ReservationDTO(
                                null,
                                DATE_TIME,
                                schedule.getId(),
                                service.getId(),
                                true,
                                child.getId()
                        )
                ).getId();

        final int actualSize = reservationsService.getReservations(null, null, null).size();
        final ReservationDTO testReservationDTO = new ReservationDTO(
                id,
                DATE_TIME,
                schedule.getId(),
                service.getId(),
                true,
                child.getId()
        );

        assertThat(reservationsService.getReservations(null, null, null)).contains(testReservationDTO);
        assertThat(expectedSize).isEqualTo(actualSize);
    }


    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithWrongDate() {

        final int date = 30;
        final int hour = 9;
        final int minutes = 15;
        final LocalDateTime wrongDate = LocalDateTime.of(YEAR, Month.JULY, date, hour, minutes);

        reservationsService.addReservation(
                new ReservationDTO(
                        null,
                        wrongDate,
                        schedule.getId(),
                        service.getId(),
                        true,
                        child.getId()
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddReservationWithWrongTime() {

        final int wrongHour = 16;
        final int wrongMinutes = 30;
        final LocalDateTime dateWithWrongTime = LocalDateTime.of(YEAR, MONTH, DAY, wrongHour, wrongMinutes);

        reservationsService.addReservation(
                new ReservationDTO(
                        null,
                        dateWithWrongTime,
                        schedule.getId(),
                        service.getId(),
                        true,
                        child.getId()
                )
        );
    }

    @Test(expected = ReservationAlreadyExistsException.class)
    @Transactional
    public void testAddReservationWithAlreadyExistedTime() {

        final int reservedHour = 9;
        final int reservedMinutes = 20;

        LocalDateTime existedDateTime = LocalDateTime.of(YEAR, MONTH, DAY, reservedHour, reservedMinutes);

        reservationsService.addReservation(
                new ReservationDTO(
                        null,
                        existedDateTime,
                        schedule.getId(),
                        service.getId(),
                        true,
                        child.getId()
                )
        );
    }

    @Test
    @Transactional
    public void testGetReservations() {
        assertThat(reservationsService.getReservations(null, null, null)).isNotNull().isNotEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testGetReservationsWithWrongDateParametersOne() {
        reservationsService.getReservations(null, DATE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testGetReservationsWithWrongDateParametersTwo() {
        reservationsService.getReservations(DATE, DATE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testGetReservationsWithWrongDateParametersThree() {
        reservationsService.getReservations(DATE, null, DATE);
    }
}