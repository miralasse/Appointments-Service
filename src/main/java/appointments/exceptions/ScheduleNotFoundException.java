package appointments.exceptions;

/**
 * Класс-исключение,
 * возникающее, если при поиске расписания оно не было найдено
 *
 * @author yanchenko_evgeniya
 */
public class ScheduleNotFoundException extends RuntimeException {

    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
