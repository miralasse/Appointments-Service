package appointments.services;

import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import appointments.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulesServiceTest {

    private static final LocalDate DATE = LocalDate.of(2019, Month.AUGUST, 1);
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(13, 0);
    private static final Integer INTERVAL = 15;
    private static final ArrayList<Reservation> RESERVATIONS = new ArrayList<>();
    private static final String SPECIALIST_NAME = "Специалист 1";

    private Specialist specialist;
    private List<Service> services;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private SpecialistsRepository specialistsRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private SchedulesService schedulesService;

    @Before
    public void setUp() {

        testHelper.clearAll();
        testHelper.initOrganizations();
        testHelper.initSpecialists();
        testHelper.initServices();
        testHelper.initSchedules();
        specialist = specialistsRepository.findOneByName(SPECIALIST_NAME).orElse(null);
        services = servicesRepository.findAll();
    }

    @Test
    @Transactional
    public void testAddSchedule() {

        final int expectedSize = schedulesService.getSchedules().size() + 1;

        final long id = schedulesService
                .addSchedule(
                        specialist, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
                )
                .getId();

        final int actualSize = schedulesService.getSchedules().size();
        final Schedule testSchedule
                = new Schedule(id, specialist, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        assertThat(schedulesService.getSchedules()).contains(testSchedule);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullSpecialist() {
        schedulesService.addSchedule(null, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullDate() {
        schedulesService.addSchedule(specialist, null, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithWrongDate() {

        final int expiredYear = 2016;
        schedulesService.addSchedule(
                specialist, LocalDate.of(expiredYear, Month.AUGUST, 1),
                services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullServices() {
        schedulesService.addSchedule(specialist, DATE, null, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullStartTime() {
        schedulesService.addSchedule(specialist, DATE, services, null, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullEndTime() {
        schedulesService.addSchedule(specialist, DATE, services, START_TIME, null, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullInterval() {
        schedulesService.addSchedule(specialist, DATE, services, START_TIME, END_TIME, null, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullReservations() {
        schedulesService.addSchedule(specialist, DATE, services, START_TIME, END_TIME, INTERVAL, null, true);
    }

    @Test
    @Transactional
    public void testFindScheduleById() {

        final long id = schedulesService
                .addSchedule(
                        specialist, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
                )
                .getId();

        final Schedule expectedSchedule
                = new Schedule(id, specialist, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        final Schedule actualSchedule = schedulesService.findScheduleById(id);

        assertThat(expectedSchedule).isEqualTo(actualSchedule);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindScheduleByIdWithNullId() {
        schedulesService.findScheduleById(null);
    }

    @Test(expected = ScheduleNotFoundException.class)
    @Transactional
    public void testFindScheduleByIdWithWrongId() {
        schedulesService.findScheduleById(Long.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testRemoveSchedule() {

        final long id = schedulesService
                .addSchedule(
                        specialist, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
                )
                .getId();

        final int expectedSize = schedulesService.getSchedules().size() - 1;

        schedulesService.removeSchedule(id);

        final int actualSize = schedulesService.getSchedules().size();
        final Schedule testSchedule
                = new Schedule(id, specialist, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        assertThat(schedulesService.getSchedules()).doesNotContain(testSchedule);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testRemoveScheduleWithNullId() {
        schedulesService.removeSchedule(null);
    }

    @Test(expected = ScheduleNotFoundException.class)
    @Transactional
    public void testRemoveScheduleWithWrongId() {
        schedulesService.removeSchedule(Long.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testGetSchedules() {
        assertThat(schedulesService.getSchedules()).isNotNull();
    }
}