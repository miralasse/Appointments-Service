package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение,
 * возникающее, если при поиске расписания оно не было найдено
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public class ScheduleNotFoundException extends EntityNotFoundException {

    public ScheduleNotFoundException(String message) {

        super(message);
        log.error(message);
    }
}
