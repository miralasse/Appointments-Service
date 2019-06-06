package appointments.services;

import appointments.domain.Service;
import appointments.exceptions.ServiceNotFoundException;
import appointments.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesServiceTest {

    private static final String TEST_SERVICE_NAME = "Оформление льготного питания";

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private ServicesService servicesService;

    @Before
    public void setUp() {

        testHelper.clearAll();
        testHelper.initServices();
    }

    @Test
    @Transactional
    public void testAddService() {

        final int expectedSize = servicesService.getServices().size() + 1;

        final int id = servicesService
                .addService(TEST_SERVICE_NAME, true)
                .getId();

        final int actualSize = servicesService.getServices().size();
        final Service testService = new Service(id, TEST_SERVICE_NAME, true);

        assertThat(servicesService.getServices()).contains(testService);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddServiceWithNullName() {
        servicesService.addService(null, true);
    }

    @Test
    @Transactional
    public void testRemoveService() {


        final int id = servicesService
                .addService(TEST_SERVICE_NAME, true)
                .getId();
        final int expectedSize = servicesService.getServices().size() - 1;

        servicesService.removeService(id);

        final int actualSize = servicesService.getServices().size();
        final Service testService = new Service(id, TEST_SERVICE_NAME, true);

        assertThat(servicesService.getServices()).doesNotContain(testService);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testRemoveServiceWithNullId() {
        servicesService.removeService(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    @Transactional
    public void testRemoveServiceWithWrongId() {
        servicesService.removeService(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testFindServiceById() {

        final int id = servicesService
                .addService(TEST_SERVICE_NAME, true)
                .getId();

        final Service expectedService = new Service(id, TEST_SERVICE_NAME, true);
        final Service actualService = servicesService.findServiceById(id);

        assertThat(expectedService).isEqualTo(actualService);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindServiceByIdWithNullId() {
        servicesService.findServiceById(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    @Transactional
    public void testFindServiceByIdWithWrongId() {
        servicesService.findServiceById(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testActivateService() {


        final int id = servicesService
                .addService(TEST_SERVICE_NAME, false)
                .getId();

        servicesService.activateService(id);

        final Service testService = servicesService.findServiceById(id);

        assertThat(testService.isActive()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testActivateServiceWithNullId() {
        servicesService.activateService(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    @Transactional
    public void testActivateServiceWrongId() {
        servicesService.activateService(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testDeactivateService() {


        final int id = servicesService
                .addService(TEST_SERVICE_NAME, true)
                .getId();

        servicesService.deactivateService(id);

        final Service testService = servicesService.findServiceById(id);

        assertThat(testService.isActive()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testDeactivateServiceWithNullId() {
        servicesService.deactivateService(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    @Transactional
    public void testDeactivateServiceWrongId() {
        servicesService.deactivateService(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void getServices() {
        assertThat(servicesService.getServices()).isNotNull();
    }
}
