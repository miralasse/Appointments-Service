package appointments.exceptions;

/**
 * Класс-исключение,
 * возникающее, если при поиске брони она не была найдена
 *
 * @author yanchenko_evgeniya
 */
public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
