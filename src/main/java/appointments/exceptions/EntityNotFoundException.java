package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Абстрактный класс-исключение,
 * возникающее, если при поиске сущности она не была найдена.
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public abstract class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
