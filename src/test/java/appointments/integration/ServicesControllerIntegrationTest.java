package appointments.integration;

import appointments.TestHelper;
import appointments.domain.Service;
import appointments.dto.ActiveDTO;
import appointments.integration.utils.TestRestClient;
import appointments.repos.ServicesRepository;
import appointments.services.ServicesService;
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

import static appointments.utils.Constants.SERVICE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SERVICE_NULL_NAME_MESSAGE;
import static appointments.utils.Constants.SERVICE_WRONG_LENGTH_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServicesControllerIntegrationTest {

    private static final String TEST_SERVICE_NAME = "Оформление льготного питания";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private ServicesRepository servicesRepository;

    private TestRestClient restClient;

    private String jSessionId;

    private final String endpoint = "/services/";
    private final String endpointWithId = "/services/{id}";
    private final String endpointForPatch = "/services/{id}?_method=patch";


    @Before
    public void setUp() {

        testHelper.refill();
        restClient = new TestRestClient(restTemplate);
        jSessionId = testHelper.loginAsAdmin(restClient);
    }

    @Test
    public void testGetAllServices() {

        final ResponseEntity<List<Service>> response = restClient.getList(
            endpoint,
            jSessionId,
            new ParameterizedTypeReference<List<Service>>() { }
        );

        final List<Service> services = response.getBody();

        assertThat(services).hasSize(servicesRepository.findAll().size());

        assertThat(services).anySatisfy(s -> assertThat(s.getName()).isEqualTo(TestHelper.SERVICE_NAME_FIRST));
        assertThat(services).anySatisfy(s -> assertThat(s.getName()).isEqualTo(TestHelper.SERVICE_NAME_SECOND));
    }

    @Test
    public void testGetActiveServices() {

        final String url = endpoint + "active";

        final ResponseEntity<List<Service>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Service>>() { }
        );

        final List<Service> services = response.getBody();

        assertThat(services).allSatisfy(Service::isActive);
    }

    @Test
    public void testGetServiceById() {


        final int id = servicesService
            .addService(TEST_SERVICE_NAME, true)
            .getId();

        final ResponseEntity<Service> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.GET,
            Service.class,
            id
        );

        final Service actualService = response.getBody();

        assertThat(actualService.getId()).isEqualTo(id);
        assertThat(actualService.getName()).isEqualTo(TEST_SERVICE_NAME);
        assertThat(actualService.isActive()).isEqualTo(true);
    }

    @Test
    public void testGetServiceByIdWithWrongId() {

        final int id = Integer.MIN_VALUE;

        final ResponseEntity<String> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.GET,
            String.class,
            id
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(SERVICE_NOT_FOUND_MESSAGE + id);
    }

    @Test
    public void testDeleteServiceById() {

        final int id = servicesService
            .addService(TEST_SERVICE_NAME, true)
            .getId();

        final Service testService = new Service(id, TEST_SERVICE_NAME, true);
        final List<Service> servicesBeforeRemoving = servicesRepository.findAll();
        final int expectedSize = servicesBeforeRemoving.size() - 1;

        assertThat(servicesBeforeRemoving).contains(testService);

        final ResponseEntity<String> deleteResponse = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.DELETE,
            String.class,
            id
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        final List<Service> servicesAfterRemoving = servicesRepository.findAll();
        final int actualSize = servicesAfterRemoving.size();

        assertThat(servicesAfterRemoving).doesNotContain(testService);
        assertThat(expectedSize).isEqualTo(actualSize);
    }


    @Test
    public void testDeleteServiceByIdWithWrongId() {

        final int id = Integer.MIN_VALUE;

        final ResponseEntity<String> response = restClient.exchange(
            endpointWithId,
            jSessionId,
            HttpMethod.GET,
            String.class,
            id
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(SERVICE_NOT_FOUND_MESSAGE + id);
    }

    @Test
    public void testPostService() {

        final List<Service> servicesBeforeAdding = servicesRepository.findAll();
        final int expectedSize = servicesBeforeAdding.size() + 1;

        final Service testService = new Service(null, TEST_SERVICE_NAME, true);

        final ResponseEntity<Service> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testService,
            Service.class
        );

        final List<Service> servicesAfterAdding = servicesRepository.findAll();
        final int actualSize = servicesAfterAdding.size();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(TEST_SERVICE_NAME);
        assertThat(response.getBody().isActive()).isTrue();

        testService.setId(response.getBody().getId());

        assertThat(servicesAfterAdding).contains(testService);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    public void testPostServiceWithWrongName() {

        final Service testService = new Service(null, "a", true);

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testService,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SERVICE_WRONG_LENGTH_MESSAGE);

    }

    @Test
    public void testPostServiceWithNullName() {

        final Service testService = new Service(null, null, true);

        final ResponseEntity<String> response = restClient.exchange(
            endpoint,
            jSessionId,
            HttpMethod.POST,
            testService,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(SERVICE_NULL_NAME_MESSAGE);
    }

    @Test
    public void testPatchDeactivateServiceStatus() {

        final int id = servicesService
            .addService(TEST_SERVICE_NAME, true)
            .getId();

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

        final Service actualService = servicesRepository.findById(id).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualService.isActive()).isFalse();
    }

    @Test
    public void testPatchActivateServiceStatus() {

        final int id = servicesService
            .addService(TEST_SERVICE_NAME, false)
            .getId();

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

        final Service actualService = servicesRepository.findById(id).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualService.isActive()).isTrue();
    }

}
