package appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static appointments.utils.Constants.SPECIALIST_EMPTY_ID_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_NAME_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_MAX_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_MIN_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_WRONG_NAME_LENGTH;


/**
 * @author yanchenko_evgeniya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistSimpleDTO {

    @NotNull(message = SPECIALIST_EMPTY_ID_MESSAGE)
    private Integer id;


    /** Должность или ФИО специалиста для отображения на сайте */
    @NotEmpty(message = SPECIALIST_EMPTY_NAME_MESSAGE)
    @Size(
            min = SPECIALIST_MIN_NAME_LENGTH,
            max = SPECIALIST_MAX_NAME_LENGTH,
            message = SPECIALIST_WRONG_NAME_LENGTH
    )
    private String name;

}
