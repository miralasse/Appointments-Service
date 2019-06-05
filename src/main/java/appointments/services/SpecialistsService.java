package appointments.services;

import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.repos.SpecialistsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Класс, реализующий действия с объектами справочника Специалисты:
 * сохранение, редактирование, удаление, активация, деактивация, получение списка специалистов
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@Service
public class SpecialistsService {

    private static final String EMPTY_ID_MESSAGE = "ID специалиста не должен быть пустым";
    private static final String SPECIALIST_NOT_FOUND_MESSAGE = "Специалист не найден. ID: ";
    private static final String EMPTY_NAME_MESSAGE = "Имя/должность специалиста должно быть заполнено";
    private static final String EMPTY_ROOM_NUMBER_MESSAGE = "Номер кабинета не должен быть пустым";
    private static final String EMPTY_ORGANIZATION_MESSAGE
            = "Должна быть указана организация, к которой относится специалист";

    /** Поле для хранения экземпляра репозитория */
    private SpecialistsRepository specialistsRepository;

    @Autowired
    public SpecialistsService(SpecialistsRepository specialistsRepository) {
        this.specialistsRepository = specialistsRepository;
    }

    /** Метод для добавления нового специалиста в справочник */
    @Transactional
    public Specialist addSpecialist(
            final String name, final String roomNumber, final boolean active, final Organization organization
    ) {

        if (isNullOrEmpty(name)) {
            log.error("Parameter 'name' is null");
            throw new IllegalArgumentException(EMPTY_NAME_MESSAGE);
        }
        if (isNullOrEmpty(roomNumber)) {
            log.error("Parameter 'roomNumber' is null");
            throw new IllegalArgumentException(EMPTY_ROOM_NUMBER_MESSAGE);
        }
        if (organization == null) {
            log.error("Parameter 'organization' is null");
            throw new IllegalArgumentException(EMPTY_ORGANIZATION_MESSAGE);
        }

        final Specialist specialist = specialistsRepository.save(
                new Specialist(null, name, roomNumber, active, organization)
        );

        log.info("Added new specialist: {}", specialist);

        return specialist;
    }

    /** Метод для поиска специалиста по идентификатору */
    @Transactional(readOnly = true)
    public Specialist findSpecialistById(final Integer id) {

        log.debug("Finding specialist with id = {}", id);

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }

        return specialistsRepository
                .findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE));
    }

    /** Метод для редактирования специалиста в справочнике */
    @Transactional
    public void editSpecialist(
            final Integer id, final String name, final String roomNumber,
            final boolean active, final Organization organization
    ) {

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        if (isNullOrEmpty(name)) {
            log.error("Trying to set 'name' to null");
            throw new IllegalArgumentException(EMPTY_NAME_MESSAGE);
        }
        if (isNullOrEmpty(roomNumber)) {
            log.error("Trying to set 'roomNumber' to null");
            throw new IllegalArgumentException(EMPTY_ROOM_NUMBER_MESSAGE);
        }
        if (organization == null) {
            log.error("Trying to set 'organization' to null");
            throw new IllegalArgumentException(EMPTY_ORGANIZATION_MESSAGE);
        }

        final Specialist specialist = specialistsRepository
                .findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE));

        specialist.setName(name);
        specialist.setRoomNumber(roomNumber);
        specialist.setActive(active);
        specialist.setOrganization(organization);

        specialistsRepository.save(specialist);

        log.info("Specialist with id = {} edited: {}", id, specialist);
    }

    /** Метод для удаления специалиста по идентификатору */
    @Transactional
    public void removeSpecialist(final Integer id) {

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Specialist specialist = specialistsRepository
                .findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE));

        specialistsRepository.delete(specialist);

        log.info("Specialist with id = {} deleted", id);
    }

    /** Метод для активации специалиста в списке */
    public void activateSpecialist(final Integer id) {

        changeActiveState(id, true);

        log.info("Specialist with id = {} activated", id);
    }

    /** Метод для деактивации специалиста в списке*/
    public void deactivateSpecialist(final Integer id) {

        changeActiveState(id, false);

        log.info("Specialist with id = {} deactivated", id);
    }

    /** Служебный метод для смены флага активности специалиста */
    @Transactional
    private void changeActiveState(final Integer id, final boolean makeActive) {

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Specialist specialist = specialistsRepository
                .findById(id)
                .orElseThrow(() -> new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE));

        specialist.setActive(makeActive);
        specialistsRepository.save(specialist);
    }

    /** Метод для получения списка специалистов */
    @Transactional(readOnly = true)
    public List<Specialist> getSpecialists() {

        log.debug("Getting list of all specialists");

        return specialistsRepository.findAll();
    }


}
