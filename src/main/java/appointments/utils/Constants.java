package appointments.utils;

/**
 * @author yanchenko_evgeniya
 */
public class Constants {

    /** Константы для сущности Child */

    public static final int CHILD_MIN_BIRTH_CERTIFICATE_SERIES_LENGTH = 3;
    public static final int CHILD_MAX_BIRTH_CERTIFICATE_SERIES_LENGTH = 6;
    public static final int CHILD_MAX_BIRTH_CERTIFICATE_NUMBER = 999999;

    public static final String CHILD_WRONG_SERIES_LENGTH
            = "Длина серии свидетельства о рождении должна содержать от "
            + CHILD_MIN_BIRTH_CERTIFICATE_SERIES_LENGTH + " до "
            + CHILD_MAX_BIRTH_CERTIFICATE_SERIES_LENGTH + " символов";

    public static final String CHILD_WRONG_NUMBER_LENGTH
            = "Номер свидетельства о рождении должен содержать только 6 цифр";

    public static final String CHILD_WRONG_PHONE_MESSAGE
            = "Номер телефона должен начинаться с кода 8 или +7 и далее содержать 10 цифр";

    public static final int CHILD_MIN_NAME_LENGTH = 2;
    public static final int CHILD_MAX_NAME_LENGTH = 50;

    public static final String CHILD_WRONG_FIRST_NAME_LENGTH = "Длина имени должна быть от "
            + CHILD_MIN_NAME_LENGTH + " до "
            + CHILD_MAX_NAME_LENGTH  + " символов";

    public static final String CHILD_WRONG_LAST_NAME_LENGTH = "Длина фамилии должна быть от "
            + CHILD_MIN_NAME_LENGTH + " до "
            + CHILD_MAX_NAME_LENGTH  + " символов";

    public static final String CHILD_NULL_SERIES_MESSAGE
            = "Серия свидетельства о рождении должна быть указана";

    public static final String CHILD_NULL_NUMBER_MESSAGE
            = "Номер свидетельства о рождении должен быть указан";

    public static final String CHILD_NULL_PHONE_MESSAGE
            = "Контактный телефон должен быть указан";

    public static final String CHILD_NULL_EMAIL_MESSAGE
            = "Адрес электронной почты должен быть указан";

    public static final String CHILD_NULL_FIRST_NAME_MESSAGE
            = "Имя должно быть указано";

    public static final String CHILD_NULL_LAST_NAME_MESSAGE
            = "Фамилия должна быть указана";

    public static final String CHILD_NOT_FOUND_MESSAGE = "Ребёнок не найден. ID: ";



    /** Константы для сущности Organization */

    public static final int ORGANIZATION_MIN_STRING_LENGTH = 12;
    public static final int ORGANIZATION_MAX_STRING_LENGTH = 400;

    public static final String ORGANIZATION_WRONG_NAME_MESSAGE
            = "Длина наименования организации должна быть от "
            + ORGANIZATION_MIN_STRING_LENGTH + " до "
            + ORGANIZATION_MAX_STRING_LENGTH + " символов";

    public static final String ORGANIZATION_WRONG_ADDRESS_MESSAGE
            = "Длина адреса организации должна быть от "
            + ORGANIZATION_MIN_STRING_LENGTH + " до "
            + ORGANIZATION_MAX_STRING_LENGTH + " символов";

    public static final String ORGANIZATION_WRONG_DESCRIPTION_MESSAGE
            = "Длина контактной информации должна быть от "
            + ORGANIZATION_MIN_STRING_LENGTH + " до "
            + ORGANIZATION_MAX_STRING_LENGTH + " символов";

    public static final String ORGANIZATION_NULL_NAME_MESSAGE
            = "Наименование организации должно быть указано";

    public static final String ORGANIZATION_NULL_ADDRESS_MESSAGE
            = "Фактический адрес организации должен быть указан";

