package appointments.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс, описывающий сущность Специалист (сотрудник, ведущий прием).
 * Содержит имя (ФИО/должность), номер кабинета,
 * флаг активности и ссылку на организацию, к которой относится специалист.
 *
 * @author yanchenko_evgeniya
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Specialist {

    private Integer id;

    /** Поле для указания должности или ФИО специалиста для отображения на сайте */
    private String name;

    /** Поле Номер кабинета */
    private String roomNumber;

    /** Поле Флаг активности специалиста */
    private boolean active;

    /** Поле Ссылка на организацию (объект Organization), к которой относится специалист */
    private Organization organization;

}
