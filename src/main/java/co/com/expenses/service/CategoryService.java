package co.com.expenses.service;

import static co.com.expenses.util.Constants.DEFAULT_FIELD_VALIDATION;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.expenses.component.Messages;
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

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    Messages messages;

    public Category findById(Long id) {
        return categoryRepository.findOne(id);
    }

    public String create(Params params) {
        validateCreate(params);
        Category category = Category.builder()
                .description(params.getDescription())
                .state(State.ACTIVE.get())
                .build();
        categoryRepository.save(category);
        return String.format(messages.get("category.created"), category.getId());
    }

    private void validateCreate(Params params) {
        if(Validations.field(params.getDescription())){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "description"));
        }
    }

    public String inactivate(Params params) {
        Category category = validateAndFind(params.getCategoryId());
        category.setState(State.INACTIVE.get());
        update(category);
        return String.format(messages.get("category.inactivated"), params.getCategoryId());
    }

    public String activate(Params params) {
        Category category = validateAndFind(params.getCategoryId());
        category.setState(State.ACTIVE.get());
        update(category);
        return String.format(messages.get("category.activated"), params.getCategoryId());
    }

    public Category validateAndFind(Long id) {
        validateId(id);
        Category category = findById(id);
        if(category == null){
            throw new ValidateException(String.format(messages.get("category.not.found"), id));
        }
        return category;
    }

    private void validateId(Long id) {
        if(Validations.field(id)){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "categoryId"));
        }
    }

    public void update(Category category) {
        categoryRepository.save(category);
    }

    public List<Util> findAll() {
        return ObjectMapperUtils.mapAll(categoryRepository.findAll(), Util.class);
    }

}
