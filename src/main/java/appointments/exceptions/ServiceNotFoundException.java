package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение,
 * возникающее, если при поиске услуги (цели обращения) в справочнике она не была найдена
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(String message) {

        super(message);
        log.error(message);
    }
}