    public static final String ORGANIZATION_NULL_DESCRIPTION_MESSAGE
            = "Контактная информация организации должна быть указана";

    public static final String ORGANIZATION_NOT_FOUND_MESSAGE = "Организация не найдена. ID: ";



    /** Константы для сущности Service и класса ServicesService */

    public static final String SERVICE_NULL_NAME_MESSAGE
            = "Наименование услуги(цели обращения) должно быть указано";

    public static final int SERVICE_MIN_NAME_LENGTH = 8;
    public static final int SERVICE_MAX_NAME_LENGTH = 400;

    public static final String SERVICE_WRONG_LENGTH_MESSAGE
            = "Длина наименования услуги(цели обращения) должна быть от "
            + SERVICE_MIN_NAME_LENGTH + " до "
            + SERVICE_MAX_NAME_LENGTH + " символов";

    public static final String SERVICE_NOT_FOUND_MESSAGE = "Услуга не найдена. ID: ";
    public static final String SERVICE_EMPTY_ID_MESSAGE = "ID услуги не должен быть пустым";



    /** Константы для сущности Specialist и класса SpecialistsService */

    public static final int SPECIALIST_MIN_NAME_LENGTH = 8;
    public static final int SPECIALIST_MAX_NAME_LENGTH = 400;

    public static final String SPECIALIST_WRONG_NAME_LENGTH
            = "Длина должности/ФИО специалиста должна быть от "
            + SPECIALIST_MIN_NAME_LENGTH + " до "
            + SPECIALIST_MAX_NAME_LENGTH + " символов";

    public static final int SPECIALIST_MIN_ROOM_NUMBER_LENGTH = 1;
    public static final int SPECIALIST_MAX_ROOM_NUMBER_LENGTH = 16;

    public static final String SPECIALIST_WRONG_ROOM_NUMBER_LENGTH
            = "Длина номера кабинета должна быть от быть от "
            + SPECIALIST_MIN_ROOM_NUMBER_LENGTH + " до "
            + SPECIALIST_MAX_ROOM_NUMBER_LENGTH + " символов";

    public static final String SPECIALIST_EMPTY_ID_MESSAGE = "ID специалиста не должен быть пустым";
    public static final String SPECIALIST_NOT_FOUND_MESSAGE = "Специалист не найден. ID: ";
    public static final String SPECIALIST_EMPTY_NAME_MESSAGE = "Должность/ФИО специалиста должно быть указано";
    public static final String SPECIALIST_EMPTY_ROOM_NUMBER_MESSAGE = "Номер кабинета не должен быть пустым";

    public static final String SPECIALIST_EMPTY_ORGANIZATION_MESSAGE
            = "Должна быть указана организация, к которой относится специалист";



    /** Константы для сущности Schedule и класса ScheduleService */

    public static final int SCHEDULE_MIN_INTERVAL_LENGTH = 5;
    public static final int SCHEDULE_MAX_INTERVAL_LENGTH = 60;

    public static final String SCHEDULE_WRONG_INTERVAL_LENGTH
            = "Интервал приема должен быть от "
            + SCHEDULE_MIN_INTERVAL_LENGTH + " до "
            + SCHEDULE_MAX_INTERVAL_LENGTH + " минут";

    public static final String SCHEDULE_EMPTY_ID_MESSAGE = "ID расписания не должен быть пустым";
    public static final String SCHEDULE_NOT_FOUND_MESSAGE = "Расписание не найдено. ID: ";
    public static final String SCHEDULE_EMPTY_SPECIALIST_MESSAGE = "Расписание должно быть привязано к специалисту";
    public static final String SCHEDULE_INCORRECT_DATE_MESSAGE = "Для расписания должна быть указана корректная дата";
    public static final String SCHEDULE_EMPTY_SERVICES_MESSAGE = "Для расписания должны быть указаны услуги";
    public static final String SCHEDULE_EMPTY_START_TIME_MESSAGE = "Время начала приема не может быть пустым";
    public static final String SCHEDULE_EMPTY_END_TIME_MESSAGE = "Время окончания приема не может быть пустым";

