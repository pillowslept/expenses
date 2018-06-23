package co.com.expenses.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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

    @Test
    public void findByIdTest() {
        // arrange
        Long id = 1L;
        Mockito.when(categoryRepository.findOne(Mockito.anyLong())).thenReturn(Category.builder().build());

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
        String message = categoryService.create(params);

        // assert
        Assert.assertNotNull(message);
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
        Mockito.when(categoryRepository.findOne(Mockito.anyLong())).thenReturn(Category.builder().build());

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
        Mockito.when(categoryRepository.findOne(Mockito.anyLong())).thenReturn(null);

        // act
        categoryService.validateAndFind(investigatorId);
    }

    @Test
    public void updateTest() throws ValidateException {
        // arrange
        Category category = Category.builder().build();

        // act
        categoryService.update(category);

        // assert
        Assert.assertNotNull(category);
    }

    @Test
    public void inactivateTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setCategoryId(1L);
        Mockito.when(categoryRepository.findOne(Mockito.anyLong())).thenReturn(Category.builder().build());

        // act
        String message = categoryService.inactivate(params);

        // assert
        Assert.assertNotNull(message);
    }

    @Test
    public void activateTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setCategoryId(1L);
        Mockito.when(categoryRepository.findOne(Mockito.anyLong())).thenReturn(Category.builder().build());

        // act
        String message = categoryService.activate(params);

        // assert
        Assert.assertNotNull(message);
    }
}
