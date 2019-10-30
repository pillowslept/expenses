package co.com.expenses.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import co.com.expenses.component.Messages;
import co.com.expenses.dto.CategorySummary;
import co.com.expenses.dto.Params;
import co.com.expenses.exception.ValidateException;
import co.com.expenses.model.Category;
import co.com.expenses.repository.CategoryRepository;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    Messages messages;

    @Before
    public void init() {
        Mockito.when(messages.get(Mockito.anyString())).thenReturn("");
    }

    @Test
    public void findByIdTest() {
        // arrange
        Long id = 1L;
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(Category.builder().build()));

        // act
        Category category = categoryService.findById(id);

        // assert
        Assert.assertNotNull(category);
    }

    @Test
    public void createTest() {
        // arrange
        Params params = new Params();
        params.setDescription("Ropa");

        // act
        CategorySummary categorySummary = categoryService.create(params);

        // assert
        Assert.assertNotNull(categorySummary);
    }

    @Test(expected = ValidateException.class)
    public void createInvalidDescriptionTest() throws ValidateException {
        // arrange
        Params params = new Params();

        // act
        categoryService.create(params);
    }

    @Test
    public void validateAndFindTest() {
        // arrange
        Long id = 1L;
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(Category.builder().build()));

        // act
        Category category = categoryService.validateAndFind(id);

        // assert
        Assert.assertNotNull(category);
    }

    @Test(expected = ValidateException.class)
    public void validateAndFindIdNullTest() {
        // arrange
        Long id = null;

        // act
        categoryService.validateAndFind(id);
    }

    @Test(expected = ValidateException.class)
    public void validateAndFindNotFoundTest() throws ValidateException {
        // arrange
        Long investigatorId = 1L;
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // act
        categoryService.validateAndFind(investigatorId);
    }

    @Test
    public void updateTest() throws ValidateException {
        // arrange
        Long id = 1L;
        Params params = new Params();
        params.setDescription("Ropa");
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(Category.builder().build()));

        // act
        CategorySummary categorySummary = categoryService.update(id, params);

        // assert
        Assert.assertNotNull(categorySummary);
    }

    @Test
    public void inactivateTest() throws ValidateException {
        // arrange
        Long id = 1L;
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(Category.builder().build()));

        // act
        CategorySummary categorySummary = categoryService.inactivate(id);

        // assert
        Assert.assertNotNull(categorySummary);
    }

    @Test
    public void activateTest() throws ValidateException {
        // arrange
        Long id = 1L;
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(Category.builder().build()));

        // act
        CategorySummary categorySummary = categoryService.activate(id);

        // assert
        Assert.assertNotNull(categorySummary);
    }

}
