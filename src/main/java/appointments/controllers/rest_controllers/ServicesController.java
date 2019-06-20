package appointments.controllers.rest_controllers;

import appointments.domain.Service;
import appointments.dto.ActiveDTO;
import appointments.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/** Класс-контроллер для справочника Услуги
 *
 * @author yanchenko_evgeniya
 */
@RestController
@RequestMapping("/services")
public class ServicesController {

    private ServicesService servicesService;

    @Autowired
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }


    /** Метод, возвращающий все услуги */
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {

        return new ResponseEntity<>(
                servicesService.getServices(),
                HttpStatus.OK
        );
    }

    /** Метод, возвращающий только активные услуги */
    @GetMapping("/active")
    public ResponseEntity<List<Service>> getActiveServices() {

        return new ResponseEntity<>(
                servicesService.getActiveServices(),
                HttpStatus.OK
        );
    }

    /** Метод, возвращающий одну услугу, найденную по указанному идентификатору услуги */
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Integer id) {

        return new ResponseEntity<>(
                servicesService.findServiceById(id),
                HttpStatus.OK
        );
    }

    /** Метод, осуществляющий удаление услуги с указанным идентификатором */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteServiceById(@PathVariable Integer id) {

        servicesService.removeService(id);

        return ResponseEntity.noContent().build();
    }

    /** Метод, осуществляющий добавление новой услуги с указанным именем и статусом "Активна" */
    @PostMapping
    public ResponseEntity<?> addNewService(@Valid @RequestBody Service service) {

        return new ResponseEntity<>(
                servicesService.addService(service.getName(), true),
                HttpStatus.CREATED
        );
    }

    /** Метод, осуществляющий смену статуса активности услуги на указанный статус */
    @PatchMapping("/{id}")
    public ResponseEntity<String> changeServiceStatus(
            @PathVariable Integer id,
            @RequestBody ActiveDTO activeDTO
    ) {

        servicesService.changeActiveState(id, activeDTO.isActive());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
