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

    public Util create(Params params) {
        this.validateFields(params);
        Category category = Category.builder()
                .description(params.getDescription())
                .state(State.ACTIVE.get())
                .build();
        categoryRepository.save(category);

        return ObjectMapperUtils.map(category, Util.class);
    }

    public Util update(Long id, Params params) {
        this.validateFields(params);
        Category category = validateAndFind(id);
        category.setDescription(params.getDescription());
        this.update(category);

        return ObjectMapperUtils.map(category, Util.class);
    }

    private void validateFields(Params params) {
        if(Validations.field(params.getDescription())){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "description"));
        }
    }

    public Util inactivate(Long id) {
        Category category = this.validateAndFind(id);
        category.setState(State.INACTIVE.get());
        this.update(category);

        return ObjectMapperUtils.map(category, Util.class);
    }

    public Util activate(Long id) {
        Category category = this.validateAndFind(id);
        category.setState(State.ACTIVE.get());
        this.update(category);

        return ObjectMapperUtils.map(category, Util.class);
    }

    public Category validateAndFind(Long id) {
        if(Validations.field(id)){
            throw new ValidateException(String.format(messages.get(DEFAULT_FIELD_VALIDATION), "categoryId"));
        }

        return this.findById(id);
    }

    private void update(Category category) {
        categoryRepository.save(category);
    }

    public List<Util> findAll() {
        return ObjectMapperUtils.mapAll(categoryRepository.findAll(), Util.class);
    }

}
