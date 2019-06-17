package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение,
 * возникающее, если при поиске брони она не была найдена
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public class ReservationNotFoundException extends EntityNotFoundException {

    public ReservationNotFoundException(String message) {

        super(message);
        log.error(message);
    }
}
