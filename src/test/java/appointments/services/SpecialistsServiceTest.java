package appointments.services;

import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.repos.OrganizationsRepository;
import appointments.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author yanchenko_evgeniya
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpecialistsServiceTest {

    private static final String TEST_SPECIALIST_NAME = "Иванова Ольга Викторовна";
    private static final String ROOM_NUMBER = "25";
    private final static String ORGANIZATION_NAME = "Управление образования г. Белгород";

    private Organization organization;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private SpecialistsService specialistsService;

    @Autowired
    public void setTestHelper(TestHelper testHelper) {
        this.testHelper = testHelper;
    }

    @Before
    public void setUp() {

        testHelper.clearAll();
        testHelper.initOrganizations();
        testHelper.initSpecialists();
        organization = organizationsRepository.findOneByName(ORGANIZATION_NAME).orElse(null);
    }

    @Test
    @Transactional
    public void testAddSpecialist() {

        final int expectedSize = specialistsService.getSpecialists().size() + 1;

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();

        final int actualSize = specialistsService.getSpecialists().size();
        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization);

        assertThat(specialistsService.getSpecialists()).contains(testSpecialist);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddSpecialistWithNullName() {
        specialistsService.addSpecialist(null, ROOM_NUMBER, true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddSpecialistWithEmptyName() {
        specialistsService.addSpecialist("", ROOM_NUMBER, true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddSpecialistWithNullRoomNumber() {
        specialistsService.addSpecialist(TEST_SPECIALIST_NAME, null, true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddSpecialistWithEmptyRoomNumber() {
        specialistsService.addSpecialist(TEST_SPECIALIST_NAME, "", true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testAddSpecialistWithNullOrganization() {
        specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, null);
    }

    @Test
    @Transactional
    public void testEditSpecialistName() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();

        final String changedName = "Петрова Екатерина Васильевна";

        specialistsService.editSpecialist(id, changedName, ROOM_NUMBER, true, organization);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist = new Specialist(id, changedName, ROOM_NUMBER, true, organization);

        assertThat(actualSpecialist.getName()).isEqualTo(testSpecialist.getName());
    }

    @Test
    @Transactional
    public void testEditSpecialistRoomNumber() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();

        final String changedRoomNumber = "18";

        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, changedRoomNumber, true, organization);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, changedRoomNumber,  true, organization);

        assertThat(actualSpecialist.getRoomNumber()).isEqualTo(testSpecialist.getRoomNumber());
    }

    @Test
    @Transactional
    public void testEditSpecialistActive() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();

        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, false, organization);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, false, organization);

        assertThat(actualSpecialist.isActive()).isEqualTo(testSpecialist.isActive());
    }

    @Test
    @Transactional
    public void testEditSpecialistOrganization() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();

        final Organization changedOrganization
                = organizationsRepository.findOneByName("Управление образования г. Старый Оскол").orElse(null);

        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, changedOrganization);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, changedOrganization);

        assertThat(actualSpecialist.getOrganization()).isEqualTo(testSpecialist.getOrganization());
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testEditSpecialistWithNullId() {
        specialistsService.editSpecialist(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization);
    }

    @Test(expected = SpecialistNotFoundException.class)
    @Transactional
    public void testEditSpecialistWithWrongId() {
        specialistsService.editSpecialist(Integer.MIN_VALUE, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testEditSpecialistWithNullName() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();
        specialistsService.editSpecialist(id, null, ROOM_NUMBER, true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testEditSpecialistWithEmptyName() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();
        specialistsService.editSpecialist(id, "", ROOM_NUMBER, true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testEditSpecialistWithNullRoomNumber() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();
        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, null, true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testEditSpecialistWithEmptyRoomNumber() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();
        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, "", true, organization);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testEditSpecialistWithNullOrganization() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();
        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, null);
    }

    @Test
    @Transactional
    public void testRemoveSpecialist() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();
        final int expectedSize = specialistsService.getSpecialists().size() - 1;

        specialistsService.removeSpecialist(id);

        final int actualSize = specialistsService.getSpecialists().size();
        final Specialist testSpecialist = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization);

        assertThat(specialistsService.getSpecialists()).doesNotContain(testSpecialist);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testRemoveSpecialistWithNullId() {
        specialistsService.removeSpecialist(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    @Transactional
    public void testRemoveSpecialistWithWrongId() {
        specialistsService.removeSpecialist(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testFindSpecialistById() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();

        final Specialist expectedSpecialist = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization);
        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);

        assertThat(expectedSpecialist).isEqualTo(actualSpecialist);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testFindSpecialistByIdWithNullId() {
        specialistsService.findSpecialistById(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    @Transactional
    public void testFindSpecialistByIdWithWrongId() {
        specialistsService.findSpecialistById(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testActivateSpecialist() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, false, organization)
                .getId();

        specialistsService.activateSpecialist(id);

        final Specialist specialist = specialistsService.findSpecialistById(id);

        assertThat(specialist.isActive()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testActivateServiceWithNullId() {
        specialistsService.activateSpecialist(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    @Transactional
    public void testActivateServiceWrongId() {
        specialistsService.activateSpecialist(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void testDeactivateSpecialist() {

        final int id = specialistsService
                .addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, organization)
                .getId();

        specialistsService.deactivateSpecialist(id);

        final Specialist specialist = specialistsService.findSpecialistById(id);

        assertThat(specialist.isActive()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testDeactivateSpecialistWithNullId() {
        specialistsService.deactivateSpecialist(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    @Transactional
    public void testDeactivateSpecialistWrongId() {
        specialistsService.deactivateSpecialist(Integer.MIN_VALUE);
    }

    @Test
    @Transactional
    public void getSpecialists() {
        assertThat(specialistsService.getSpecialists()).isNotNull();
    }
}