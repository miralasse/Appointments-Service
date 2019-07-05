package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение,
 * возникающее при попытке удалить сущность, на которую есть ссылки в других сущностях
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public class EntityDependencyException extends RuntimeException {

    public EntityDependencyException(String message) {

        super(message);
        log.error(message);
    }
}
