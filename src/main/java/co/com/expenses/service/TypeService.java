package co.com.expenses.service;

import static co.com.expenses.util.Constants.DEFAULT_FIELD_VALIDATION;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.Messages;
import co.com.expenses.dto.Params;
import co.com.expenses.dto.TypeSummary;
import co.com.expenses.enums.State;
import co.com.expenses.exception.ValidateException;
import co.com.expenses.model.Type;
import co.com.expenses.repository.TypeRepository;
import co.com.expenses.util.ObjectMapperUtils;
import co.com.expenses.util.Validations;

@Service
@Transactional
public class TypeService {

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    Messages messages;

    public Type findById(Long id) {
        Optional<Type> type = typeRepository.findById(id);
        if (!type.isPresent()) {
            throw new ValidateException(String.format(messages.get("type.not.found"), id));
        }
        return type.get();
    }

    public String create(Params params) {
        validateCreate(params);
        Type type = Type.builder()
                .description(params.getDescription())
                .state(State.ACTIVE.get())
                .build();
        typeRepository.save(type);
        return String.format(messages.get("type.created"), type.getId());
    }

    private void validateExistence(String description) {
        Type type = typeRepository.findByDescriptionIgnoreCase(description);
        if(type != null) {
            throw new ValidateException(String.format(messages.get("type.exists"), description));
        }
    }

    private void validateCreate(Params params) {
        if(Validations.field(params.getDescription())){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "description"));
        }
        validateExistence(params.getDescription());
    }

    public String inactivate(Long id) {
        Type type = validateAndFind(id);
        type.setState(State.INACTIVE.get());
        update(type);
        return String.format(messages.get("type.inactivated"), id);
    }

    public String activate(Long id) {
        Type type = validateAndFind(id);
        type.setState(State.ACTIVE.get());
        update(type);
        return String.format(messages.get("type.inactivated"), id);
    }

    public Type validateAndFind(Long id) {
        validateId(id);
        return findById(id);
    }

    private void validateId(Long id) {
        if(Validations.field(id)){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "typeId"));
        }
    }

    public void update(Type type) {
        typeRepository.save(type);
    }

    public List<TypeSummary> findAll() {
        return ObjectMapperUtils.mapAll(typeRepository.findByState(State.ACTIVE.get()), TypeSummary.class);
    }
}
