package appointments.services;

import appointments.TestHelper;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.dto.ScheduleDTO;
import appointments.exceptions.ScheduleNotFoundException;
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
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
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
    private static final ArrayList<Long> RESERVATION_IDS = new ArrayList<>();
    private static final String SPECIALIST_NAME = "Специалист 1";

    private Specialist specialist;
    private List<Service> services;
    private List<Integer> serviceIds;

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

        testHelper.refill();
        specialist = specialistsRepository.findOneByName(SPECIALIST_NAME).orElse(null);
        services = servicesRepository.findAll();
        serviceIds = services
                .stream()
                .map(Service::getId)
                .collect(toList());
    }

    @Test
    @Transactional
    public void testAddSchedule() {

        final int expectedSize = schedulesService.getSchedules().size() + 1;

        final long id = schedulesService
                .addSchedule(
                        new ScheduleDTO(
                                null,
                                specialist.getId(),
                                DATE,
                                serviceIds,
                                START_TIME,
                                END_TIME,
                                INTERVAL,
                                RESERVATION_IDS,
                                true
                        )
                ).getId();

        final int actualSize = schedulesService.getSchedules().size();
        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
                id,
                specialist.getId(),
                DATE,
                serviceIds,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS,
                true
        );

        assertThat(schedulesService.getSchedules()).contains(testScheduleDTO);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullSpecialist() {

        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        null,
                        DATE,
                        serviceIds,
                        START_TIME,
                        END_TIME,
                        INTERVAL,
                        RESERVATION_IDS,
                        true
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullDate() {
        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        specialist.getId(),
                        null,
                        serviceIds,
                        START_TIME,
                        END_TIME,
                        INTERVAL,
                        RESERVATION_IDS,
                        true
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithWrongDate() {

        final int expiredYear = 2016;
        final LocalDate expiredDate = LocalDate.of(expiredYear, Month.AUGUST, 1);
        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        specialist.getId(),
                        expiredDate,
                        serviceIds,
                        START_TIME,
                        END_TIME,
                        INTERVAL,
                        RESERVATION_IDS,
                        true
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullServices() {

        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        specialist.getId(),
                        DATE,
                        null,
                        START_TIME,
                        END_TIME,
                        INTERVAL,
                        RESERVATION_IDS,
                        true
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullStartTime() {

        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        specialist.getId(),
                        DATE,
                        serviceIds,
                        null,
                        END_TIME,
                        INTERVAL,
                        RESERVATION_IDS,
                        true
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullEndTime() {

        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        specialist.getId(),
                        DATE,
                        serviceIds,
                        START_TIME,
                        null,
                        INTERVAL,
                        RESERVATION_IDS,
                        true
                )
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithNullInterval() {

        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        specialist.getId(),
                        DATE,
                        serviceIds,
                        START_TIME,
                        END_TIME,
                        null,
                        RESERVATION_IDS,
                        true
                )
        );
    }

    @Test
    @Transactional
    public void testFindScheduleById() {

        final long id = schedulesService
                .addSchedule(
                        new ScheduleDTO(
                                null,
                                specialist.getId(),
                                DATE,
                                serviceIds,
                                START_TIME,
                                END_TIME,
                                INTERVAL,
                                RESERVATION_IDS,
                                true
                        )
                ).getId();

        final ScheduleDTO expectedScheduleDTO = new ScheduleDTO(
                id,
                specialist.getId(),
                DATE,
                serviceIds,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS,
                true
        );

        final ScheduleDTO actualScheduleDTO = schedulesService.findScheduleById(id);

        assertThat(expectedScheduleDTO).isEqualTo(actualScheduleDTO);
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
                        new ScheduleDTO(
                                null,
                                specialist.getId(),
                                DATE,
                                serviceIds,
                                START_TIME,
                                END_TIME,
                                INTERVAL,
                                RESERVATION_IDS,
                                true
                        )
                ).getId();

        final int expectedSize = schedulesService.getSchedules().size() - 1;

        schedulesService.removeSchedule(id);

        final int actualSize = schedulesService.getSchedules().size();
        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
                id,
                specialist.getId(),
                DATE,
                serviceIds,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS,
                true
        );

        assertThat(schedulesService.getSchedules()).doesNotContain(testScheduleDTO);
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
        assertThat(schedulesService.getSchedules()).isNotNull().isNotEmpty();
    }

    @Test
    @Transactional
    public void getActiveSchedules() {

        assertThat(schedulesService.getActiveSchedules()).allSatisfy(ScheduleDTO::isActive);
    }
}