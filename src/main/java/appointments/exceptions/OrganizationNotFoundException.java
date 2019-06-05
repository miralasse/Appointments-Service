package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение,
 * возникающее, если при поиске ребенка он не была найден
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(String message) {

        super(message);
        log.error(message);
    }
}
