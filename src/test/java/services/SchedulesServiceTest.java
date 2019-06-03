package services;

import appointments.domain.Organization;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.services.SchedulesService;
import appointments.utils.EmulatorInitializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author yanchenko_evgeniya
 */
public class SchedulesServiceTest {

    private static final Specialist SPECIALIST = new Specialist(
            4, "Агаев Михаил Вячеславович", "44", true,
            new Organization(
                    3, "Управление культуры Белгородской области",
                    "г. Белгород, Гражданский пр-т, 40", "+7(4722)27‑72-52"
            )
    );
    private static final LocalDate DATE = LocalDate.of(2019, Month.AUGUST, 1);
    private static final List<Service> SERVICES = Arrays.asList(new Service(3, "Льготное питание", true));
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(13, 0);
    private static final Duration INTERVAL = Duration.of(15, ChronoUnit.MINUTES);
    private static final ArrayList<Reservation> RESERVATIONS = new ArrayList<>();

    private static EmulatorInitializer emulatorInitializer = new EmulatorInitializer();
    private SchedulesService schedulesService = new SchedulesService();


    @BeforeClass
    public static void initCollections() {
        emulatorInitializer.initAll();
    }

    @Before
    public void initTest() {

        schedulesService.getSchedules().clear();
        schedulesService.getSchedules().addAll(emulatorInitializer.initSchedules());
    }

    @Test
    public void testAddSchedule() {

        final int expectedSize = schedulesService.getSchedules().size() + 1;

        final long id = schedulesService.addSchedule(
                        SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
        );

        final int actualSize = schedulesService.getSchedules().size();
        final Schedule testSchedule
                = new Schedule(id, SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        assertThat(schedulesService.getSchedules()).contains(testSchedule);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithNullSpecialist() {
        schedulesService.addSchedule(null, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithNullDate() {
        schedulesService.addSchedule(SPECIALIST, null, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithWrongDate() {
        final int expiredYear = 2016;
        schedulesService.addSchedule(
                SPECIALIST, LocalDate.of(expiredYear, Month.AUGUST, 1),
                SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithNullServices() {
        schedulesService.addSchedule(SPECIALIST, DATE, null, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithNullStartTime() {
        schedulesService.addSchedule(SPECIALIST, DATE, SERVICES, null, END_TIME, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithNullEndTime() {
        schedulesService.addSchedule(SPECIALIST, DATE, SERVICES, START_TIME, null, INTERVAL, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithNullInterval() {
        schedulesService.addSchedule(SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, null, RESERVATIONS, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduleWithNullReservations() {
        schedulesService.addSchedule(SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, null, true);
    }

    @Test
    public void testFindScheduleById() {

        final long id = schedulesService.addSchedule(
                SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
        );

        final Schedule expectedSchedule
                = new Schedule(id, SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        final Schedule actualSchedule = schedulesService.findScheduleById(id);

        assertThat(expectedSchedule).isEqualTo(actualSchedule);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindScheduleByIdWithNullId() {
        schedulesService.findScheduleById(null);
    }

    @Test(expected = ScheduleNotFoundException.class)
    public void testFindScheduleByIdWithWrongId() {
        schedulesService.findScheduleById(Long.MIN_VALUE);
    }

    @Test
    public void testRemoveSchedule() {

        final long id = schedulesService.addSchedule(
                SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true
        );
        final int expectedSize = schedulesService.getSchedules().size() - 1;

        schedulesService.removeSchedule(id);

        final int actualSize = schedulesService.getSchedules().size();
        final Schedule testSchedule
                = new Schedule(id, SPECIALIST, DATE, SERVICES, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        assertThat(schedulesService.getSchedules()).doesNotContain(testSchedule);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveScheduleWithNullId() {
        schedulesService.removeSchedule(null);
    }

    @Test(expected = ScheduleNotFoundException.class)
    public void testRemoveScheduleWithWrongId() {
        schedulesService.removeSchedule(Long.MIN_VALUE);
    }

    @Test
    public void testGetSchedules() {
        assertThat(schedulesService.getSchedules()).isNotNull();
    }
}