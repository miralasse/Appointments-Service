package appointments.exceptions;

/**
 * Класс-исключение,
 * возникающее, если при поиске специалиста в справочнике он не был найден
 *
 * @author yanchenko_evgeniya
 */
public class SpecialistNotFoundException extends RuntimeException {

    public SpecialistNotFoundException(String message) {
        super(message);
    }
}
