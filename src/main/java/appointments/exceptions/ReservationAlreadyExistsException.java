package appointments.exceptions;

/**
 * Класс-исключение,
 * возникающее, если при попытке записаться на прием указанное время уже занято
 *
 * @author yanchenko_evgeniya
 */
public class ReservationAlreadyExistsException extends RuntimeException {

    public ReservationAlreadyExistsException(String message) {
        super(message);
    }
}
