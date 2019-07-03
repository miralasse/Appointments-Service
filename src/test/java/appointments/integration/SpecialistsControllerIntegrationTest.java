package appointments.integration;

import appointments.TestHelper;
import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.dto.ActiveDTO;
import appointments.dto.SpecialistDTO;
import appointments.integration.utils.TestRestClient;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static appointments.utils.Constants.ORGANIZATION_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_NAME_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ORGANIZATION_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_WRONG_NAME_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecialistsControllerIntegrationTest {

    private static final String TEST_SPECIALIST_NAME = "Иванова Ольга Викторовна";
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

    private TestRestClient restClient;

    private String jSessionId;

    private final String endpoint = "/specialists/";
    private final String endpointWithId = "/specialists/{id}";
    private final String endpointForPatch = "/specialists/{id}?_method=patch";

    @Before
    public void setUp() {

        testHelper.refill();
        organization = organizationsRepository.findOneByName(ORGANIZATION_NAME).orElse(null);
        restClient = new TestRestClient(restTemplate);
        jSessionId = testHelper.loginAsAdmin(restClient);
    }

    @Test
    public void testGetAllSpecialists() {

        final ResponseEntity<List<SpecialistDTO>> response = restClient.getList(
            endpoint,
            jSessionId,
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
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
            ).getId();


        final ResponseEntity<SpecialistDTO> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.GET,
            SpecialistDTO.class,
            id
        );

        final SpecialistDTO actualSpecialistDTO = response.getBody();

        assertThat(actualSpecialistDTO.getId()).isEqualTo(id);
        assertThat(actualSpecialistDTO.getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(actualSpecialistDTO.getOrganizationId()).isEqualTo(organization.getId());
        assertThat(actualSpecialistDTO.isActive()).isEqualTo(true);
    }

    @Test
    public void testGetSpecialistByIdWithWrongId() {

        final int id = Integer.MIN_VALUE;

        final ResponseEntity<String> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.GET,
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
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
            )
            .getId();

        final Specialist testSpecialist
            = new Specialist(id, TEST_SPECIALIST_NAME, true, organization);

        final List<Specialist> specialistsBeforeRemoving = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeRemoving.size() - 1;

        assertThat(specialistsBeforeRemoving).contains(testSpecialist);

        final ResponseEntity<String> deleteResponse = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.DELETE,
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

        final ResponseEntity<String> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.DELETE,
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
            = new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId());

        final ResponseEntity<SpecialistDTO> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testSpecialistDTO,
            SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterAdding = specialistsRepository.findAll();
        final int actualSize = specialistsAfterAdding.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(TEST_SPECIALIST_NAME);
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
            = new SpecialistDTO(null, "a", true, organization.getId());

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testSpecialistDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_WRONG_NAME_LENGTH);
    }

    @Test
    public void testPostSpecialistWithNullName() {

        final SpecialistDTO testSpecialistDTO
            = new SpecialistDTO(null, null, true, organization.getId());

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testSpecialistDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_EMPTY_NAME_MESSAGE);
    }

    @Test
    public void testPostSpecialistWithWrongOrganization() {

        int wrongId = Integer.MIN_VALUE;

        final SpecialistDTO testSpecialistDTO
            = new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, wrongId);

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testSpecialistDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains(ORGANIZATION_NOT_FOUND_MESSAGE + wrongId);
    }

    @Test
    public void testPostSpecialistWithNullOrganization() {

        final SpecialistDTO testSpecialistDTO
            = new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, null);

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testSpecialistDTO,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_EMPTY_ORGANIZATION_MESSAGE);
    }

    @Test
    public void testUpdateSpecialistsName() {

        final int id = specialistsService
            .addSpecialist(
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
            ).getId();

        final List<Specialist> specialistsBeforeUpdating = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeUpdating.size();
        final String changedName = "Петрова Екатерина Васильевна";

        final ResponseEntity<SpecialistDTO> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.PUT,
            new SpecialistDTO(id, changedName, true, organization.getId()),
            SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterUpdating = specialistsRepository.findAll();
        final int actualSize = specialistsAfterUpdating.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(actualSpecialist.getName()).isEqualTo(changedName);
        assertThat(actualSpecialist.isActive()).isTrue();
        assertThat(actualSpecialist.getOrganization()).isEqualTo(organization);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testUpdateSpecialistWithWrongName() {

        final int id = specialistsService
            .addSpecialist(
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
            ).getId();

        final String changedName = "b";

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.PUT,
            new SpecialistDTO(id, changedName, true, organization.getId()),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SPECIALIST_WRONG_NAME_LENGTH);
    }


    @Test
    public void testUpdateSpecialistsActive() {

        final int id = specialistsService
            .addSpecialist(
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
            ).getId();

        final List<Specialist> specialistsBeforeUpdating = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeUpdating.size();

        final ResponseEntity<SpecialistDTO> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.PUT,
            new SpecialistDTO(id, TEST_SPECIALIST_NAME, false, organization.getId()),
            SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterUpdating = specialistsRepository.findAll();
        final int actualSize = specialistsAfterUpdating.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(actualSpecialist.getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(actualSpecialist.isActive()).isFalse();
        assertThat(actualSpecialist.getOrganization()).isEqualTo(organization);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testUpdateSpecialistsOrganization() {

        final int id = specialistsService
            .addSpecialist(
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
            ).getId();

        final List<Specialist> specialistsBeforeUpdating = specialistsRepository.findAll();
        final int expectedSize = specialistsBeforeUpdating.size();
        final Organization changedOrganization
            = organizationsRepository.findOneByName("Управление образования г. Старый Оскол").orElse(null);

        final ResponseEntity<SpecialistDTO> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.PUT,
            new SpecialistDTO(id, TEST_SPECIALIST_NAME, true, changedOrganization.getId()),
            SpecialistDTO.class
        );

        final List<Specialist> specialistsAfterUpdating = specialistsRepository.findAll();
        final int actualSize = specialistsAfterUpdating.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(actualSpecialist.getName()).isEqualTo(TEST_SPECIALIST_NAME);
        assertThat(actualSpecialist.isActive()).isTrue();
        assertThat(actualSpecialist.getOrganization()).isEqualTo(changedOrganization);

        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testPatchDeactivateServiceStatus() {

        final int id = specialistsService
            .addSpecialist(
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
            ).getId();

        final ActiveDTO notActiveDTO = new ActiveDTO();
        notActiveDTO.setActive(false);

        final ResponseEntity<ActiveDTO> response = restClient.exchange(
            endpointForPatch,
            jSessionId,
            HttpMethod.POST,
            notActiveDTO,
            ActiveDTO.class,
            id
        );

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualSpecialist.isActive()).isFalse();
    }

    @Test
    public void testPatchActivateServiceStatus() {

        final int id = specialistsService
            .addSpecialist(
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, false, organization.getId())
            ).getId();

        final ActiveDTO activeDTO = new ActiveDTO();
        activeDTO.setActive(true);

        final ResponseEntity<ActiveDTO> response = restClient.exchange(
            endpointForPatch,
            jSessionId,
            HttpMethod.POST,
            activeDTO,
            ActiveDTO.class,
            id
        );

        final Specialist actualSpecialist = specialistsRepository.findById(id).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualSpecialist.isActive()).isTrue();
    }
}
