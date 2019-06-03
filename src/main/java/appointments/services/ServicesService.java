package appointments.services;

import appointments.domain.Service;
import appointments.exceptions.ServiceNotFoundException;
import appointments.utils.DatabaseEmulator;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Класс, реализующий действия с объектами справочника Цели обращения(Услуги):
 * сохранение, удаление, активацию, деактивацию, получение списка целей обращения
 *
 * @author yanchenko_evgeniya
 */
public class ServicesService {

    /** Строковая константа для передачи в исключение, возникающее, если ID услуги не заполнен */
    private static final String EMPTY_ID_MESSAGE = "ID услуги не должен быть пустым";

    /** Строковая константа для передачи в исключение, возникающее, если услуга с указанным ID не найдена*/
    private static final String SERVICE_NOT_FOUND_MESSAGE = "Услуга не найдена. ID: ";

    /** Поле для хранения экземпляра эмулятора базы данных */
    private DatabaseEmulator databaseEmulator;

    public ServicesService() {
        databaseEmulator = DatabaseEmulator.getInstance();
    }

    /** Метод для добавления новой цели обращения (услуги) в справочник */
    public Integer addService(final String name, final boolean active) {

        if (isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Название услуги не должно быть пустым");
        }
        Service service = new Service(null, name, active);

        return databaseEmulator.addService(service);
    }

    /** Метод для удаления цели обращения (услуги) по идентификатору */
    public boolean removeService(final Integer id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Service service = databaseEmulator.findServiceById(id);
        if (service == null) {
            throw new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE + id);
        }

        return databaseEmulator.removeService(service);
    }

    /** Метод для поиска цели обращения по идентификатору */
    public Service findServiceById(final Integer id) {

        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Service service = databaseEmulator.findServiceById(id);
        if (service == null) {
            throw new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE);
        } else {

            return service;
        }
    }

    /** Метод для активации цели обращения (услуги) */
    public void activateService(final Integer id) {
        changeActiveState(id, true);
    }

    /** Метод для деактивации цели обращения (услуги) */
    public void deactivateService(final Integer id) {
        changeActiveState(id, false);
    }

    /** Служебный метод для смены флага активности цели обращения (услуги) */
    private void changeActiveState(final Integer id, final boolean makeActive) {
        if (id == null) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Service service = databaseEmulator.findServiceById(id);
        if (service == null) {
            throw new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE + id);
        }

        service.setActive(makeActive);
    }

    /** Метод для получения списка целей обращения (услуг) */
    public List<Service> getServices() {
        return databaseEmulator.getServices();
    }


}
