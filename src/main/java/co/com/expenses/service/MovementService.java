package co.com.expenses.service;

import static co.com.expenses.util.Constants.DEFAULT_FIELD_VALIDATION;
import static org.springframework.data.domain.Sort.Direction.ASC;

import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.DateUtilities;
import co.com.expenses.component.Messages;
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

    @Autowired
    MovementRepository movementRepository;

    @Autowired
    TypeService typeService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DateUtilities dateUtilities;

    @Autowired
    Messages messages;

    public Movement findById(Long id) {
        Optional<Movement> movement = movementRepository.findById(id);
        if (!movement.isPresent()) {
            throw new ValidateException(String.format(messages.get("movement.not.found"), id));
        }
        return movement.get();
    }

    public MovementSummary findByIdMapped(Long id) {
        Movement movement = findById(id);
        java.lang.reflect.Type targetListType = new TypeToken<MovementSummary>() {}.getType();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(movement, targetListType);
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
        return String.format(messages.get("movement.created"), movement.getId());
    }

    private void validateCreate(Params params) {
        if (Validations.field(params.getValue())) {
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "value"));
        }
        if (Validations.field(params.getDate())) {
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "date"));
        }
        dateUtilities.validateStringInFormat(params.getDate());
    }

    public Movement validateAndFind(Long id) {
        validateId(id);
        return findById(id);
    }

    private void validateId(Long id) {
        if (Validations.field(id)) {
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "movementId"));
        }
    }

    public void update(Long id, Params params) {
        Movement movement = validateAndFind(id);
        movementRepository.save(movement);
    }

    public List<MovementSummary> findAll(Integer pageNumber, Integer pageSize) {
        List<MovementSummary> movementsSummary;
        if (applyPaginable(pageSize)) {
            movementsSummary = findAllPageable(pageNumber, pageSize);
        } else {
            movementsSummary = findAllByOrderByCreationDateAsc();
        }
        return movementsSummary;
    }

    private boolean applyPaginable(Integer pageSize) {
        return pageSize != null && pageSize.intValue() != 0;
    }

    public List<MovementSummary> findAllByOrderByCreationDateAsc() {
        return mapResults(movementRepository.findAllByOrderByCreationDateAsc());
    }

    public List<MovementSummary> findByCreationDateBetween(int month) {
        Date startDate = dateUtilities.obtainBeginingOfDate(month);
        Date endDate = dateUtilities.obtainEndOfDate(month);
        return mapResults(movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
    }

    public List<MovementSummary> findByCreationDateOfYear(int year) {
        Date startDate = dateUtilities.obtainBeginingOfDate(Month.JANUARY.getValue(), year);
        Date endDate = dateUtilities.obtainEndOfDate(Month.DECEMBER.getValue(), year);
        return mapResults(movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
    }

    public List<MovementSummary> findByCreationDateBetween(int month, int year) {
        Date startDate = dateUtilities.obtainBeginingOfDate(month, year);
        Date endDate = dateUtilities.obtainEndOfDate(month, year);
        return mapResults(movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
    }

    public List<MovementSummary> findAllPageable(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, ASC, "creationDate");
        Page<Movement> movements = movementRepository.findAll(pageable);
        return mapResults(movements.getContent());
    }

    public List<MovementSummary> findByCreationDateBetween(int month, int year, Integer pageNumber, Integer pageSize) {
        List<MovementSummary> movementsSummary;
        Date startDate = dateUtilities.obtainBeginingOfDate(month, year);
        Date endDate = dateUtilities.obtainEndOfDate(month, year);
        if (applyPaginable(pageSize)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            movementsSummary = mapResults(
                    movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate, pageable));
        } else {
            movementsSummary = mapResults(
                    movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
        }
        return movementsSummary;
    }

    private List<MovementSummary> mapResults(List<Movement> results) {
        java.lang.reflect.Type targetListType = new TypeToken<List<MovementSummary>>() {}.getType();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(results, targetListType);
    }

}
