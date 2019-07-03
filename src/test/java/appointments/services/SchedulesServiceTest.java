package appointments.services;

import appointments.TestHelper;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.dto.ScheduleDTO;
import appointments.dto.ServiceSimpleDTO;
import appointments.dto.SpecialistSimpleDTO;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

    private static final int YEAR = 2019;
    private static final LocalDate DATE = LocalDate.of(YEAR, Month.AUGUST, 1);
    private static final LocalDate ANOTHER_DATE = LocalDate.of(YEAR, Month.AUGUST, 15);
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(13, 0);
    private static final Integer INTERVAL = 15;
    private static final ArrayList<Long> RESERVATION_IDS = new ArrayList<>();
    private static final String SPECIALIST_NAME = "Специалист 1";
    private static final String ROOM_NUMBER = "25";
    private static final int PAGE_SIZE = 5;
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, PAGE_SIZE);

    private Specialist specialist;
    private SpecialistSimpleDTO specialistSimpleDTO;
    private List<ServiceSimpleDTO> serviceSimpleDTOs;

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
        specialistSimpleDTO = new SpecialistSimpleDTO(specialist.getId(), specialist.getName());
        List<Service> services = servicesRepository.findAll();
        serviceSimpleDTOs = services
                .stream()
                .map(service -> new ServiceSimpleDTO(service.getId(), service.getName()))
                .collect(toList());
    }

    @Test
    @Transactional
    public void testAddSchedule() {

        final int expectedSize = schedulesService.getSchedules(PAGE_REQUEST, DATE).getContent().size() + 1;

        final long id = schedulesService
                .addSchedule(
                        new ScheduleDTO(
                                null,
                                specialistSimpleDTO,
                                ROOM_NUMBER,
                                DATE,
                                serviceSimpleDTOs,
                                START_TIME,
                                END_TIME,
                                INTERVAL,
                                RESERVATION_IDS
                        )
                ).getId();

        final int actualSize = schedulesService.getSchedules(PAGE_REQUEST, DATE).getContent().size();
        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
                id,
                specialistSimpleDTO,
                ROOM_NUMBER,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
        );

        assertThat(schedulesService.getSchedules(PAGE_REQUEST, DATE)).contains(testScheduleDTO);

        assertThat(expectedSize).isEqualTo(actualSize);
    }


    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddScheduleWithWrongDate() {

        final int expiredYear = 2016;
        final LocalDate expiredDate = LocalDate.of(expiredYear, Month.AUGUST, 1);
        schedulesService.addSchedule(
                new ScheduleDTO(
                        null,
                        specialistSimpleDTO,
                        ROOM_NUMBER,
                        expiredDate,
                        serviceSimpleDTOs,
                        START_TIME,
                        END_TIME,
                        INTERVAL,
                        RESERVATION_IDS
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
                                specialistSimpleDTO,
                                ROOM_NUMBER,
                                DATE,
                                serviceSimpleDTOs,
                                START_TIME,
                                END_TIME,
                                INTERVAL,
                                RESERVATION_IDS
                        )
                ).getId();

        final ScheduleDTO expectedScheduleDTO = new ScheduleDTO(
                id,
                specialistSimpleDTO,
                ROOM_NUMBER,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
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
                                specialistSimpleDTO,
                                ROOM_NUMBER,
                                DATE,
                                serviceSimpleDTOs,
                                START_TIME,
                                END_TIME,
                                INTERVAL,
                                RESERVATION_IDS
                        )
                ).getId();

        final int expectedSize = schedulesService.getSchedules(PAGE_REQUEST, DATE).getContent().size() - 1;

        schedulesService.removeSchedule(id);

        final int actualSize = schedulesService.getSchedules(PAGE_REQUEST, DATE).getContent().size();
        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
                id,
                specialistSimpleDTO,
                ROOM_NUMBER,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
        );

        assertThat(schedulesService.getSchedules(PAGE_REQUEST, DATE)).doesNotContain(testScheduleDTO);
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

        assertThat(schedulesService.getSchedules(PAGE_REQUEST, ANOTHER_DATE)).hasSize(1);
    }

}