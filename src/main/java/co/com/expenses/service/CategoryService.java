package co.com.expenses.service;

import static co.com.expenses.util.Constants.DEFAULT_FIELD_VALIDATION;

import java.util.List;
import java.util.Optional;

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
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            throw new ValidateException(String.format(messages.get("category.not.found"), id));
        }
        return category.get();
    }

    public String create(Params params) {
        validateFields(params);
        Category category = Category.builder()
                .description(params.getDescription())
                .state(State.ACTIVE.get())
                .build();
        categoryRepository.save(category);
        return String.format(messages.get("category.created"), category.getId());
    }

    public String update(Long id, Params params) {
        validateFields(params);
        Category category = validateAndFind(id);
        category.setDescription(params.getDescription());
        update(category);
        return String.format(messages.get("category.updated"), category.getId());
    }

    private void validateFields(Params params) {
        if(Validations.field(params.getDescription())){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "description"));
        }
    }

    public String inactivate(Long id) {
        Category category = validateAndFind(id);
        category.setState(State.INACTIVE.get());
        update(category);
        return String.format(messages.get("category.inactivated"), id);
    }

    public String activate(Long id) {
        Category category = validateAndFind(id);
        category.setState(State.ACTIVE.get());
        update(category);
        return String.format(messages.get("category.activated"), id);
    }

    public Category validateAndFind(Long id) {
        validateId(id);
        return findById(id);
    }

    private void validateId(Long id) {
        if(Validations.field(id)){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "categoryId"));
        }
    }

    private void update(Category category) {
        categoryRepository.save(category);
    }

    public List<Util> findAll() {
        return ObjectMapperUtils.mapAll(categoryRepository.findAll(), Util.class);
    }

}
