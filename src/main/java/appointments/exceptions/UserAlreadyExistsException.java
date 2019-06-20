package appointments.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yanchenko_evgeniya
 */
@Slf4j
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {

        super(message);
        log.error(message);
    }
}
