package co.com.expenses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.dto.Params;
import co.com.expenses.dto.Util;
import co.com.expenses.enums.State;
import co.com.expenses.exception.ValidateException;
import co.com.expenses.model.Type;
import co.com.expenses.repository.TypeRepository;
import co.com.expenses.util.ObjectMapperUtils;
import co.com.expenses.util.Validations;

@Service
@Transactional
public class TypeService {

    private static final String TYPE_CREATED = "El tipo de movimiento ha sido creado con éxito, el identificador generado es <%d>";
    private static final String TYPE_NOT_FOUND = "El tipo de movimiento con identificador <%d> no existe en la base de datos";
    private static final String TYPE_ACTIVATED = "El tipo de movimiento con identificador <%d> fue activado con éxito";
    private static final String TYPE_INACTIVATED = "El tipo de movimiento con identificador <%d> fue inactivado con éxito";
    private static final String DESCRIPTION_NOT_VALID = "El campo <description> no es válido";
    private static final String TYPE_ID_NOT_VALID = "El campo <typeId> no es válido";
    private static final String TYPE_EXISTS = "El tipo de movimiento con descripción <%s> ya existe en la base de datos";

    @Autowired
    TypeRepository typeRepository;

    public Type findById(Long id) {
        return typeRepository.findOne(id);
    }

    public String create(Params params) {
        validateCreate(params);
        Type type = Type.builder()
                .description(params.getDescription())
                .state(State.ACTIVE.get())
                .build();
        typeRepository.save(type);
        return String.format(TYPE_CREATED, type.getId());
    }

    private void validateExistence(String description) {
        Type type = typeRepository.findByDescriptionIgnoreCase(description);
        if(type != null) {
            throw new ValidateException(String.format(TYPE_EXISTS, description));
        }
    }

    private void validateCreate(Params params) {
        if(Validations.field(params.getDescription())){
            throw new ValidateException(DESCRIPTION_NOT_VALID);
        }
        validateExistence(params.getDescription());
    }

    public String inactivate(Params params) {
        Type type = validateAndFind(params.getTypeId());
        type.setState(State.INACTIVE.get());
        update(type);
        return String.format(TYPE_INACTIVATED, params.getTypeId());
    }

    public String activate(Params params) {
        Type type = validateAndFind(params.getTypeId());
        type.setState(State.ACTIVE.get());
        update(type);
        return String.format(TYPE_ACTIVATED, params.getTypeId());
    }

    public Type validateAndFind(Long id) {
        validateId(id);
        Type type = findById(id);
        if(type == null){
            throw new ValidateException(String.format(TYPE_NOT_FOUND, id));
        }
        return type;
    }

    private void validateId(Long id) {
        if(Validations.field(id)){
            throw new ValidateException(TYPE_ID_NOT_VALID);
        }
    }

    public void update(Type type) {
        typeRepository.save(type);
    }

    public List<Util> findAll() {
        return ObjectMapperUtils.mapAll(typeRepository.findByState(State.ACTIVE.get()), Util.class);
    }
}
