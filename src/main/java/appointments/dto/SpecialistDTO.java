package appointments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static appointments.utils.Constants.SPECIALIST_EMPTY_NAME_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ORGANIZATION_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_EMPTY_ROOM_NUMBER_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_MAX_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_MAX_ROOM_NUMBER_LENGTH;
import static appointments.utils.Constants.SPECIALIST_MIN_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_MIN_ROOM_NUMBER_LENGTH;
import static appointments.utils.Constants.SPECIALIST_WRONG_NAME_LENGTH;
import static appointments.utils.Constants.SPECIALIST_WRONG_ROOM_NUMBER_LENGTH;


/**
 * @author yanchenko_evgeniya
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistDTO {

    /** Поле для индентификатора специалиста: null для нового, not null для существующего */
    private Integer id;


    /** Поле для указания должности или ФИО специалиста для отображения на сайте */
    @NotNull(message = SPECIALIST_EMPTY_NAME_MESSAGE)
    @Size(
            min = SPECIALIST_MIN_NAME_LENGTH,
            max = SPECIALIST_MAX_NAME_LENGTH,
            message = SPECIALIST_WRONG_NAME_LENGTH
    )
    private String name;


    /** Поле Номер кабинета */
    @NotNull(message = SPECIALIST_EMPTY_ROOM_NUMBER_MESSAGE)
    @Size(
            min = SPECIALIST_MIN_ROOM_NUMBER_LENGTH,
            max = SPECIALIST_MAX_ROOM_NUMBER_LENGTH,
            message = SPECIALIST_WRONG_ROOM_NUMBER_LENGTH
    )
    private String roomNumber;


    /** Поле Флаг активности специалиста */
    private boolean active;


    /** Поле индентификатора организации к которой относится специалист */
    @NotNull(message = SPECIALIST_EMPTY_ORGANIZATION_MESSAGE)
    private Integer organizationId;

}
