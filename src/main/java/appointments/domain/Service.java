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

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    /** Поле Название услуги*/
    @NotNull(message = SERVICE_NULL_NAME_MESSAGE)
    @Size(
            min = SERVICE_MIN_NAME_LENGTH,
            max = SERVICE_MAX_NAME_LENGTH,
            message = SERVICE_WRONG_LENGTH_MESSAGE
    )
    @Column
    private String name;


    /** Поле Флаг активности */
    @Column
    private boolean active;
}
