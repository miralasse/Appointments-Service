package appointments.integration;

import appointments.TestHelper;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.dto.ScheduleDTO;
import appointments.mappers.ScheduleMapper;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import appointments.services.SchedulesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static appointments.utils.Constants.SCHEDULE_EMPTY_END_TIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_INTERVAL_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SERVICES_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SPECIALIST_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_START_TIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_INCORRECT_DATE_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_WRONG_INTERVAL_LENGTH;
import static appointments.utils.Constants.SPECIALIST_NOT_FOUND_MESSAGE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulesControllerIntegrationTest {

    private static final LocalDate DATE = LocalDate.of(2019, Month.AUGUST, 1);
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(13, 0);
    private static final Integer INTERVAL = 15;
    private static final ArrayList<Reservation> RESERVATIONS = new ArrayList<>();
    private static final ArrayList<Long> RESERVATION_IDS = new ArrayList<>();
    private static final String SPECIALIST_NAME = "Специалист 1";

    private Specialist specialist;
    private List<Service> services;
    private List<Integer> serviceIds;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScheduleMapper mapper;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private SpecialistsRepository specialistsRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private SchedulesRepository schedulesRepository;

    @Autowired
    private SchedulesService schedulesService;

    private TestRestClient restClient;

    private String jSessionId;

    private final String endpoint = "/schedules/";
    private final String endpointWithId = "/schedules/{id}";

    @Before
    public void setUp() {

        testHelper.clearAll();
        testHelper.initRoles();
        testHelper.initUsers();
        testHelper.initOrganizations();
        testHelper.initSpecialists();
        testHelper.initServices();
        testHelper.initSchedules();
        specialist = specialistsRepository.findOneByName(SPECIALIST_NAME).orElse(null);
        services = servicesRepository.findAll();
        serviceIds = services
            .stream()
            .map(Service::getId)
            .collect(toList());
        restClient = new TestRestClient(restTemplate);
        jSessionId = testHelper.loginAsAdmin(restClient);
    }

    @Test
    @Transactional
    public void testGetAllSchedules() {

        final ResponseEntity<List<ScheduleDTO>> response = restClient.getList(
            endpoint,
            jSessionId,
            new ParameterizedTypeReference<List<ScheduleDTO>>() { }
        );

        System.out.println(response);

        final List<ScheduleDTO> scheduleDTOs = response.getBody();

        assertThat(scheduleDTOs).hasSize(schedulesRepository.findAll().size());

        final List<Schedule> schedules = mapper.scheduleDTOListToScheduleList(scheduleDTOs);

        assertThat(schedules)
            .anySatisfy(s -> assertThat(s.getSpecialist())
                .isEqualTo(specialistsRepository.findOneByName(TestHelper.SPECIALIST_NAME_FIRST).get()));

        assertThat(schedules)
            .anySatisfy(s -> assertThat(s.getSpecialist())
                .isEqualTo(specialistsRepository.findOneByName(TestHelper.SPECIALIST_NAME_SECOND).get()));

        assertThat(schedules).anySatisfy(s -> assertThat(s.getDate()).isEqualTo(TestHelper.JULY_TWELVE));
        assertThat(schedules).anySatisfy(s -> assertThat(s.getDate()).isEqualTo(TestHelper.JULY_FIFTEEN));
        assertThat(schedules).anySatisfy(s -> assertThat(s.getDate()).isEqualTo(TestHelper.AUGUST_TWELVE));
        assertThat(schedules).anySatisfy(s -> assertThat(s.getDate()).isEqualTo(TestHelper.AUGUST_FIFTEEN));
    }

    @Test
    @Transactional
    public void testGetActiveSchedules() {

        final String url = endpoint + "active";

        final ResponseEntity<List<ScheduleDTO>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ScheduleDTO>>() { }
        );

        final List<ScheduleDTO> scheduleDTOs = response.getBody();

        assertThat(scheduleDTOs).allSatisfy(ScheduleDTO::isActive);
    }

    @Test
    public void testGetScheduleById() {

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

        final ResponseEntity<ScheduleDTO> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.GET,
            ScheduleDTO.class,
            id
        );

        final ScheduleDTO actualScheduleDTO = response.getBody();

        assertThat(actualScheduleDTO.getId()).isEqualTo(id);
        assertThat(actualScheduleDTO.getSpecialistId()).isEqualTo(specialist.getId());
        assertThat(actualScheduleDTO.getDate()).isEqualTo(DATE);
        assertThat(actualScheduleDTO.getServiceIds()).isEqualTo(serviceIds);
        assertThat(actualScheduleDTO.getStartTime()).isEqualTo(START_TIME);
        assertThat(actualScheduleDTO.getEndTime()).isEqualTo(END_TIME);
        assertThat(actualScheduleDTO.getIntervalOfReception()).isEqualTo(INTERVAL);
        assertThat(actualScheduleDTO.getReservationIds()).isEqualTo(RESERVATION_IDS);
        assertThat(actualScheduleDTO.isActive()).isEqualTo(true);
    }


    @Test
    @Transactional
    public void testGetScheduleByIdWithWrongId() {

        final int id = Integer.MIN_VALUE;

        final ResponseEntity<String> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.GET,
            String.class,
            id
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(SCHEDULE_NOT_FOUND_MESSAGE + id);
    }

    @Test
    @Transactional
    public void testDeleteScheduleById() {

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

        final Schedule testSchedule
            = new Schedule(id, specialist, DATE, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        final List<Schedule> schedulesBeforeRemoving = schedulesRepository.findAll();
        final int expectedSize = schedulesBeforeRemoving.size() - 1;

        assertThat(schedulesBeforeRemoving).contains(testSchedule);

        final ResponseEntity<String> deleteResponse = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.DELETE,
            String.class,
            id
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        final List<Schedule> schedulesAfterRemoving = schedulesRepository.findAll();
        final int actualSize = schedulesAfterRemoving.size();

        assertThat(schedulesAfterRemoving).doesNotContain(testSchedule);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    @Transactional
    public void testDeleteScheduleByIdWithWrongId() {

        final int id = Integer.MIN_VALUE;

        final ResponseEntity<String> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.DELETE,
            String.class,
            id
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(SCHEDULE_NOT_FOUND_MESSAGE + id);
    }

    @Test
    @Transactional
    public void testPostSchedule() {

        final List<Schedule> schedulesBeforeAdding = schedulesRepository.findAll();
        final int expectedSize = schedulesBeforeAdding.size() + 1;

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
            null,
            specialist.getId(),
            DATE,
            serviceIds,
            START_TIME,
            END_TIME,
            INTERVAL,
            RESERVATION_IDS,
            true
        );

        final ResponseEntity<ScheduleDTO> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            ScheduleDTO.class
        );

        final List<Schedule> schedulesAfterAdding = schedulesRepository.findAll();
        final int actualSize = schedulesAfterAdding.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getSpecialistId()).isEqualTo(specialist.getId());
        assertThat(response.getBody().getDate()).isEqualTo(DATE);
        assertThat(response.getBody().getServiceIds()).isEqualTo(serviceIds);
        assertThat(response.getBody().getStartTime()).isEqualTo(START_TIME);
        assertThat(response.getBody().getEndTime()).isEqualTo(END_TIME);
        assertThat(response.getBody().getIntervalOfReception()).isEqualTo(INTERVAL);
        assertThat(response.getBody().getReservationIds()).isEqualTo(RESERVATION_IDS);
        assertThat(response.getBody().isActive()).isTrue();

        final Schedule schedule = mapper.scheduleDTOToSchedule(testScheduleDTO);
        schedule.setId(response.getBody().getId());

        assertThat(schedulesAfterAdding).contains(schedule);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    @Transactional
    public void testPostScheduleWithWrongInterval() {

        final int wrongInterval = 2;

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
            null,
            specialist.getId(),
            DATE,
            serviceIds,
            START_TIME,
            END_TIME,
            wrongInterval,
            RESERVATION_IDS,
            true
        );

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_WRONG_INTERVAL_LENGTH);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullInterval() {

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
            null,
            specialist.getId(),
            DATE,
            serviceIds,
            START_TIME,
            END_TIME,
            null,
            RESERVATION_IDS,
            true
        );

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_EMPTY_INTERVAL_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullSpecialist() {

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
            null,
            null,
            DATE,
            serviceIds,
            START_TIME,
            END_TIME,
            INTERVAL,
            RESERVATION_IDS,
            true
        );

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_EMPTY_SPECIALIST_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithWrongSpecialist() {

        final int wrongId = Integer.MIN_VALUE;

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
            null,
            wrongId,
            DATE,
            serviceIds,
            START_TIME,
            END_TIME,
            INTERVAL,
            RESERVATION_IDS,
            true
        );

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains(SPECIALIST_NOT_FOUND_MESSAGE + wrongId);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullDate() {

        final Schedule testSchedule
            = new Schedule(null, specialist, null, services, START_TIME, END_TIME, INTERVAL, RESERVATIONS, true);

        final ScheduleDTO testScheduleDTO = mapper.scheduleToScheduleDto(testSchedule);

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_INCORRECT_DATE_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullServices() {

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
            null,
            specialist.getId(),
            DATE,
            null,
            START_TIME,
            END_TIME,
            INTERVAL,
            RESERVATION_IDS,
            true
        );

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_EMPTY_SERVICES_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullStartDate() {

        final Schedule testSchedule
            = new Schedule(null, specialist, DATE, services, null, END_TIME, INTERVAL, RESERVATIONS, true);

        final ScheduleDTO testScheduleDTO = mapper.scheduleToScheduleDto(testSchedule);

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_EMPTY_START_TIME_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullEndDate() {

        final Schedule testSchedule
            = new Schedule(null, specialist, DATE, services, START_TIME, null, INTERVAL, RESERVATIONS, true);

        final ScheduleDTO testScheduleDTO = mapper.scheduleToScheduleDto(testSchedule);

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testScheduleDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_EMPTY_END_TIME_MESSAGE);
    }

}
