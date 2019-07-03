package appointments.services;

import appointments.TestHelper;
import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.dto.SpecialistDTO;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.repos.OrganizationsRepository;
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
public class SpecialistsServiceTest {

    private static final String TEST_SPECIALIST_NAME = "Иванова Ольга Викторовна";
    private final static String ORGANIZATION_NAME = "Управление образования г. Белгород";

    private Organization organization;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private SpecialistsService specialistsService;


    @Before
    public void setUp() {

        testHelper.refill();
        organization = organizationsRepository.findOneByName(ORGANIZATION_NAME).orElse(null);
    }

    @Test
    @Transactional
    public void testAddSpecialist() {

        final int expectedSize = specialistsService.getSpecialists().size() + 1;

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
                ).getId();

        final int actualSize = specialistsService.getSpecialists().size();
        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(id, TEST_SPECIALIST_NAME, true, organization.getId());

        assertThat(specialistsService.getSpecialists()).contains(testSpecialistDTO);
        assertThat(expectedSize).isEqualTo(actualSize);
    }

    @Test
    @Transactional
    public void testEditSpecialistName() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
                ).getId();

        final String changedName = "Петрова Екатерина Васильевна";

        specialistsService.editSpecialist(
                new SpecialistDTO(id, changedName, true, organization.getId())
        );

        final SpecialistDTO actualSpecialistDTO = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist = new Specialist(id, changedName, true, organization);

        assertThat(actualSpecialistDTO.getName()).isEqualTo(testSpecialist.getName());
    }

    @Test
    @Transactional
    public void testEditSpecialistActive() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
                ).getId();

        specialistsService.editSpecialist(
                new SpecialistDTO(id, TEST_SPECIALIST_NAME, false, organization.getId())
        );

        final SpecialistDTO actualSpecialistDTO = specialistsService.findSpecialistById(id);

        assertThat(actualSpecialistDTO.isActive()).isFalse();
    }

    @Test
    @Transactional
    public void testEditSpecialistOrganization() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
                ).getId();

        final Organization changedOrganization
                = organizationsRepository.findOneByName("Управление образования г. Старый Оскол").orElse(null);

        specialistsService.editSpecialist(
                new SpecialistDTO(id, TEST_SPECIALIST_NAME, false, changedOrganization.getId())
        );

        final SpecialistDTO actualSpecialistDTO = specialistsService.findSpecialistById(id);
        final Specialist testSpecialist
                = new Specialist(id, TEST_SPECIALIST_NAME, true, changedOrganization);

        assertThat(actualSpecialistDTO.getOrganizationId()).isEqualTo(testSpecialist.getOrganization().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testEditSpecialistWithNullId() {
        specialistsService.editSpecialist(
                new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
        );
    }

    @Test(expected = SpecialistNotFoundException.class)
    @Transactional
    public void testEditSpecialistWithWrongId() {
        specialistsService.editSpecialist(
                new SpecialistDTO(Integer.MIN_VALUE, TEST_SPECIALIST_NAME, true, organization.getId())
        );
    }

    @Test
    @Transactional
    public void testRemoveSpecialist() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
                ).getId();

        final int expectedSize = specialistsService.getSpecialists().size() - 1;

        specialistsService.removeSpecialist(id);

        final int actualSize = specialistsService.getSpecialists().size();
        final SpecialistDTO testSpecialistDTO
                = new SpecialistDTO(id, TEST_SPECIALIST_NAME, true, organization.getId());

        assertThat(specialistsService.getSpecialists()).doesNotContain(testSpecialistDTO);
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
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
                ).getId();

        final SpecialistDTO expectedSpecialistDTO
                = new SpecialistDTO(id, TEST_SPECIALIST_NAME, true, organization.getId());
        final SpecialistDTO actualSpecialistDTO = specialistsService.findSpecialistById(id);

        assertThat(expectedSpecialistDTO).isEqualTo(actualSpecialistDTO);
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
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, false, organization.getId())
                ).getId();

        specialistsService.changeActiveState(id, true);

        final SpecialistDTO specialistDTO = specialistsService.findSpecialistById(id);

        assertThat(specialistDTO.isActive()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testActivateServiceWithNullId() {
        specialistsService.changeActiveState(null, true);
    }

    @Test(expected = SpecialistNotFoundException.class)
    @Transactional
    public void testActivateServiceWrongId() {
        specialistsService.changeActiveState(Integer.MIN_VALUE, true);
    }

    @Test
    @Transactional
    public void testDeactivateSpecialist() {

        final int id = specialistsService
                .addSpecialist(
                        new SpecialistDTO(null, TEST_SPECIALIST_NAME, true, organization.getId())
                ).getId();

        specialistsService.changeActiveState(id, false);

        final SpecialistDTO specialistDTO = specialistsService.findSpecialistById(id);

        assertThat(specialistDTO.isActive()).isFalse();
    }


    @Test
    @Transactional
    public void getSpecialists() {
        assertThat(specialistsService.getSpecialists()).isNotNull().isNotEmpty();
    }

    @Test
    @Transactional
    public void getActiveSpecialists() {

       assertThat(specialistsService.getActiveSpecialists()).allSatisfy(SpecialistDTO::isActive);
    }
}