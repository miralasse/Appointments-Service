package appointments.integration;

import appointments.TestHelper;
import appointments.domain.Child;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.dto.ReservationDTO;
import appointments.mappers.ReservationMapper;
import appointments.repos.ChildrenRepository;
import appointments.repos.ReservationsRepository;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import appointments.services.ReservationsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static appointments.utils.Constants.CHILD_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_CHILD_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_SCHEDULE_MESSAGE;
import static appointments.utils.Constants.RESERVATION_EMPTY_SERVICE_MESSAGE;
import static appointments.utils.Constants.RESERVATION_INCORRECT_DATETIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SERVICE_NOT_FOUND_MESSAGE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationsControllerIntegrationTest {

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
    private TestRestTemplate restTemplate;

    @Autowired
    private ReservationMapper mapper;

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
    private ReservationsRepository reservationsRepository;

    @Autowired
    private ReservationsService reservationsService;

    private final String endpoint = "/reservations/";

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
    public void testGetAllReservations() {

        final ResponseEntity<List<ReservationDTO>> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationDTO>>() { }
        );

        List<ReservationDTO> reservationDTOs = response.getBody();

        assertThat(reservationDTOs).hasSize(reservationsRepository.findAll().size());

        assertThat(reservationDTOs)
                .anySatisfy(r -> assertThat(r.getDateTime()).isEqualTo(TestHelper.FIRST_RESERVATION_HOUR));

        assertThat(reservationDTOs)
                .anySatisfy(r -> assertThat(r.getDateTime()).isEqualTo(TestHelper.SECOND_RESERVATION_HOUR));

        assertThat(reservationDTOs)
                .anySatisfy(r -> assertThat(r.getDateTime()).isEqualTo(TestHelper.THIRD_RESERVATION_HOUR));

        assertThat(reservationDTOs)
                .anySatisfy(r -> assertThat(r.getDateTime()).isEqualTo(TestHelper.FOURTH_RESERVATION_HOUR));

        assertThat(reservationDTOs)
                .anySatisfy(r -> assertThat(r.getDateTime()).isEqualTo(TestHelper.FIFTH_RESERVATION_HOUR));
    }

    @Test
    @Transactional
    public void testGetReservationsByDate() {

        final String url = endpoint + "?date={date}";

        final ResponseEntity<List<ReservationDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationDTO>>() { },
                DATE
        );

        final List<ReservationDTO> reservationDTOs = response.getBody();

        assertThat(reservationDTOs).isNotNull();

        final List<Long> ids = reservationDTOs
                .stream()
                .map(ReservationDTO::getId)
                .collect(toList());

        assertThat(
                reservationsRepository
                        .findAll()
                        .stream()
                        .filter(r -> !ids.contains(r.getId()) && DATE.equals(r.getDateTime().toLocalDate()))
        ).isEmpty();

    }

    @Test
    @Transactional
    public void testGetReservationsByPeriod() {

        final String url = endpoint + "?startDate={startDate}&endDate={endDate}";

        final LocalDate startDate = DATE;
        final int daysToAdd = 5;
        final LocalDate endDate = LocalDate.of(YEAR, MONTH, DAY + daysToAdd);

        final ResponseEntity<List<ReservationDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationDTO>>() { },
                startDate,
                endDate
        );

        List<ReservationDTO> reservationDTOs = response.getBody();

        assertThat(reservationDTOs).isNotNull();

        final List<Long> ids = reservationDTOs
                .stream()
                .map(ReservationDTO::getId)
                .collect(toList());

        assertThat(
                reservationsRepository
                        .findAll()
                        .stream()
                        .filter(
                                r -> !ids.contains(r.getId())
                                        && (!r.getDateTime().toLocalDate().isAfter(endDate)
                                        && !r.getDateTime().toLocalDate().isBefore(startDate))
                        )
        ).isEmpty();
    }

    @Test
    @Transactional
    public void testGetReservationsWithWrongParameters() {

        String url = endpoint + "?date={date}&endDate={endDate}";

        final int daysToAdd = 5;
        LocalDate endDate = LocalDate.of(YEAR, MONTH, DAY + daysToAdd);

        final ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() { },
                DATE,
                endDate
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Transactional
    public void testGetReservationById() {

        final String url = endpoint + "{id}";

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

        ReservationDTO actualReservationDTO = restTemplate.getForObject(url, ReservationDTO.class, id);

        assertThat(actualReservationDTO.getId()).isEqualTo(id);
        assertThat(actualReservationDTO.getDateTime()).isEqualTo(DATE_TIME);
        assertThat(actualReservationDTO.getScheduleId()).isEqualTo(schedule.getId());
        assertThat(actualReservationDTO.getServiceId()).isEqualTo(service.getId());
        assertThat(actualReservationDTO.isActive()).isEqualTo(true);
        assertThat(actualReservationDTO.getChildId()).isEqualTo(child.getId());
    }

    @Test
    @Transactional
    public void testPostSchedule() {

        final List<Reservation> reservationsBeforeAdding = reservationsRepository.findAll();
        final int expectedSize = reservationsBeforeAdding.size() + 1;

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, DATE_TIME, schedule.getId(), service.getId(), true, child.getId());

        final ResponseEntity<ReservationDTO> response
                = restTemplate.postForEntity(endpoint, testReservationDTO, ReservationDTO.class);

        final List<Reservation> reservationsAfterAdding = reservationsRepository.findAll();
        final int actualSize = reservationsAfterAdding.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getDateTime()).isEqualTo(DATE_TIME);
        assertThat(response.getBody().getScheduleId()).isEqualTo(schedule.getId());
        assertThat(response.getBody().getServiceId()).isEqualTo(service.getId());
        assertThat(response.getBody().getChildId()).isEqualTo(child.getId());
        assertThat(response.getBody().isActive()).isTrue();

        Reservation reservation = mapper.reservationDTOToReservation(testReservationDTO);
        reservation.setId(response.getBody().getId());

        assertThat(reservationsAfterAdding).contains(reservation);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullDateTime() {

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, null, schedule.getId(), service.getId(), true, child.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testReservationDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(RESERVATION_INCORRECT_DATETIME_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullSchedule() {

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, DATE_TIME, null, service.getId(), true, child.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testReservationDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(RESERVATION_EMPTY_SCHEDULE_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithWrongSchedule() {

        final long wrongId = Long.MIN_VALUE;

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, DATE_TIME, wrongId, service.getId(), true, child.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testReservationDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains(SCHEDULE_NOT_FOUND_MESSAGE + wrongId);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullService() {

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, DATE_TIME, schedule.getId(), null, true, child.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testReservationDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(RESERVATION_EMPTY_SERVICE_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithWrongService() {

        final int wrongId = Integer.MIN_VALUE;

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, DATE_TIME, schedule.getId(), wrongId, true, child.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testReservationDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains(SERVICE_NOT_FOUND_MESSAGE + wrongId);
    }

    @Test
    @Transactional
    public void testPostScheduleWithNullChild() {

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, DATE_TIME, schedule.getId(), service.getId(), true, null);

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testReservationDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(RESERVATION_EMPTY_CHILD_MESSAGE);
    }

    @Test
    @Transactional
    public void testPostScheduleWithWrongChild() {

        final int wrongId = Integer.MIN_VALUE;

        final ReservationDTO testReservationDTO
                = new ReservationDTO(null, DATE_TIME, schedule.getId(), service.getId(), true, wrongId);

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testReservationDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains(CHILD_NOT_FOUND_MESSAGE + wrongId);
    }


}
