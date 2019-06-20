package appointments.controllers.rest_controllers;

import appointments.dto.ActiveDTO;
import appointments.dto.SpecialistDTO;
import appointments.services.SpecialistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/** Класс-контроллер для справочника Специалисты
 *
 * @author yanchenko_evgeniya
 */
@RestController
@RequestMapping("/specialists")
public class SpecialistsController {

    private SpecialistsService specialistsService;


    @Autowired
    public SpecialistsController(SpecialistsService specialistsService) {
        this.specialistsService = specialistsService;
    }


    /** Метод, возвращающий всех специалистов */
    @GetMapping
    public ResponseEntity<List<SpecialistDTO>> getAllSpecialists() {

        return new ResponseEntity<>(
                specialistsService.getSpecialists(),
                HttpStatus.OK
        );
    }

    /** Метод, возвращающий только активных специалистов */
    @GetMapping("/active")
    public ResponseEntity<List<SpecialistDTO>> getActiveSpecialists() {

        return new ResponseEntity<>(
                specialistsService.getActiveSpecialists(),
                HttpStatus.OK
        );
    }

    /** Метод, возвращающий одного специалиста, найденного по указанному идентификатору */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialistDTO> getSpecialistById(@PathVariable Integer id) {

        return new ResponseEntity<>(
                specialistsService.findSpecialistById(id),
                HttpStatus.OK
        );
    }

    /** Метод, осуществляющий удаление специалиста с указанным идентификатором */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpecialistById(@PathVariable Integer id) {

        specialistsService.removeSpecialist(id);

        return ResponseEntity.noContent().build();
    }

    /** Метод, осуществляющий добавление нового специалиста со статусом "Активен" */
    @PostMapping
    public ResponseEntity<?> addNewSpecialist(@Valid @RequestBody SpecialistDTO specialistDto) {

        return new ResponseEntity<>(
                specialistsService.addSpecialist(specialistDto),
                HttpStatus.CREATED
        );

    }

    /** Метод, осуществляющий редактирование существующего специалиста */
    @PutMapping
    public ResponseEntity<?> updateSpecialist(@Valid @RequestBody SpecialistDTO specialistDto) {

        specialistsService.editSpecialist(specialistDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /** Метод, осуществляющий смену статуса активности специалиста на указанный статус */
    @PatchMapping("/{id}")
    public ResponseEntity<String> changeSpecialistStatus(
            @PathVariable Integer id,
            @RequestBody ActiveDTO activeDTO
    ) {
        specialistsService.changeActiveState(id, activeDTO.isActive());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
