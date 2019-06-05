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

    private static final int MIN_NAME_LENGTH = 8;
    private static final int MAX_NAME_LENGTH = 400;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;


    /** Поле Название услуги*/
    @NotNull(message = "Наименование услуги(цели обращения) должно быть указано")
    @Size(
            min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Длина наименования услуги(цели обращения) должна быть от "
                    + MIN_NAME_LENGTH
                    + " до "
                    + MAX_NAME_LENGTH
                    + " символов"
    )
    @Column
    private String name;


    /** Поле Флаг активности */
    @Column
    private boolean active;
}
