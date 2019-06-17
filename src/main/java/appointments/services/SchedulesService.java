package appointments.services;

import appointments.domain.Schedule;
import appointments.dto.ScheduleDTO;
import appointments.exceptions.ScheduleNotFoundException;
import appointments.exceptions.ServiceNotFoundException;
import appointments.exceptions.SpecialistNotFoundException;
import appointments.mappers.ScheduleMapper;
import appointments.repos.SchedulesRepository;
import appointments.repos.ServicesRepository;
import appointments.repos.SpecialistsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static appointments.utils.Constants.SCHEDULE_EMPTY_END_TIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_ID_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_INTERVAL_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SERVICES_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_SPECIALIST_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_EMPTY_START_TIME_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_INCORRECT_DATE_MESSAGE;
import static appointments.utils.Constants.SCHEDULE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SERVICE_NOT_FOUND_MESSAGE;
import static appointments.utils.Constants.SPECIALIST_NOT_FOUND_MESSAGE;
import static java.util.stream.Collectors.toList;

/**
 * Класс, реализующий действия с объектами Расписание:
 * сохранение, удаление, получение списка расписаний
 *
 * @author yanchenko_evgeniya
 */
@Slf4j
@org.springframework.stereotype.Service
public class SchedulesService {

    /** Поле для хранения экземпляра репозитория */
    private SchedulesRepository schedulesRepository;

    /** Поле для хранения экземпляра репозитория услуг */
    private ServicesRepository servicesRepository;

    /** Поле для хранения экземпляра репозитория специалистов */
    private SpecialistsRepository specialistsRepository;

    /** Поле для хранения экземпляра маппера расписаний в DTO */
    private ScheduleMapper mapper;

    @Autowired
    public SchedulesService(
            SchedulesRepository schedulesRepository,
            ServicesRepository servicesRepository,
            SpecialistsRepository specialistsRepository,
            ScheduleMapper mapper
    ) {
        this.schedulesRepository = schedulesRepository;
        this.servicesRepository = servicesRepository;
        this.specialistsRepository = specialistsRepository;
        this.mapper = mapper;
    }

    /** Метод для добавления нового расписания */
    @Transactional
    public ScheduleDTO addSchedule(final ScheduleDTO dto) {

        if (dto.getSpecialistId() == null) {
            log.error("Parameter 'specialist' is null");
            throw new IllegalArgumentException(SCHEDULE_EMPTY_SPECIALIST_MESSAGE);
        }
        if (dto.getDate() == null || dto.getDate().isBefore(LocalDate.now())) {
            log.error("Value of parameter 'date' is incorrect or null: {}", dto.getDate());
            throw new IllegalArgumentException(SCHEDULE_INCORRECT_DATE_MESSAGE);
        }
        if (dto.getServiceIds() == null) {
            log.error("Parameter 'services' is null");
            throw new IllegalArgumentException(SCHEDULE_EMPTY_SERVICES_MESSAGE);
        }
        if (dto.getStartTime() == null) {
            log.error("Parameter 'startTime' is null");
            throw new IllegalArgumentException(SCHEDULE_EMPTY_START_TIME_MESSAGE);
        }
        if (dto.getEndTime() == null) {
            log.error("Parameter 'endTime' is null");
            throw new IllegalArgumentException(SCHEDULE_EMPTY_END_TIME_MESSAGE);
        }
        if (dto.getIntervalOfReception() == null) {
            log.error("Parameter 'interval' is null");
            throw new IllegalArgumentException(SCHEDULE_EMPTY_INTERVAL_MESSAGE);
        }

        final Schedule schedule = mapper.scheduleDTOToSchedule(dto);

        schedule.setId(null);
        schedule.setSpecialist(
                specialistsRepository
                        .findById(dto.getSpecialistId())
                        .orElseThrow(() -> new SpecialistNotFoundException(
                                SPECIALIST_NOT_FOUND_MESSAGE + dto.getSpecialistId()
                                )
                        )
        );
        schedule.setServices(
                dto.getServiceIds()
                        .stream()
                        .map(id -> servicesRepository
                                .findById(id)
                                .orElseThrow(() -> new ServiceNotFoundException(SERVICE_NOT_FOUND_MESSAGE + id)))
                        .collect(toList())
        );
        schedule.setReservations(new ArrayList<>());

        final Schedule savedSchedule = schedulesRepository.save(schedule);

        log.info("Added new schedule: {}", savedSchedule);

        return mapper.scheduleToScheduleDto(savedSchedule);
    }

    /** Метод для удаления расписания по идентификатору */
    @Transactional
    public void removeSchedule(final Long id) {

        if (id == null) {
            log.error(SCHEDULE_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SCHEDULE_EMPTY_ID_MESSAGE);
        }
        final Schedule schedule = schedulesRepository
                .findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE + id));

        schedulesRepository.delete(schedule);

        log.info("Schedule with id = {} deleted", id);
    }

    /** Метод для поиска расписания по идентификатору */
    @Transactional(readOnly = true)
    public ScheduleDTO findScheduleById(final Long id) {

        log.debug("Finding schedule with id = {}", id);

        if (id == null) {
            log.error(SCHEDULE_EMPTY_ID_MESSAGE);
            throw new IllegalArgumentException(SCHEDULE_EMPTY_ID_MESSAGE);
        }

        return mapper.scheduleToScheduleDto(
                schedulesRepository
                        .findById(id)
                        .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_MESSAGE + id))
        );
    }

    /** Метод для получения списка расписаний */
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getSchedules() {

        log.debug("Getting list of all schedules");

        return mapper.scheduleListToScheduleDTOList(schedulesRepository.findAll());
    }

    /** Метод для получения списка только активных расписаний */
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getActiveSchedules() {

        log.debug("Getting list of active schedules");

        return mapper.scheduleListToScheduleDTOList(
                schedulesRepository
                        .findAll()
                        .stream()
                        .filter(Schedule::isActive)
                        .collect(toList())
        );
    }
}
