package services;

import appointments.domain.Service;
import appointments.exceptions.ServiceNotFoundException;
import appointments.services.ServicesService;
import appointments.utils.EmulatorInitializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author yanchenko_evgeniya
 */
public class ServicesServiceTest {

    private static EmulatorInitializer emulatorInitializer = new EmulatorInitializer();
    private ServicesService servicesService = new ServicesService();

    @BeforeClass
    public static void initCollections() {
        emulatorInitializer.initAll();
    }

    @Before
    public void initTest() {
        servicesService.getServices().clear();
        servicesService.getServices().addAll(emulatorInitializer.initServices());
    }

    @Test
    public void testAddService() {

        final String serviceName = "Оформление документов";
        final boolean serviceActive = true;
        final int expectedSize = servicesService.getServices().size() + 1;

        final int id = servicesService.addService(serviceName, serviceActive);

        final int actualSize = servicesService.getServices().size();
        final Service testService = new Service(id, serviceName, serviceActive);

        assertThat(servicesService.getServices()).contains(testService);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddServiceWithNullName() {
        servicesService.addService(null, true);
    }

    @Test
    public void testRemoveService() {

        final String serviceName = "Льготное питание";
        final boolean serviceActive = true;
        final int id = servicesService.addService(serviceName, serviceActive);
        final int expectedSize = servicesService.getServices().size() - 1;

        servicesService.removeService(id);

        final int actualSize = servicesService.getServices().size();
        final Service testService = new Service(id, serviceName, serviceActive);

        assertThat(servicesService.getServices()).doesNotContain(testService);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveServiceWithNullId() {
        servicesService.removeService(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    public void testRemoveServiceWithWrongId() {
        servicesService.removeService(Integer.MIN_VALUE);
    }

    @Test
    public void testFindServiceById() {
        final String serviceName = "Прием руководителем управления образования";
        final boolean serviceActive = true;
        final int id = servicesService.addService(serviceName, serviceActive);

        final Service expectedService = new Service(id, serviceName, serviceActive);
        final Service actualService = servicesService.findServiceById(id);

        assertThat(expectedService).isEqualTo(actualService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindServiceByIdWithNullId() {
        servicesService.findServiceById(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    public void testFindServiceByIdWithWrongId() {
        servicesService.findServiceById(Integer.MIN_VALUE);
    }

    @Test
    public void testActivateService() {

        final String serviceName = "Путевка в летний лагерь";
        final boolean serviceActive = false;
        final int id = servicesService.addService(serviceName, serviceActive);

        servicesService.activateService(id);

        final Service testService = servicesService.findServiceById(id);

        assertThat(testService.isActive()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActivateServiceWithNullId() {
        servicesService.activateService(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    public void testActivateServiceWrongId() {
        servicesService.activateService(Integer.MIN_VALUE);
    }

    @Test
    public void testDeactivateService() {

        final String serviceName = "Путевка в детский сад";
        final boolean serviceActive = true;
        final int id = servicesService.addService(serviceName, serviceActive);

        servicesService.deactivateService(id);

        final Service testService = servicesService.findServiceById(id);

        assertThat(testService.isActive()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeactivateServiceWithNullId() {
        servicesService.deactivateService(null);
    }

    @Test(expected = ServiceNotFoundException.class)
    public void testDeactivateServiceWrongId() {
        servicesService.deactivateService(Integer.MIN_VALUE);
    }

    @Test
    public void getServices() {
        assertThat(servicesService.getServices()).isNotNull();
    }
}
