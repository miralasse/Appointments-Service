package appointments.handlers;

import appointments.dto.UserDTO;
import appointments.exceptions.EntityNotFoundException;
import appointments.exceptions.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

/** Класс для обработки исключений, возникающих при работе приложения
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    /** Метод, реализующий обработку исключений, возникающих при некорректных входных данных*/
    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<String> handleIllegalArgumentException(Exception e) {

        log.error(e.getMessage());

        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    /** Метод, реализующий обработку исключений, возникающих при отсутствии ресурса с указанным идентификатором */
    @ExceptionHandler({EntityNotFoundException.class, UsernameNotFoundException.class})
    protected ResponseEntity<String> handleEntityNotFoundException(Exception e) {

        log.error(e.getMessage());

        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    /** Метод, реализующий обработку исключений, возникающих при попытке добавить пользователя,
     * уникальное имя которого уже есть в базе */
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected String handleUserAlreadyExistsException(UserAlreadyExistsException e, Model model) {

        log.error(e.getMessage());

        model.addAttribute("user", new UserDTO());
        model.addAttribute("usernameExistsError", e.getMessage());

        return "sign-up";
    }



    /** Метод, реализующий обработку исключений, возникающих при нарушении правил валидации полей */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {

        final Map<String, String> errors = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .collect(toMap(
                        error -> ((FieldError)error).getField(), DefaultMessageSourceResolvable::getDefaultMessage)
                );

        log.error(exception.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    /** Метод, реализующий обработку остальных исключений */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleAnotherException(Exception e) {

        log.error(e.getMessage(), e);

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
