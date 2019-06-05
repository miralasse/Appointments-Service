package appointments.exceptions;

/**
 * Класс-исключение,
 * возникающее, если при поиске ребенка он не была найден
 *
 * @author yanchenko_evgeniya
 */
public class ChildNotFoundException extends RuntimeException {

    public ChildNotFoundException(String message) {
        super(message);
    }
}
