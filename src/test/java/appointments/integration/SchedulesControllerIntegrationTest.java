package appointments.integration;

import appointments.TestHelper;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.dto.ScheduleDTO;
import appointments.dto.ServiceSimpleDTO;
import appointments.dto.SpecialistSimpleDTO;
import appointments.integration.utils.RestPageImpl;
import appointments.integration.utils.TestRestClient;
import appointments.mappers.ScheduleMapper;
import appointments.repos.ReservationsRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import static appointments.utils.Constants.SCHEDULE_EMPTY_ROOM_NUMBER_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SERVICES_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SPECIALIST_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_START_TIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_INCORRECT_DATE_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_IS_ALREADY_USED;
import static appointments.utils.Constants.SCHEDULE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_WRONG_INTERVAL_LENGTH;
import static appointments.utils.Constants.SCHEDULE_WRONG_ROOM_NUMBER_LENGTH;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulesControllerIntegrationTest {

    private static final int YEAR = 2019;
    private static final LocalDate DATE = LocalDate.of(YEAR, Month.AUGUST, 1);
    private static final LocalDate ANOTHER_DATE = LocalDate.of(YEAR, Month.AUGUST, 15);
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(13, 0);
    private static final Integer INTERVAL = 15;
    private static final ArrayList<Reservation> RESERVATIONS = new ArrayList<>();
    private static final ArrayList<Long> RESERVATION_IDS = new ArrayList<>();
    private static final String SPECIALIST_NAME = "Специалист 1";
    private static final String ROOM_NUMBER = "25";
    private static final int PAGE_SIZE = 5;
    private static final int INITIAL_PAGE = 0;
    private static final PageRequest PAGE_REQUEST = PageRequest.of(INITIAL_PAGE, PAGE_SIZE);

    private Specialist specialist;
    private List<Service> services;
    private SpecialistSimpleDTO specialistSimpleDTO;
    private List<ServiceSimpleDTO> serviceSimpleDTOs;

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
    private ReservationsRepository reservationsRepository;

    @Autowired
    private SchedulesService schedulesService;

    private TestRestClient restClient;

    private String jSessionId;

    private final String endpoint = "/schedules/";
    private final String endpointWithId = "/schedules/{id}";

    @Before
    public void setUp() {

        testHelper.refill();
        specialist = specialistsRepository.findOneByName(SPECIALIST_NAME).orElse(null);
        specialistSimpleDTO = new SpecialistSimpleDTO(specialist.getId(), specialist.getName());
        services = servicesRepository.findAll();
        serviceSimpleDTOs = services
                .stream()
                .map(service -> new ServiceSimpleDTO(service.getId(), service.getName()))
                .collect(toList());
        restClient = new TestRestClient(restTemplate);
        jSessionId = testHelper.loginAsAdmin(restClient);
    }

    @Test
    @Transactional
    public void testGetAllSchedules() {

        String url = endpoint + "?date={date}&page={page}&size={size}";

        final ResponseEntity<RestPageImpl<ScheduleDTO>> response = restClient.getPage(
                url,
                jSessionId,
                new ParameterizedTypeReference<RestPageImpl<ScheduleDTO>>() { },
                ANOTHER_DATE,
                INITIAL_PAGE,
                PAGE_SIZE
        );

        final Page<ScheduleDTO> scheduleDTOs = response.getBody();

        assertThat(scheduleDTOs.getTotalElements()).isEqualTo(
                schedulesRepository
                        .findAllByDateOrderByStartTime(PAGE_REQUEST, ANOTHER_DATE)
                        .getTotalElements()
                );

        assertThat(scheduleDTOs.getContent())
                .anySatisfy(
                        s -> assertThat(s.getSpecialist().getName()).isEqualTo(
                                specialistsRepository
                                        .findOneByName(TestHelper.SPECIALIST_NAME_SECOND)
                                        .get()
                                        .getName()
                        )
                );

        assertThat(scheduleDTOs.getContent())
                .anySatisfy(s -> assertThat(s.getDate()).isEqualTo(TestHelper.AUGUST_FIFTEEN));

    }

    @Test
    public void testGetScheduleById() {

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

        final ResponseEntity<ScheduleDTO> response = restClient.exchange(
                endpointWithId,
                jSessionId,
                HttpMethod.GET,
                ScheduleDTO.class,
                id
        );

        final ScheduleDTO actualScheduleDTO = response.getBody();

        assertThat(actualScheduleDTO.getId()).isEqualTo(id);
        assertThat(actualScheduleDTO.getSpecialist()).isEqualTo(specialistSimpleDTO);
        assertThat(actualScheduleDTO.getDate()).isEqualTo(DATE);
        assertThat(actualScheduleDTO.getServices()).isEqualTo(serviceSimpleDTOs);
        assertThat(actualScheduleDTO.getStartTime()).isEqualTo(START_TIME);
        assertThat(actualScheduleDTO.getEndTime()).isEqualTo(END_TIME);
        assertThat(actualScheduleDTO.getInterval()).isEqualTo(INTERVAL);
        assertThat(actualScheduleDTO.getReservationIds()).isEqualTo(RESERVATION_IDS);
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

        final Schedule testSchedule = new Schedule(
                id,
                specialist,
                ROOM_NUMBER,
                DATE,
                services,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATIONS
        );

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
    public void testDeleteScheduleUsedInReservation() {

        final Reservation reservation = reservationsRepository.findAll().get(0);

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
                                singletonList(reservation.getId())
                        )
                ).getId();

        final Schedule testSchedule = new Schedule(
                id,
                specialist,
                ROOM_NUMBER,
                DATE,
                services,
                START_TIME,
                END_TIME,
                INTERVAL,
                singletonList(reservation)
        );

        reservation.setSchedule(testSchedule);
        reservationsRepository.flush();

        assertThat(schedulesRepository.findAll()).contains(testSchedule);

        final ResponseEntity<String> deleteResponse = restClient.exchange(
                endpointWithId,
                jSessionId,
                HttpMethod.DELETE,
                String.class,
                id
        );

        assertThat(schedulesRepository.findAll()).contains(testSchedule);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(deleteResponse.getBody()).isEqualTo(SCHEDULE_IS_ALREADY_USED);
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
                specialistSimpleDTO,
                ROOM_NUMBER,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
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
        assertThat(response.getBody().getSpecialist()).isEqualTo(specialistSimpleDTO);
        assertThat(response.getBody().getDate()).isEqualTo(DATE);
        assertThat(response.getBody().getServices()).isEqualTo(serviceSimpleDTOs);
        assertThat(response.getBody().getStartTime()).isEqualTo(START_TIME);
        assertThat(response.getBody().getEndTime()).isEqualTo(END_TIME);
        assertThat(response.getBody().getInterval()).isEqualTo(INTERVAL);
        assertThat(response.getBody().getReservationIds()).isEqualTo(RESERVATION_IDS);

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
                specialistSimpleDTO,
                ROOM_NUMBER,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                wrongInterval,
                RESERVATION_IDS
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
                specialistSimpleDTO,
                ROOM_NUMBER,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                null,
                RESERVATION_IDS
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
    public void testPostScheduleWithWrongRoomNumber() {

        final String tooLongRoomNumber = "1234567891123456789";

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
                null,
                specialistSimpleDTO,
                tooLongRoomNumber,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
        );

        final ResponseEntity<String> response = restClient.exchange(
                endpoint,
                jSessionId,
                HttpMethod.POST,
                testScheduleDTO,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_WRONG_ROOM_NUMBER_LENGTH);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullRoomNumber() {

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
                null,
                specialistSimpleDTO,
                null,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
        );

        final ResponseEntity<String> response = restClient.exchange(
                endpoint,
                jSessionId,
                HttpMethod.POST,
                testScheduleDTO,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SCHEDULE_EMPTY_ROOM_NUMBER_MESSAGE);
    }


    @Test
    @Transactional
    public void testPostScheduleWithNullSpecialist() {

        final ScheduleDTO testScheduleDTO = new ScheduleDTO(
                null,
                null,
                ROOM_NUMBER,
                DATE,
                serviceSimpleDTOs,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
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
    public void testPostScheduleWithNullDate() {

        final Schedule testSchedule = new Schedule(
                null,
                specialist,
                ROOM_NUMBER,
                null,
                services,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATIONS
        );

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
                specialistSimpleDTO,
                ROOM_NUMBER,
                DATE,
                null,
                START_TIME,
                END_TIME,
                INTERVAL,
                RESERVATION_IDS
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

        final Schedule testSchedule = new Schedule(
                null,
                specialist,
                ROOM_NUMBER,
                DATE,
                services,
                null,
                END_TIME,
                INTERVAL,
                RESERVATIONS
        );

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

        final Schedule testSchedule = new Schedule(
                null,
                specialist,
                ROOM_NUMBER,
                DATE,
                services,
                START_TIME,
                null,
                INTERVAL,
                RESERVATIONS
        );

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
