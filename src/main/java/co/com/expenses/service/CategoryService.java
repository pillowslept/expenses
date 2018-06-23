package co.com.expenses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.dto.Params;
import co.com.expenses.dto.Util;
import co.com.expenses.enums.State;
import co.com.expenses.exception.ValidateException;
import co.com.expenses.model.Category;
import co.com.expenses.repository.CategoryRepository;
import co.com.expenses.util.ObjectMapperUtils;
import co.com.expenses.util.Validations;

@Service
@Transactional
public class CategoryService {

    private static final String CATEGORY_CREATED = "El tipo de categoría ha sido creado con éxito, el identificador generado es <%d>";
    private static final String CATEGORY_NOT_FOUND = "El tipo de categoría con identificador <%d> no existe en la base de datos";
    private static final String CATEGORY_ACTIVATED = "El tipo de categoría con identificador <%d> fue activado con éxito";
    private static final String CATEGORY_INACTIVATED = "El tipo de categoría con identificador <%d> fue inactivado con éxito";
    private static final String DESCRIPTION_NOT_VALID = "El campo <description> no es válido";
    private static final String CATEGORY_ID_NOT_VALID = "El campo <categoryId> no es válido";

    @Autowired
    CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findOne(id);
    }

    public String create(Params params) {
        validateCreate(params);
        Category category = Category.builder()
                .description(params.getDescription())
                .state(State.ACTIVE.getState())
                .build();
        categoryRepository.save(category);
        return String.format(CATEGORY_CREATED, category.getId());
    }

    private void validateCreate(Params params) {
        if(Validations.field(params.getDescription())){
            throw new ValidateException(DESCRIPTION_NOT_VALID);
        }
    }

    public String inactivate(Params params) {
        Category category = validateAndFind(params.getCategoryId());
        category.setState(State.INACTIVE.getState());
        update(category);
        return String.format(CATEGORY_INACTIVATED, params.getCategoryId());
    }

    public String activate(Params params) {
        Category category = validateAndFind(params.getCategoryId());
        category.setState(State.ACTIVE.getState());
        update(category);
        return String.format(CATEGORY_ACTIVATED, params.getCategoryId());
    }

    public Category validateAndFind(Long id) {
        validateId(id);
        Category category = findById(id);
        if(category == null){
            throw new ValidateException(String.format(CATEGORY_NOT_FOUND, id));
        }
        return category;
    }

    private void validateId(Long id) {
        if(Validations.field(id)){
            throw new ValidateException(CATEGORY_ID_NOT_VALID);
        }
    }

    public void update(Category category) {
        categoryRepository.save(category);
    }

    public List<Util> findAll() {
        return ObjectMapperUtils.mapAll(categoryRepository.findAll(), Util.class);
    }

}
