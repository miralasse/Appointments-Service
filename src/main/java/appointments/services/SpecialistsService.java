package appointments.services;

import appointments.domain.Organization;
import appointments.domain.Specialist;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.utils.DatabaseEmulator;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Класс, реализующий действия с объектами справочника Специалисты:
 * сохранение, редактирование, удаление, активация, деактивация, получение списка специалистов
 *
 * @author yanchenko_evgeniya
 */
public class SpecialistsService {

    /** Строковая константа для передачи в исключение, возникающее, если ID специалиста не заполнен */
    private static final String EMPTY_ID_MESSAGE = "ID специалиста не должен быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если специалист с указанным ID не найден */
    private static final String SPECIALIST_NOT_FOUND_MESSAGE = "Специалист не найден. ID: ";

    /** Строковая константа для передачи в исключение, возникающее, если имя/должность специалиста не заполнено */
    private static final String EMPTY_NAME_MESSAGE = "Имя/должность специалиста должно быть заполнено";

    /** Строковая константа для передачи в исключение, возникающее, если номер кабинета специалиста не заполнен */
    private static final String EMPTY_ROOM_NUMBER_MESSAGE = "Номер кабинета не должен быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если организация специалиста не заполнена */
    private static final String EMPTY_ORGANIZATION_MESSAGE
            = "Должна быть указана организация, к которой относится специалист";

    /** Поле для хранения экземпляра эмулятора базы данных */
    private DatabaseEmulator databaseEmulator;

    public SpecialistsService() {
        databaseEmulator = DatabaseEmulator.getInstance();
    }

    /** Метод для добавления нового специалиста в справочник */
    public Integer addSpecialist(
            final String name, final String roomNumber, final boolean active, final Organization organization
    ) {

        if (isNullOrEmpty(name)) {
            throw new IllegalArgumentException(EMPTY_NAME_MESSAGE);
        }
        if (isNullOrEmpty(roomNumber)) {
            throw new IllegalArgumentException(EMPTY_ROOM_NUMBER_MESSAGE);
        }
        if (organization == null) {
            throw new IllegalArgumentException(EMPTY_ORGANIZATION_MESSAGE);
        }

        final Specialist specialist = new Specialist(null, name, roomNumber, active, organization);

        return databaseEmulator.addSpecialist(specialist);
    }

    /** Метод для поиска цели обращения по идентификатору */
    public Specialist findSpecialistById(final Integer id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Specialist specialist = databaseEmulator.findSpecialistById(id);
        if (specialist == null) {
            throw new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE);
        } else {

            return specialist;
        }
    }

    /** Метод для редактирования специалиста в справочнике */
    public void editSpecialist(
            final Integer id, final String name, final String roomNumber,
            final boolean active, final Organization organization
    ) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        if (isNullOrEmpty(name)) {
            throw new IllegalArgumentException(EMPTY_NAME_MESSAGE);
        }
        if (isNullOrEmpty(roomNumber)) {
            throw new IllegalArgumentException(EMPTY_ROOM_NUMBER_MESSAGE);
        }
        if (organization == null) {
            throw new IllegalArgumentException(EMPTY_ORGANIZATION_MESSAGE);
        }
        Specialist specialist = findSpecialistById(id);
        if (specialist == null) {
            throw new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE);
        } else {
            specialist = new Specialist(id, name, roomNumber, active, organization);
            databaseEmulator.updateSpecialist(specialist);
        }
    }

    /** Метод для удаления специалиста по идентификатору */
    public boolean removeSpecialist(final Integer id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Specialist specialist = databaseEmulator.findSpecialistById(id);
        if (specialist == null) {
            throw new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE);
        }

        return databaseEmulator.removeSpecialist(specialist);
    }

    /** Метод для активации специалиста в списке */
    public void activateSpecialist(final Integer id) {
        changeActiveState(id, true);
    }

    /** Метод для деактивации специалиста в списке*/
    public void deactivateSpecialist(final Integer id) {
        changeActiveState(id, false);
    }

    /** Служебный метод для смены флага активности специалиста */
    private void changeActiveState(final Integer id, final boolean makeActive) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Specialist specialist = databaseEmulator.findSpecialistById(id);
        if (specialist == null) {
            throw new SpecialistNotFoundException(SPECIALIST_NOT_FOUND_MESSAGE);
        }

        specialist.setActive(makeActive);
    }


    /** Метод для получения списка специалистов */
    public List<Specialist> getSpecialists() {
        return databaseEmulator.getSpecialists();
    }


}
