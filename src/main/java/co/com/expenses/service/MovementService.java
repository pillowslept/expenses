package co.com.expenses.service;

import static co.com.expenses.util.Constants.DEFAULT_FIELD_VALIDATION;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.DateUtilities;
import co.com.expenses.component.Messages;
import co.com.expenses.dto.MovementSummary;
import co.com.expenses.dto.PageableSummary;
import co.com.expenses.dto.Params;
import co.com.expenses.exception.NotFoundException;
import co.com.expenses.exception.ValidateException;
import co.com.expenses.model.Category;
import co.com.expenses.model.Movement;
import co.com.expenses.model.Type;
import co.com.expenses.repository.MovementRepository;
import co.com.expenses.specifications.MovementFilterSpecification;
import co.com.expenses.util.ObjectMapperUtils;
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

    private Movement findById(Long id) {
        Optional<Movement> movement = movementRepository.findById(id);
        if (!movement.isPresent()) {
            throw new NotFoundException(String.format(messages.get("movement.not.found"), id));
        }

        return movement.get();
    }

    public MovementSummary findByIdMapped(Long id) {
        return this.mapMovement(this.findById(id));
    }

    private MovementSummary mapMovement(Movement movement) {
        return ObjectMapperUtils.map(movement, MovementSummary.class);
    }

    public MovementSummary create(Params params) {
        this.validateCreate(params);
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

        return this.mapMovement(movement);
    }

    private void validateCreate(Params params) {
        if (Validations.field(params.getValue())) {
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "value"));
        }
        if (Validations.field(params.getDate())) {
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "date"));
        }
        dateUtilities.toTimestamp(params.getDate());
    }

    public Movement validateAndFind(Long id) {
        if (Validations.field(id)) {
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "movementId"));
        }

        return this.findById(id);
    }

    public MovementSummary update(Long id, Params params) {
        Movement movement = this.validateAndFind(id);
        this.validateCreate(params);
        Type type = typeService.validateAndFind(params.getTypeId());
        Category category = categoryService.validateAndFind(params.getCategoryId());

        movement.setType(type);
        movement.setCategory(category);
        movement.setValue(params.getValue());
        movement.setObservations(params.getObservations());
        movement.setCreationDate(dateUtilities.toTimestamp(params.getDate()));
        movementRepository.save(movement);

        return this.mapMovement(movement);
    }

    public List<MovementSummary> findAll(Integer pageNumber, Integer pageSize) {
        List<MovementSummary> movementsSummary;
        if (this.applyPaginable(pageSize)) {
            movementsSummary = this.findAllPageable(pageNumber, pageSize);
        } else {
            movementsSummary = this.findAllByOrderByCreationDateAsc();
        }

        return movementsSummary;
    }

    private boolean applyPaginable(Integer pageSize) {
        return pageSize != null && pageSize.intValue() != 0;
    }

    public List<MovementSummary> findAllByOrderByCreationDateAsc() {
        return this.mapResults(movementRepository.findAllByOrderByCreationDateAsc());
    }

    public List<MovementSummary> findByCreationDateBetween(int month) {
        Date startDate = dateUtilities.obtainBeginingOfDate(month);
        Date endDate = dateUtilities.obtainEndOfDate(month);

        return mapResults(movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
    }

    public List<MovementSummary> findByCreationDateOfYear(int year) {
        Date startDate = dateUtilities.obtainBeginingOfDate(Month.JANUARY.getValue(), year);
        Date endDate = dateUtilities.obtainEndOfDate(Month.DECEMBER.getValue(), year);

        return this.mapResults(movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
    }

    public List<MovementSummary> findByCreationDateBetween(int month, int year) {
        Date startDate = dateUtilities.obtainBeginingOfDate(month, year);
        Date endDate = dateUtilities.obtainEndOfDate(month, year);

        return this.mapResults(movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
    }

    public List<MovementSummary> findAllPageable(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, ASC, "creationDate");
        Page<Movement> movements = movementRepository.findAll(pageable);

        return this.mapResults(movements.getContent());
    }

    public List<MovementSummary> findByCreationDateBetween(int month, int year, Integer pageNumber, Integer pageSize) {
        List<MovementSummary> movementsSummary;
        Date startDate = dateUtilities.obtainBeginingOfDate(month, year);
        Date endDate = dateUtilities.obtainEndOfDate(month, year);
        if (this.applyPaginable(pageSize)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            movementsSummary = this.mapResults(
                    movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate, pageable));
        } else {
            movementsSummary = this.mapResults(
                    movementRepository.findByCreationDateBetweenOrderByCreationDateAsc(startDate, endDate));
        }

        return movementsSummary;
    }

    public PageableSummary byFilters(Long value, Integer month, Integer year, Integer pageSize, Integer pageNumber, String sortType) {
        boolean areValidDates = month != null && year != null;
        Date startDate = areValidDates ? dateUtilities.obtainBeginingOfDate(month.intValue(), year) : null;
        Date endDate = areValidDates ? dateUtilities.obtainEndOfDate(month.intValue(), year) : null;
        Pageable pageable = this.buildPageable(pageSize, pageNumber, sortType);

        Page<Movement> paged = movementRepository
                .findAll(
                        Specification.where(MovementFilterSpecification.withFilter(value, "value"))
                                .and(MovementFilterSpecification.withFilterBetween(startDate, endDate, "creationDate")),
                        pageable);

        return ObjectMapperUtils.map(paged, PageableSummary.class);
    }

    private Pageable buildPageable(Integer pageSize, Integer pageNumber, String sortType) {
        int size = !Validations.field(pageSize) ? pageSize.intValue() : 10;
        int page = !Validations.field(pageNumber) ? pageNumber.intValue() : 0;

        return PageRequest.of(page, size, this.getSortType(sortType), "creationDate");
    }
    
    private Direction getSortType(String sortType) {
        return !Validations.field(sortType) && sortType.equalsIgnoreCase(DESC.toString()) ? DESC : ASC;
    }

    private List<MovementSummary> mapResults(List<Movement> results) {
        return ObjectMapperUtils.mapAll(results, MovementSummary.class);
    }

}
