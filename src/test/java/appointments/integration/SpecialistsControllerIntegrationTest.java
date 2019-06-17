package appointments.integration;

import appointments.TestHelper;
import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.dto.ActiveDTO;
import appointments.dto.SpecialistDTO;
import appointments.mappers.SpecialistMapper;
import appointments.repos.OrganizationsRepository;
import appointments.repos.SpecialistsRepository;
import appointments.services.SpecialistsService;
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

import java.util.List;

import static appointments.utils.Constants.ORGANIZATION_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_NAME_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ORGANIZATION_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ROOM_NUMBER_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_WRONG_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_WRONG_ROOM_NUMBER_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecialistsControllerIntegrationTest {

    private static final String TEST_SPECIALIST_NAME = "Иванова Ольга Викторовна";
    private static final String ROOM_NUMBER = "25";
    private final static String ORGANIZATION_NAME = "Управление образования г. Белгород";

    private Organization organization;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private SpecialistsService specialistsService;

    @Autowired
    private SpecialistsRepository specialistsRepository;

    @Autowired
    private SpecialistMapper mapper;

    private final String endpoint = "/specialists/";
    private final String endpointWithId = "/specialists/{id}";
    private final String endpointForPatch = "/specialists/{id}?_method=patch";

    @Before
    public void setUp() {

        testHelper.clearAll();
        testHelper.initServices();
        testHelper.initOrganizations();
        testHelper.initSpecialists();
        organization = organizationsRepository.findOneByName(ORGANIZATION_NAME).orElse(null);
    }

    @Test
    public void testGetAllSpecialists() {

        final ResponseEntity<List<SpecialistDTO>> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SpecialistDTO>>() { }
        );

        final List<SpecialistDTO> specialistDTOs = response.getBody();

        assertThat(specialistDTOs).hasSize(specialistsRepository.findAll().size());

        assertThat(specialistDTOs)
                .anySatisfy(s -> assertThat(s.getName()).isEqualTo(TestHelper.SPECIALIST_NAME_FIRST));

        assertThat(specialistDTOs)
                .anySatisfy(s -> assertThat(s.getName()).isEqualTo(TestHelper.SPECIALIST_NAME_SECOND));

        assertThat(specialistDTOs)
                .anySatisfy(s -> assertThat(s.getName()).isEqualTo(TestHelper.SPECIALIST_NAME_THIRD));
    }

    @Test
    public void testGetActiveSpecialists() {

        final String url = endpoint + "active";

        final ResponseEntity<List<SpecialistDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SpecialistDTO>>() { }
        );

        final List<SpecialistDTO> specialistDTOs = response.getBody();

        assertThat(specialistDTOs).allSatisfy(SpecialistDTO::isActive);
    }

    @Test
    public void testGetSpecialistById() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final SpecialistDTO actualSpecialistDTO
                = restTemplate.getForObject(endpointWithId, SpecialistDTO.class, id);

        assertThat(actualSpecialistDTO.getId()).isEqualTo(id);
        assertThat(actualSpecialistDTO.getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(actualSpecialistDTO.getRoomNumber()).isEqualTo(ROOM_NUMBER);
        assertThat(actualSpecialistDTO.getOrganizationId()).isEqualTo(organization.getId());
        assertThat(actualSpecialistDTO.isActive()).isEqualTo(true);
    }

    @Test
    public void testGetSpecialistByIdWithWrongId() {

        final int id = Integer.MIN_VALUE;

        final ResponseEntity<String> response = restTemplate.exchange(
                endpointWithId,
                HttpMethod.GET,
                null,
                String.class,
                id
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(SPECIALIST_NOT_FOUND_MESSAGE + id);
    }

    @Test
    public void testDeleteSpecialistById() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                )
                .getId();

        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization);

        final List<Specialist> specialistsBeforeRemoving = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeRemoving.size() - 1;

        assertThat(specialistsBeforeRemoving).contains(testSpecialist);

        final ResponseEntity<String> deleteResponse = restTemplate.exchange(
                endpointWithId,
                HttpMethod.DELETE,
                null,
                String.class,
                id
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        final List<Specialist> specialistsAfterRemoving = specialistsRepository.findAll();
        final int actualSize = specialistsAfterRemoving.size();

        assertThat(specialistsAfterRemoving).doesNotContain(testSpecialist);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testDeleteSpecialistByIdWithWrongId() {

        final int id = Integer.MIN_VALUE;

        final ResponseEntity<String> response = restTemplate.exchange(
                endpointWithId,
                HttpMethod.DELETE,
                null,
                String.class,
                id
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(SPECIALIST_NOT_FOUND_MESSAGE + id);
    }

    @Test
    public void testPostSpecialist() {

        final List<Specialist> specialistsBeforeAdding = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeAdding.size() + 1;

        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId());

        final ResponseEntity<SpecialistDTO> response
                = restTemplate.postForEntity(endpoint, testSpecialistDTO, SpecialistDTO.class);

        final List<Specialist> specialistsAfterAdding = specialistsRepository.findAll();
        final int actualSize = specialistsAfterAdding.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(response.getBody().getRoomNumber()).isEqualTo(ROOM_NUMBER);
        assertThat(response.getBody().isActive()).isTrue();
        assertThat(response.getBody().getOrganizationId()).isEqualTo(organization.getId());

        final Specialist testSpecialist = mapper.specialistDTOToSpecialist(testSpecialistDTO);
        testSpecialist.setId(response.getBody().getId());

        assertThat(specialistsAfterAdding).contains(testSpecialist);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testPostSpecialistWithWrongName() {

        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(null, "a", ROOM_NUMBER, true, organization.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testSpecialistDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_WRONG_NAME_LENGTH);
    }

    @Test
    public void testPostSpecialistWithNullName() {

        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(null, null, ROOM_NUMBER, true, organization.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testSpecialistDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_EMPTY_NAME_MESSAGE);
    }

    @Test
    public void testPostSpecialistWithWrongRoomNumber() {

        final String tooLongRoomNumber = "1234567891123456789";

        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(null, TEST_SPECIALIST_NAME, tooLongRoomNumber, true, organization.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testSpecialistDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_WRONG_ROOM_NUMBER_LENGTH);
    }

    @Test
    public void testPostSpecialistWithWrongOrganization() {

        int wrongId = Integer.MIN_VALUE;

        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, wrongId);

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testSpecialistDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains(ORGANIZATION_NOT_FOUND_MESSAGE + wrongId);
    }

    @Test
    public void testPostSpecialistWithNullRoomNumber() {

        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(null, TEST_SPECIALIST_NAME, null, true, organization.getId());

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testSpecialistDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_EMPTY_ROOM_NUMBER_MESSAGE);
    }

    @Test
    public void testPostSpecialistWithNullOrganization() {

        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, null);

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                new HttpEntity<>(testSpecialistDTO),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_EMPTY_ORGANIZATION_MESSAGE);
    }

    @Test
    public void testUpdateSpecialistsName() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final List<Specialist> specialistsBeforeUpdating = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeUpdating.size();
        final String changedName = "Петрова Екатерина Васильевна";

        final ResponseEntity<SpecialistDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                new HttpEntity<>(new SpecialistDTO(id, changedName, ROOM_NUMBER, true, organization.getId())),
                SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterUpdating = specialistsRepository.findAll();
        final int actualSize = specialistsAfterUpdating.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(actualSpecialist.getName()).isEqualTo(changedName);
        assertThat(actualSpecialist.getRoomNumber()).isEqualTo(ROOM_NUMBER);
        assertThat(actualSpecialist.isActive()).isTrue();
        assertThat(actualSpecialist.getOrganization()).isEqualTo(organization);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testUpdateSpecialistWithWrongName() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final String changedName = "b";

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                new HttpEntity<>(new SpecialistDTO(id, changedName, ROOM_NUMBER, true, organization.getId())),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_WRONG_NAME_LENGTH);
    }

    @Test
    public void testUpdateSpecialistsRoomNumber() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final List<Specialist> specialistsBeforeUpdating = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeUpdating.size();
        final String changedRoomNumber = "18";

        final ResponseEntity<SpecialistDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                new HttpEntity<>(
                        new SpecialistDTO(id, TEST_SPECIALIST_NAME, changedRoomNumber, true, organization.getId())),
                SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterUpdating = specialistsRepository.findAll();
        final int actualSize = specialistsAfterUpdating.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(actualSpecialist.getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(actualSpecialist.getRoomNumber()).isEqualTo(changedRoomNumber);
        assertThat(actualSpecialist.isActive()).isTrue();
        assertThat(actualSpecialist.getOrganization()).isEqualTo(organization);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testUpdateSpecialistWithWrongRoomNumber() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final String changedRoomNumber = "12345678911234567";

        final ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                new HttpEntity<>(
                        new SpecialistDTO(id, TEST_SPECIALIST_NAME, changedRoomNumber, true, organization.getId())
                ),
                new ParameterizedTypeReference<String>() { }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_WRONG_ROOM_NUMBER_LENGTH);
    }

    @Test
    public void testUpdateSpecialistsActive() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final List<Specialist> specialistsBeforeUpdating = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeUpdating.size();

        final ResponseEntity<SpecialistDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                new HttpEntity<>(new SpecialistDTO(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, false, organization.getId())),
                SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterUpdating = specialistsRepository.findAll();
        final int actualSize = specialistsAfterUpdating.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(actualSpecialist.getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(actualSpecialist.getRoomNumber()).isEqualTo(ROOM_NUMBER);
        assertThat(actualSpecialist.isActive()).isFalse();
        assertThat(actualSpecialist.getOrganization()).isEqualTo(organization);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testUpdateSpecialistsOrganization() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final List<Specialist> specialistsBeforeUpdating = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeUpdating.size();
        final Organization changedOrganization
                = organizationsRepository.findOneByName("Управление образования г. Старый Оскол").orElse(null);

        final ResponseEntity<SpecialistDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                new HttpEntity<>(
                        new SpecialistDTO(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, changedOrganization.getId())
                ),
                SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterUpdating = specialistsRepository.findAll();
        final int actualSize = specialistsAfterUpdating.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(actualSpecialist.getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(actualSpecialist.getRoomNumber()).isEqualTo(ROOM_NUMBER);
        assertThat(actualSpecialist.isActive()).isTrue();
        assertThat(actualSpecialist.getOrganization()).isEqualTo(changedOrganization);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testPatchDeactivateServiceStatus() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization.getId())
                ).getId();

        final ActiveDTO notActiveDTO = new ActiveDTO();
        notActiveDTO.setActive(false);

        final ResponseEntity<String> response
                = restTemplate.postForEntity(endpointForPatch, new HttpEntity<>(notActiveDTO), String.class, id);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualSpecialist.isActive()).isFalse();
    }

    @Test
    public void testPatchActivateServiceStatus() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, false, organization.getId())
                ).getId();

        final ActiveDTO activeDTO = new ActiveDTO();
        activeDTO.setActive(true);

        final ResponseEntity<String> response
                = restTemplate.postForEntity(endpointForPatch, new HttpEntity<>(activeDTO), String.class, id);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualSpecialist.isActive()).isTrue();
    }
}
