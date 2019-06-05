package appointments.exceptions;

/**
 * Класс-исключение,
 * возникающее, если при поиске услуги (цели обращения) в справочнике она не была найдена
 *
 * @author yanchenko_evgeniya
 */
public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(String message) {
        super(message);
    }
}