    public static final String SCHEDULE_EMPTY_INTERVAL_MESSAGE
            = "Для расписания должен быть указан интервал приема (время на один талон) в минутах";



    /** Константы для сущности Reservation и класса ReservationService */

    public static final String RESERVATION_EMPTY_ID_MESSAGE = "ID записи на прием не должен быть пустым";
    public static final String RESERVATION_NOT_FOUND_MESSAGE = "Запись на прием не найдена. ID: ";

    public static final String RESERVATION_INCORRECT_DATETIME_MESSAGE
            = "Дата и время не заполнены или некорректны для указанного расписания";

    public static final String RESERVATION_EMPTY_SCHEDULE_MESSAGE
            = "Для записи на прием не указано, к какому расписанию она относится";

    public static final String RESERVATION_EMPTY_SERVICE_MESSAGE
            = "Для записи на прием должна быть указана цель обращения";

    public static final String RESERVATION_EMPTY_CHILD_MESSAGE
            = "Для записи на прием должен быть указан ребенок";


    /** Константы для сущности User */

    public static final String USER_WRONG_PHONE_MESSAGE
            = "Номер телефона должен начинаться с кода 8 или +7 и далее содержать 10 цифр";

    public static final int USER_MIN_NAME_LENGTH = 2;
    public static final int USER_MAX_NAME_LENGTH = 50;

    public static final String USER_WRONG_USERNAME_LENGTH = "Длина имени учетной записи должна быть от "
            + USER_MIN_NAME_LENGTH + " до "
            + USER_MAX_NAME_LENGTH + " символов";

    public static final String USER_WRONG_FIRST_NAME_LENGTH = "Длина имени должна быть от "
            + USER_MIN_NAME_LENGTH + " до "
            + USER_MAX_NAME_LENGTH + " символов";

    public static final String USER_WRONG_LAST_NAME_LENGTH = "Длина фамилии должна быть от "
            + USER_MIN_NAME_LENGTH + " до "
            + USER_MAX_NAME_LENGTH + " символов";

    public static final String USER_EMPTY_PHONE_MESSAGE
            = "Контактный телефон должен быть указан";

    public static final String USER_EMPTY_EMAIL_MESSAGE
            = "Адрес электронной почты должен быть указан";

    public static final String USER_EMPTY_USERNAME_MESSAGE
            = "Имя учетной записи должно быть указано";

    public static final String USER_EMPTY_PASSWORD_MESSAGE
            = "Пароль должен быть указан";

    public static final String USER_EMPTY_FIRST_NAME_MESSAGE
            = "Имя должно быть указано";

    public static final String USER_EMPTY_LAST_NAME_MESSAGE
            = "Фамилия должна быть указана";

    public static final String USER_NOT_FOUND_MESSAGE = "Некорректный логин/пароль. Не найден пользователь: ";

    public static final String USER_ALREADY_EXISTS_MESSAGE = "Уже существует пользователь с именем учетной записи: ";

    public static final int USER_MIN_PASSWORD_LENGTH = 6;
    public static final int USER_MAX_PASSWORD_LENGTH = 30;

    public static final String USER_WRONG_PASSWORD_LENGTH = "Длина пароля должна быть от "
            + USER_MIN_PASSWORD_LENGTH + " до "
            + USER_MAX_PASSWORD_LENGTH + " символов";

    public static final String USER_PASSWORDS_DO_NOT_MATCH = "Пароли должны быть одинаковые";


    /** Константы для сущности Role */

    public static final String ROLE_NOT_FOUND_MESSAGE = "Не найдена роль: ";

    /** Константы для настроек Security */

    public static final String SECURITY_ROLE_ADMIN_NAME = "ADMIN";
    public static final String SECURITY_ROLE_USER_NAME = "USER";


}
