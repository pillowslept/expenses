package co.com.expenses.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.dto.Params;
import co.com.expenses.exception.ValidateException;
import co.com.expenses.model.Category;
import co.com.expenses.model.Movement;
import co.com.expenses.model.Type;
import co.com.expenses.repository.MovementRepository;
import co.com.expenses.util.DateUtilities;
import co.com.expenses.util.Validations;

@Service
@Transactional
public class MovementService {

    private static final String NOT_A_VALID_MONTH = "El mes indicado para la búsqueda no es válido.";
    private static final String MOVEMENT_CREATED = "Movimiento creado con éxito, identificador generado es <%d>";
    private static final String MOVEMENT_NOT_FOUND = "El movimiento con identificador <%d> no existe en la base de datos";
    private static final String VALUE_NOT_VALID = "El campo <value> no es válido";
    private static final String DATE_NOT_VALID = "El campo <date> no es válido";
    private static final String MOVEMENT_ID_NOT_VALID = "El campo <movementId> no es válido";

    @Autowired
    MovementRepository movementRepository;

    @Autowired
    TypeService typeService;

    @Autowired
    CategoryService categoryService;

    public Movement findById(Long id) {
        return movementRepository.findOne(id);
    }

    public String create(Params params) {
        validateCreate(params);
        Type type = typeService.validateAndFind(params.getTypeId());
        Category category = categoryService.validateAndFind(params.getCategoryId());
        Movement movement = Movement.builder()
                .type(type)
                .category(category)
                .value(params.getValue())
                .observations(params.getObservations())
                .creationDate(DateUtilities.toTimestamp(params.getDate()))
                .build();
        movementRepository.save(movement);
        return String.format(MOVEMENT_CREATED, movement.getId());
    }

    private void validateCreate(Params params) {
        if (Validations.field(params.getValue())) {
            throw new ValidateException(VALUE_NOT_VALID);
        }
        if (Validations.field(params.getDate())) {
            throw new ValidateException(DATE_NOT_VALID);
        }
        DateUtilities.validateStringInFormat(params.getDate());
    }

    public Movement validateAndFind(Long id) {
        validateId(id);
        Movement movement = findById(id);
        if (movement == null) {
            throw new ValidateException(String.format(MOVEMENT_NOT_FOUND, id));
        }
        return movement;
    }

    private void validateId(Long id) {
        if (Validations.field(id)) {
            throw new ValidateException(MOVEMENT_ID_NOT_VALID);
        }
    }

    public void update(Movement movement) {
        movementRepository.save(movement);
    }

    public List<Movement> findAll() {
        return movementRepository.findAll();
    }

    public List<Movement> findAllByOrderByCreationDateAsc() {
        return movementRepository.findAllByOrderByCreationDateAsc();
    }

    public List<Movement> findByCreationDateBetween(int month) {
        if (DateUtilities.VALID_MONTHS.indexOf(month) == -1) {
            throw new ValidateException(NOT_A_VALID_MONTH);
        }
        Date startDate = DateUtilities.obtainBeginingOfDate(month);
        Date endDate = DateUtilities.obtainEndOfDate(month);
        return movementRepository.findByCreationDateBetween(startDate, endDate);
    }
}
