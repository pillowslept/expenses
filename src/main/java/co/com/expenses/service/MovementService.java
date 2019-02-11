package co.com.expenses.service;

import java.time.Month;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.DateUtilities;
import co.com.expenses.dto.MovementSummary;
import co.com.expenses.dto.Params;
import co.com.expenses.exception.ValidateException;
import co.com.expenses.model.Category;
import co.com.expenses.model.Movement;
import co.com.expenses.model.Type;
import co.com.expenses.repository.MovementRepository;
import co.com.expenses.util.Validations;

@Service
@Transactional
public class MovementService {

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

    @Autowired
    DateUtilities dateUtilities;

    public MovementSummary findById(Long id) {
        java.lang.reflect.Type targetListType = new TypeToken<MovementSummary>() {}.getType();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(movementRepository.findOne(id), targetListType);
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
                .creationDate(dateUtilities.toTimestamp(params.getDate()))
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
        dateUtilities.validateStringInFormat(params.getDate());
    }

    public Movement validateAndFind(Long id) {
        validateId(id);
        Movement movement = movementRepository.findOne(id);
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

    public List<MovementSummary> findAll() {
        return mapResults(findAllByOrderByCreationDateAsc());
    }

    public List<Movement> findAllByOrderByCreationDateAsc() {
        return movementRepository.findAllByOrderByCreationDateAsc();
    }

    public List<Movement> findByCreationDateBetween(int month) {
        Date startDate = dateUtilities.obtainBeginingOfDate(month);
        Date endDate = dateUtilities.obtainEndOfDate(month);
        return movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate);
    }

    public List<Movement> findByCreationDateOfYear(int year) {
        Date startDate = dateUtilities.obtainBeginingOfDate(Month.JANUARY.getValue(), year);
        Date endDate = dateUtilities.obtainEndOfDate(Month.DECEMBER.getValue(), year);
        return movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate);
    }

    public List<Movement> findByCreationDateBetween(int month, int year) {
        Date startDate = dateUtilities.obtainBeginingOfDate(month, year);
        Date endDate = dateUtilities.obtainEndOfDate(month, year);
        return movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate);
    }

    public List<MovementSummary> findAllPageable(int pageNumber, int pageSize) {
        Pageable pageable = new PageRequest(pageNumber, pageSize, org.springframework.data.domain.Sort.Direction.ASC, "creationDate");
        Page<Movement> movements = movementRepository.findAll(pageable);
        return mapResults(movements.getContent());
    }

    public List<MovementSummary> findByCreationDateBetweenAndPageable(int month, int year, int pageNumber, int pageSize) {
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        Date startDate = dateUtilities.obtainBeginingOfDate(month, year);
        Date endDate = dateUtilities.obtainEndOfDate(month, year);
        return mapResults(movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate, pageable));
    }

    private List<MovementSummary> mapResults(List<Movement> results) {
        java.lang.reflect.Type targetListType = new TypeToken<List<MovementSummary>>() {}.getType();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(results, targetListType);
    }

}
