package services;

import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.services.SpecialistsService;
import appointments.utils.EmulatorInitializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author yanchenko_evgeniya
 */
public class SpecialistsServiceTest {

    private static final String TEST_SPECIALIST_NAME = "Иванова Ольга Викторовна";
    private static final String ROOM_NUMBER = "25";
    private static final String TEST_ORGANIZATION_NAME = "Управление культуры Белгородской области";
    private static final String TEST_ORGANIZATION_ADDRESS = "г. Белгород, Гражданский пр-т, 40";
    private static final String DESCRIPTION = "+7(4722)27‑72-52";
    private static final Organization ORGANIZATION
            = new Organization(3, TEST_ORGANIZATION_NAME, TEST_ORGANIZATION_ADDRESS, DESCRIPTION);

    private static EmulatorInitializer emulatorInitializer = new EmulatorInitializer();
    private SpecialistsService specialistsService = new SpecialistsService();



    @BeforeClass
    public static void initCollections() {
        emulatorInitializer.initAll();
    }

    @Before
    public void initTest() {

        specialistsService.getSpecialists().clear();
        specialistsService.getSpecialists().addAll(emulatorInitializer.initSpecialists());
    }

    @Test
    public void testAddSpecialist() {

        final int expectedSize = specialistsService.getSpecialists().size() + 1;

        final int id
                = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        final int actualSize = specialistsService.getSpecialists().size();
        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        assertThat(specialistsService.getSpecialists()).contains(testSpecialist);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecialistWithNullName() {
        specialistsService.addSpecialist(null, ROOM_NUMBER, true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecialistWithEmptyName() {
        specialistsService.addSpecialist("", ROOM_NUMBER, true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecialistWithNullRoomNumber() {
        specialistsService.addSpecialist(TEST_SPECIALIST_NAME, null, true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecialistWithEmptyRoomNumber() {
        specialistsService.addSpecialist(TEST_SPECIALIST_NAME, "", true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpecialistWithNullOrganization() {
        specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, null);
    }

    @Test
    public void testEditSpecialistName() {

        final int id
                = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        final String changedName = "Петрова Екатерина Васильевна";

        specialistsService.editSpecialist(id, changedName, ROOM_NUMBER, true, ORGANIZATION);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist = new Specialist(id, changedName, ROOM_NUMBER, true, ORGANIZATION);

        assertThat(actualSpecialist.getName()).isEqualTo(testSpecialist.getName());
    }

    @Test
    public void testEditSpecialistRoomNumber() {

        final int id
                = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        final String changedRoomNumber = "18";

        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, changedRoomNumber, true, ORGANIZATION);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, changedRoomNumber,  true, ORGANIZATION);

        assertThat(actualSpecialist.getRoomNumber()).isEqualTo(testSpecialist.getRoomNumber());
    }

    @Test
    public void testEditSpecialistActive() {

        final int id
                = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, false, ORGANIZATION);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, false, ORGANIZATION);

        assertThat(actualSpecialist.isActive()).isEqualTo(testSpecialist.isActive());
    }

    @Test
    public void testEditSpecialistOrganization() {

        final int id
                = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        final Organization changedOrganization = new Organization(
                4, "Департамент здравоохранения и социальной защиты населения Белгородской области",
                "г. Белгород, Свято-Троицкий бул., 18", "+7(4722)23‑56-46"
        );

        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, changedOrganization);

        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, changedOrganization);

        assertThat(actualSpecialist.getOrganization()).isEqualTo(testSpecialist.getOrganization());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditSpecialistWithNullId() {
        specialistsService.editSpecialist(null, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
    }

    @Test(expected = SpecialistNotFoundException.class)
    public void testEditSpecialistWithWrongId() {
        specialistsService.editSpecialist(Integer.MIN_VALUE, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditSpecialistWithNullName() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
        specialistsService.editSpecialist(id, null, ROOM_NUMBER, true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditSpecialistWithEmptyName() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
        specialistsService.editSpecialist(id, "", ROOM_NUMBER, true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditSpecialistWithNullRoomNumber() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, null, true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditSpecialistWithEmptyRoomNumber() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, "", true, ORGANIZATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditSpecialistWithNullOrganization() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
        specialistsService.editSpecialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, null);
    }

    @Test
    public void testRemoveSpecialist() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
        final int expectedSize = specialistsService.getSpecialists().size() - 1;

        specialistsService.removeSpecialist(id);

        final int actualSize = specialistsService.getSpecialists().size();
        final Specialist testSpecialist = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        assertThat(specialistsService.getSpecialists()).doesNotContain(testSpecialist);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSpecialistWithNullId() {
        specialistsService.removeSpecialist(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    public void testRemoveSpecialistWithWrongId() {
        specialistsService.removeSpecialist(Integer.MIN_VALUE);
    }

    @Test
    public void testFindSpecialistById() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        final Specialist expectedSpecialist = new Specialist(id, TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);
        final Specialist actualSpecialist = specialistsService.findSpecialistById(id);

        assertThat(expectedSpecialist).isEqualTo(actualSpecialist);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSpecialistByIdWithNullId() {
        specialistsService.findSpecialistById(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    public void testFindSpecialistByIdWithWrongId() {
        specialistsService.findSpecialistById(Integer.MIN_VALUE);
    }

    @Test
    public void testActivateSpecialist() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, false, ORGANIZATION);

        specialistsService.activateSpecialist(id);

        final Specialist specialist = specialistsService.findSpecialistById(id);

        assertThat(specialist.isActive()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActivateServiceWithNullId() {
        specialistsService.activateSpecialist(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    public void testActivateServiceWrongId() {
        specialistsService.activateSpecialist(Integer.MIN_VALUE);
    }

    @Test
    public void testDeactivateSpecialist() {

        final int id = specialistsService.addSpecialist(TEST_SPECIALIST_NAME, ROOM_NUMBER, true, ORGANIZATION);

        specialistsService.deactivateSpecialist(id);

        final Specialist specialist = specialistsService.findSpecialistById(id);

        assertThat(specialist.isActive()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeactivateSpecialistWithNullId() {
        specialistsService.deactivateSpecialist(null);
    }

    @Test(expected = SpecialistNotFoundException.class)
    public void testDeactivateSpecialistWrongId() {
        specialistsService.deactivateSpecialist(Integer.MIN_VALUE);
    }

    @Test
    public void getSpecialists() {
        assertThat(specialistsService.getSpecialists()).isNotNull();
    }
}