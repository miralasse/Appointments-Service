package appointments.controllers.rest_controllers;

import appointments.dto.ReservationDTO;
import appointments.services.ReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/reservations")
public class ReservationsController {

    private ReservationsService reservationsService;

    @Autowired
    public ReservationsController(ReservationsService reservationsService) {
        this.reservationsService = reservationsService;
    }

    /** Метод, возвращающий все записи на прием во всех расписаниях */
    @GetMapping
    public ResponseEntity<?> getAllReservations(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {

        return new ResponseEntity<>(
                reservationsService.getReservations(date, startDate, endDate),
                HttpStatus.OK
        );
    }


    /** Метод, возвращающий одну запись на прием, найденную по указанному идентификатору */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {

        return new ResponseEntity<>(
                reservationsService.findReservationById(id),
                HttpStatus.OK
        );
    }

    /** Метод, осуществляющий добавление новой записи на прием */
    @PostMapping
    public ResponseEntity<?> addNewReservation(@Valid @RequestBody ReservationDTO reservationDTO) {

        return new ResponseEntity<>(
                reservationsService.addReservation(reservationDTO),
                HttpStatus.CREATED
        );
    }
}
