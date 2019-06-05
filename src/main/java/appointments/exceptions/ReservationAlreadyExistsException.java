package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение,
 * возникающее, если при попытке записаться на прием указанное время уже занято
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public class ReservationAlreadyExistsException extends RuntimeException {

    public ReservationAlreadyExistsException(String message) {

        super(message);
        log.error(message);
    }
}
