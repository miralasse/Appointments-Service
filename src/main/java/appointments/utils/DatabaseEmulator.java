package appointments.utils;

import appointments.domain.Child;
import appointments.domain.Organization;
import appointments.domain.Reservation;
import appointments.domain.Schedule;
import appointments.domain.Service;
import appointments.domain.Specialist;
import appointments.exceptions.ReservationNotFoundException;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.exceptions.ServiceNotFoundException;
import appointments.exceptions.SpecialistNotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс-синглтон с коллекциями основных сущностей (эмулятор БД)
 *
 * @author yanchenko_evgeniya
 */
@Getter
@Setter
public class DatabaseEmulator {

    /** Поле для хранение экземпляра сиглтона */
    private static DatabaseEmulator instance;

    /** Поле Список детей */
    private List<Child> children = new ArrayList<>();

    /** Поле Список организаций */
    private List<Organization> organizations = new ArrayList<>();

    /** Поле Список услуг (целей обращения) */
    private List<Service> services = new ArrayList<>();

    /** Поле Список специалистов */
    private List<Specialist> specialists = new ArrayList<>();

    /** Поле Список расписаний */
    private List<Schedule> schedules = new ArrayList<>();

    /** Поле Список записей на прием */
    private List<Reservation> reservations = new ArrayList<>();

    private DatabaseEmulator() {

    }

    /** Метод для получения экземпляра синглтона */
    public static DatabaseEmulator getInstance() {

        if (instance == null) {
            instance = new DatabaseEmulator();
        }

        return instance;
    }


    /** Метод для добавления новой цели обращения (услуги) в справочник */
    public Integer addService(Service service) {
        int id = services.size() + 1;
        service.setId(id);
        services.add(service);

        return id;
    }

    /** Метод для поиска конкретной цели обращения (услуги) по идентификатору */
    public Service findServiceById(int id) {

        try {
            return services.get(id - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceNotFoundException("Услуга с ID=" + id + " не найдена");
        }

    }

    /** Метод для удаления цели обращения (услуги) */
    public boolean removeService(Service service) {
        return services.remove(service);
    }

    /** Метод для получения списка целей обращения (услуг) */
    public List<Service> getServices() {
        return services;
    }


    /** Метод для добавления нового специалиста в справочник */
    public Integer addSpecialist(Specialist specialist) {

        int id = specialists.size() + 1;
        specialist.setId(id);
        specialists.add(specialist);

        return id;
    }

    /** Метод для обновления информации о специалисте */
    public void updateSpecialist(Specialist specialist) {
        specialists.set(specialist.getId() - 1, specialist);

    }

    /** Метод для поиска конкретного специалиста по идентификатору */
    public Specialist findSpecialistById(int id) {

        try {
            return specialists.get(id - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new SpecialistNotFoundException("Специалист с ID=" + id + " не найден");
        }
    }

    /** Метод для удаления специалиста */
    public boolean removeSpecialist(Specialist specialist) {
        return specialists.remove(specialist);
    }

    /** Метод для получения списка специалистов */
    public List<Specialist> getSpecialists() {
        return specialists;
    }


    /** Метод для добавления расписания */
    public Long addSchedule(Schedule schedule) {

        long id = schedules.size() + 1;
        schedule.setId(id);
        schedules.add(schedule);

        return id;
    }

    /** Метод для поиска конкретного расписания по идентификатору */
    public Schedule findScheduleById(long id) {

        try {
            return schedules.get((int)id - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new ScheduleNotFoundException("Расписание с ID=" + id + " не найдено");
        }
    }

    /** Метод для удаления расписания */
    public boolean removeSchedule(Schedule schedule) {
        return schedules.remove(schedule);
    }

    /** Метод для получения списка расписаний */
    public List<Schedule> getSchedules() {
        return schedules;
    }

    /** Метод для добавления записи на прием */
    public Long addReservation(Reservation reservation) {

        long id = reservations.size() + 1;
        reservation.setId(id);
        reservations.add(reservation);

        return id;
    }

    /** Метод для поиска конкретной записи на прием по идентификатору */
    public Reservation findReservationById(long id) {

        try {
            return reservations.get((int)id - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new ReservationNotFoundException("Запись на прием с ID=" + id + " не была найдена");
        }
    }


    /** Метод для удаления записи на прием */
    public boolean removeReservation(Reservation reservation) {
        return reservations.remove(reservation);
    }

    /** Метод для получения списка всех записей на прием */
    public List<Reservation> getReservations() {
        return reservations;
    }




}