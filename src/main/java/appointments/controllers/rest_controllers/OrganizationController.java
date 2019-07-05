package appointments.controllers.rest_controllers;

import appointments.repos.OrganizationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Класс-контроллер для получения организации по-умолчанию
 *
 * @author yanchenko_evgeniya
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    private OrganizationsRepository organizationsRepository;

    @Autowired
    public OrganizationController(OrganizationsRepository organizationsRepository) {
        this.organizationsRepository = organizationsRepository;
    }

    /** Метод, возвращающий организацию по-умолчанию */
    @GetMapping
    public ResponseEntity<?> getOrganization() {

        return new ResponseEntity<>(
                organizationsRepository.findAll().get(0),
                HttpStatus.OK
        );
    }
}
