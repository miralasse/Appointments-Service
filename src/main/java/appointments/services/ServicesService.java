package appointments.services;

import appointments.domain.Service;
import appointments.exceptions.ServiceNotFoundException;
import appointments.repos.ServicesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Класс, реализующий действия с объектами справочника Цели обращения(Услуги):
 * сохранение, удаление, активацию, деактивацию, получение списка целей обращения
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@org.springframework.stereotype.Service
public class ServicesService {

    private static final String EMPTY_ID_MESSAGE = "ID услуги не должен быть пустым";
    private static final String SERVICE_NOT_FOUND_MESSAGE = "Услуга не найдена. ID: ";

    /** Поле для хранения экземпляра репозитория */
    private ServicesRepository servicesRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    /** Метод для добавления новой цели обращения (услуги) в справочник */
    @Transactional
    public Service addService(final String name, final boolean active) {

        if (isNullOrEmpty(name)) {
            log.error("Parameter 'name' is null");
            throw new IllegalArgumentException("Название услуги не должно быть пустым");
        }

        final Service service = servicesRepository.save(new Service(null, name, active));

        log.info("Added new service: {}", service);

        return service;
    }

    /** Метод для удаления цели обращения (услуги) по идентификатору */
    @Transactional
    public void removeService(final Integer id) {

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Service service = servicesRepository
                .findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE + id));

        servicesRepository.delete(service);

        log.info("Service with id = {} deleted", id);
    }

    /** Метод для поиска цели обращения по идентификатору */
    @Transactional(readOnly = true)
    public Service findServiceById(final Integer id) {

        log.debug("Finding service with id = {}", id);

        if (id == null) {
            log.error(EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }

        return servicesRepository
                .findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE));
    }

    /** Метод для активации цели обращения (услуги) */
    public void activateService(final Integer id) {

        changeActiveState(id, true);

        log.info("Service with id = {} activated", id);
    }

    /** Метод для деактивации цели обращения (услуги) */
    public void deactivateService(final Integer id) {

        changeActiveState(id, false);

        log.info("Service with id = {} deactivated", id);
    }

    /** Служебный метод для смены флага активности цели обращения (услуги) */
    @Transactional
    private void changeActiveState(final Integer id, final boolean makeActive) {

        if (id == null) {
            log.error("Parameter 'id' is null");
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        final Service service = servicesRepository
                .findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE + id));

        service.setActive(makeActive);
        servicesRepository.save(service);
    }

    /** Метод для получения списка целей обращения (услуг) */
    @Transactional(readOnly = true)
    public List<Service> getServices() {

        log.debug("Getting list of all services");

        return servicesRepository.findAll();
    }


}
