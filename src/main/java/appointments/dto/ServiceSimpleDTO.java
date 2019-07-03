package appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static appointments.utils.Constants.SERVICE_EMPTY_ID_MESSAGE;
import static appointments.utils.Constants.SERVICE_MAX_NAME_LENGTH;
import static appointments.utils.Constants.SERVICE_MIN_NAME_LENGTH;
import static appointments.utils.Constants.SERVICE_NULL_NAME_MESSAGE;
import static appointments.utils.Constants.SERVICE_WRONG_LENGTH_MESSAGE;


/**
 * Класс, описывающий сущность Услуга (Цель обращения).
 * Содержит название и флаг активности услуги.
 *
 * @author yanchenko_evgeniya
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSimpleDTO {

    @NotNull(message = SERVICE_EMPTY_ID_MESSAGE)
    private Integer id;


    /** Название услуги */
    @NotEmpty(message = SERVICE_NULL_NAME_MESSAGE)
    @Size(
            min = SERVICE_MIN_NAME_LENGTH,
            max = SERVICE_MAX_NAME_LENGTH,
            message = SERVICE_WRONG_LENGTH_MESSAGE
    )
    private String name;
}
