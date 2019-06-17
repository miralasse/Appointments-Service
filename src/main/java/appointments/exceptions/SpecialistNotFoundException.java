package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс-исключение,
 * возникающее, если при поиске специалиста в справочнике он не был найден
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
public class SpecialistNotFoundException extends EntityNotFoundException {

    public SpecialistNotFoundException(String message) {

        super(message);
        log.error(message);
    }
}
