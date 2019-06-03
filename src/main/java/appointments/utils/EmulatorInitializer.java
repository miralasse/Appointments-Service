package appointments.utils;

import appointments.domain.Child;
import appointments.domain.Organization;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Служебный класс, осуществляюший первичное наполнение коллекций эмулятора БД
 *
 * @author yanchenko_evgeniya
 */
public class EmulatorInitializer {

    /** Поля с числовыми константами для использования при наполнении коллекций, имитирующих таблицы БД */

    private final static int ID_FIRST = 1;
    private final static int ID_SECOND = 2;
    private final static int ID_THIRD = 3;
    private static final int ID_FOURTH = 4;
    private static final int ID_FIFTH = 5;

    private final static int BIRTH_CERTIFICATE_FIRST = 456845;
    private final static int BIRTH_CERTIFICATE_SECOND = 321856;
    private final static int BIRTH_CERTIFICATE_THIRD = 987523;

    private final static String ROOM_NUMBER_FIRST = "107";
    private final static String ROOM_NUMBER_SECOND = "109";
    private final static String ROOM_NUMBER_THIRD = "32";

    private final static int YEAR = 2019;
    private final static Duration INTERVAL = Duration.of(15, ChronoUnit.MINUTES);
    private final static int DAY_TWELVE = 12;
    private final static int DAY_FIFTEEN = 15;

    private final static LocalDate JULY_TWELVE = LocalDate.of(YEAR, Month.JULY, DAY_TWELVE);
    private final static LocalDate JULY_FIFTEEN = LocalDate.of(YEAR, Month.JULY, DAY_FIFTEEN);
    private final static LocalDate AUGUST_TWELVE = LocalDate.of(YEAR, Month.AUGUST, DAY_TWELVE);
    private final static LocalDate AUGUST_FIFTEEN = LocalDate.of(YEAR, Month.AUGUST, DAY_FIFTEEN);

    private final static LocalTime FIRST_OFFICE_HOURS_START = LocalTime.of(8, 0);
    private final static LocalTime FIRST_OFFICE_HOURS_END = LocalTime.of(13, 0);

    private final static LocalTime SECOND_OFFICE_HOURS_START = LocalTime.of(14, 0);
    private final static LocalTime SECOND_OFFICE_HOURS_END = LocalTime.of(19, 0);

    private final static LocalTime THIRD_OFFICE_HOURS_START = LocalTime.of(10, 0);
    private final static LocalTime THIRD_OFFICE_HOURS_END = LocalTime.of(15, 0);

