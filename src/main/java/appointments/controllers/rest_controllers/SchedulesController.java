package appointments.controllers.rest_controllers;

import appointments.dto.ScheduleDTO;
import appointments.services.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author yanchenko_evgeniya
 */
@RestController
@RequestMapping("/schedules")
public class SchedulesController {


    private SchedulesService schedulesService;

    @Autowired
    public SchedulesController(SchedulesService schedulesService) {
        this.schedulesService = schedulesService;
    }

    /** Метод, возвращающий все расписания */
    @GetMapping
    public ResponseEntity<Page<ScheduleDTO>> getAllSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
            Pageable pageable
    ) {

        return new ResponseEntity<>(
                schedulesService.getSchedules(pageable, date),
                HttpStatus.OK
        );
    }

    /** Метод, возвращающий одно расписание, найденное по указанному идентификатору */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {

        return new ResponseEntity<>(
                schedulesService.findScheduleById(id),
                HttpStatus.OK
        );
    }

    /** Метод, осуществляющий удаление расписания с указанным идентификатором */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScheduleById(@PathVariable Long id) {

        schedulesService.removeSchedule(id);

        return ResponseEntity.noContent().build();
    }

    /** Метод, осуществляющий добавление нового расписания */
    @PostMapping
    public ResponseEntity<?> addNewSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {

        return new ResponseEntity<>(
                schedulesService.addSchedule(scheduleDTO),
                HttpStatus.CREATED
        );
    }
}
