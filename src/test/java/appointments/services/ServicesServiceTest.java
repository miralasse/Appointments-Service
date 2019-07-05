package appointments.services;

import appointments.TestHelper;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.exceptions.EntityDependencyException;
import appointments.exceptions.ServiceNotFoundException;
import appointments.repos.SchedulesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.singletonList;
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

    @Autowired
    private SchedulesRepository schedulesRepository;

    @Before
    public void setUp() {
        testHelper.refill();
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

    @Test(expected = EntityDependencyException.class)
    @Transactional
    public void testRemoveServiceUsedInSchedule() {


        final int id = servicesService
                .addService(TEST_SERVICE_NAME, true)
                .getId();

        final Service testService = new Service(id, TEST_SERVICE_NAME, true);

        final Schedule schedule = schedulesRepository.findAll().get(0);
        schedule.setServices(singletonList(testService));

        schedulesRepository.flush();

        servicesService.removeService(id);
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

        servicesService.changeActiveState(id, true);

        final Service testService = servicesService.findServiceById(id);

        assertThat(testService.isActive()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testChangeActivateStateWithNullId() {
        servicesService.changeActiveState(null, true);
    }

    @Test(expected = ServiceNotFoundException.class)
    @Transactional
    public void testActivateServiceWrongId() {
        servicesService.changeActiveState(Integer.MIN_VALUE, true);
    }

    @Test
    @Transactional
    public void testDeactivateService() {


        final int id = servicesService
                .addService(TEST_SERVICE_NAME, true)
                .getId();

        servicesService.changeActiveState(id, false);

        final Service testService = servicesService.findServiceById(id);

        assertThat(testService.isActive()).isFalse();
    }

    @Test
    @Transactional
    public void getServices() {
        assertThat(servicesService.getServices()).isNotNull().isNotEmpty();
    }

    @Test
    @Transactional
    public void getActiveServices() {

        assertThat(servicesService.getActiveServices()).allSatisfy(Service::isActive);
    }
}