    private final static LocalDateTime FIRST_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.JULY, DAY_TWELVE, 9, 15);
    private final static LocalDateTime SECOND_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_TWELVE, 9, 15);
    private final static LocalDateTime THIRD_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_FIFTEEN, 11, 30);
    private final static LocalDateTime FOURTH_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_TWELVE, 10, 15);
    private final static LocalDateTime FIFTH_RESERVATION_HOUR
            = LocalDateTime.of(YEAR, Month.AUGUST, DAY_FIFTEEN, 12, 30);


    /** Поля для хранения экземпляра эмулятора БД*/
    private static DatabaseEmulator databaseEmulator = DatabaseEmulator.getInstance();


    /** Метод для первичного наполнения всех коллекций */
    public void initAll() {
        databaseEmulator.getChildren().clear();
        databaseEmulator.getChildren().addAll(initChildren());

        databaseEmulator.getOrganizations().clear();
        databaseEmulator.getOrganizations().addAll(initOrganizations());

        databaseEmulator.getServices().clear();
        databaseEmulator.getServices().addAll(initServices());

        databaseEmulator.getSpecialists().clear();
        databaseEmulator.getSpecialists().addAll(initSpecialists());

        databaseEmulator.getSchedules().clear();
        databaseEmulator.getSchedules().addAll(initSchedules());

        databaseEmulator.getReservations().clear();
        databaseEmulator.getReservations().addAll(initReservations());

        bindScheduleWithReservations();
    }

    /** Метод для первичного наполнения коллекции объектов Child */
    public List<Child> initChildren() {

        return new ArrayList<>(Arrays.asList(
                new Child(
                        ID_FIRST, "SI", BIRTH_CERTIFICATE_FIRST,
                        "+7(910)452-85-96", "example1@mail.ru",
                        "Иванов", "Иван", "Иванович"
                ),
                new Child(
                        ID_SECOND, "GI", BIRTH_CERTIFICATE_SECOND,
                        "+7(910)785-12-36", "example2@mail.ru",
                        "Кузнецова", "Елена", "Александровна"
                ),
                new Child(
                        ID_THIRD, "DI",
                        BIRTH_CERTIFICATE_THIRD, "+7(920)623-85-47", "example3@mail.ru",
                        "Сергеев", "Антон", "Олегович"
                )
        ));
    }

    /** Метод для первичного наполнения коллекции объектов Organization */
    public List<Organization> initOrganizations() {

        return new ArrayList<>(Arrays.asList(
                new Organization(
                        ID_FIRST, "Управление образования г. Белгород",
                        "г. Белгород, ул. Попова 25а", "+7(4722)32-68-95"
                ),
                new Organization(
                        ID_SECOND, "Управление образования г. Старый Оскол",
                        "Белгородская обл., г. Старый Оскол, ул. Комсомольская, 43", "+7(4725)22‑03-38"
                )
        ));
    }

    /** Метод для первичного наполнения коллекции объектов Service */
    public List<Service> initServices() {

        return new ArrayList<>(Arrays.asList(
                new Service(ID_FIRST, "Постановка на очередь", true),
                new Service(ID_SECOND, "Получение путевки в ДОО", true)
        ));
    }

    /** Метод для первичного наполнения коллекции объектов Specialist */
    public List<Specialist> initSpecialists() {

        return new ArrayList<>(Arrays.asList(
                new Specialist(
                        ID_FIRST, "Специалист 1", ROOM_NUMBER_FIRST, true,
                        databaseEmulator.getOrganizations().get(ID_FIRST - 1)
                ),
                new Specialist(
                        ID_SECOND, "Специалист 2", ROOM_NUMBER_SECOND, true,
                        databaseEmulator.getOrganizations().get(ID_FIRST - 1)
                ),
                new Specialist(
                        ID_THIRD, "Специалист 3", ROOM_NUMBER_THIRD, true,
                        databaseEmulator.getOrganizations().get(ID_SECOND - 1)
                )
        ));
    }

    /** Метод для первичного наполнения коллекции объектов Schedule */
    public List<Schedule> initSchedules() {

        return new ArrayList<>(Arrays.asList(
                new Schedule(
                        (long)ID_FIRST,
                        databaseEmulator.getSpecialists().get(ID_FIRST - 1),
                        JULY_TWELVE,
                        new ArrayList<>(databaseEmulator.getServices()),
                        FIRST_OFFICE_HOURS_START,
                        FIRST_OFFICE_HOURS_END,
                        INTERVAL,
                        new ArrayList<>(),
                        true
                ),
                new Schedule(
                        (long)ID_SECOND,
                        databaseEmulator.getSpecialists().get(ID_THIRD - 1),
                        JULY_FIFTEEN,
                        new ArrayList<>(databaseEmulator.getServices()),
                        THIRD_OFFICE_HOURS_START,
                        THIRD_OFFICE_HOURS_END,
                        INTERVAL,
                        new ArrayList<>(),
                        true
                ),
                new Schedule(
                        (long)ID_THIRD,
                        databaseEmulator.getSpecialists().get(ID_FIRST - 1),
                        AUGUST_TWELVE,
                        new ArrayList<>(databaseEmulator.getServices()),
                        FIRST_OFFICE_HOURS_START,
                        FIRST_OFFICE_HOURS_END,
                        INTERVAL,
                        new ArrayList<>(),
                        true
                ),
                new Schedule(
                        (long)ID_FOURTH,
                        databaseEmulator.getSpecialists().get(ID_SECOND - 1),
                        AUGUST_FIFTEEN,
                        new ArrayList<>(databaseEmulator.getServices()),
                        SECOND_OFFICE_HOURS_START,
                        SECOND_OFFICE_HOURS_END,
                        INTERVAL,
                        new ArrayList<>(),
                        true
                )
        ));
    }

    /** Метод для первичного наполнения коллекции объектов Reservation */
    public List<Reservation> initReservations() {

        return new ArrayList<>(Arrays.asList(
                new Reservation(
                        (long)ID_FIRST,
                        FIRST_RESERVATION_HOUR,
                        databaseEmulator.getSchedules().get(ID_FIRST - 1),
                        databaseEmulator.getServices().get(ID_FIRST - 1),
                        true,
                        databaseEmulator.getChildren().get(ID_FIRST - 1)
                ),
                new Reservation(
                        (long)ID_SECOND,
                        SECOND_RESERVATION_HOUR,
                        databaseEmulator.getSchedules().get(ID_THIRD - 1),
                        databaseEmulator.getServices().get(ID_FIRST - 1),
                        true,
                        databaseEmulator.getChildren().get(ID_FIRST - 1)
                ),
                new Reservation(
                        (long)ID_THIRD,
                        THIRD_RESERVATION_HOUR,
                        databaseEmulator.getSchedules().get(ID_FOURTH - 1),
                        databaseEmulator.getServices().get(ID_SECOND - 1),
                        true,
                        databaseEmulator.getChildren().get(ID_THIRD - 1)
                ),
                new Reservation(
                        (long)ID_FOURTH,
                        FOURTH_RESERVATION_HOUR,
                        databaseEmulator.getSchedules().get(ID_THIRD - 1),
                        databaseEmulator.getServices().get(ID_FIRST - 1),
                        true,
                        databaseEmulator.getChildren().get(ID_FIRST - 1)
                ),
                new Reservation(
                        (long)ID_FIFTH,
                        FIFTH_RESERVATION_HOUR,
                        databaseEmulator.getSchedules().get(ID_FOURTH - 1),
                        databaseEmulator.getServices().get(ID_SECOND - 1),
                        true,
                        databaseEmulator.getChildren().get(ID_THIRD - 1)
                )
        ));
    }

    /** Метод для вставки созданных объектов Reservation в созданные ранее объекты Schedule */
    private void bindScheduleWithReservations() {
        for (Schedule schedule: databaseEmulator.getSchedules()) {
            for (Reservation reservation: databaseEmulator.getReservations()) {
                if (reservation.getSchedule().equals(schedule)) {
                    schedule.getReservations().add(reservation);
                }
            }
        }
    }
}
