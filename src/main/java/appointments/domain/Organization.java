package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static appointments.utils.Constants.ORGANIZATION_MAX_STRING_LENGTH;
import static appointments.utils.Constants.ORGANIZATION_MIN_STRING_LENGTH;
import static appointments.utils.Constants.ORGANIZATION_NULL_ADDRESS_MESSAGE;
import static appointments.utils.Constants.ORGANIZATION_NULL_DESCRIPTION_MESSAGE;
import static appointments.utils.Constants.ORGANIZATION_NULL_NAME_MESSAGE;
import static appointments.utils.Constants.ORGANIZATION_WRONG_ADDRESS_MESSAGE;
import static appointments.utils.Constants.ORGANIZATION_WRONG_DESCRIPTION_MESSAGE;
import static appointments.utils.Constants.ORGANIZATION_WRONG_NAME_MESSAGE;


/**
 * Класс, описывающий сущность Организация.
 * Содержит название, фактический адрес и описание с контактной информацией.
 *
 * @author yanchenko_evgeniya
 */

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    /** Поле Название */
    @NotNull(message = ORGANIZATION_NULL_NAME_MESSAGE)
    @Size(
            min = ORGANIZATION_MIN_STRING_LENGTH,
            max = ORGANIZATION_MAX_STRING_LENGTH,
            message = ORGANIZATION_WRONG_NAME_MESSAGE
    )
    @Column
    private String name;


    /** Поле Фактический адрес */
    @NotNull(message = ORGANIZATION_NULL_ADDRESS_MESSAGE)
    @Size(
            min = ORGANIZATION_MIN_STRING_LENGTH,
            max = ORGANIZATION_MAX_STRING_LENGTH,
            message = ORGANIZATION_WRONG_ADDRESS_MESSAGE
    )
    @Column(name = "actual_address")
    private String actualAddress;


    /** Поле Описание для внесения контактной и другой полезной информации */
    @NotNull(message = ORGANIZATION_NULL_DESCRIPTION_MESSAGE)
    @Size(
            min = ORGANIZATION_MIN_STRING_LENGTH,
            max = ORGANIZATION_MAX_STRING_LENGTH,
            message = ORGANIZATION_WRONG_DESCRIPTION_MESSAGE
    )
    @Column
    private String description;

}
