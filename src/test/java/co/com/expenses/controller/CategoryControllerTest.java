package co.com.expenses.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import co.com.expenses.dto.Params;
import co.com.expenses.dto.SuccessResponse;
import co.com.expenses.service.CategoryService;

@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerTest {

    @InjectMocks
    CategoriesController categoryController;

    @Mock
    CategoryService categoryService;

    @Test
    public void inactivateTest() {
        // arrange
        Params params = new Params();
        params.setCategoryId(1L);
        String messageToReturn = "Categoría creada " + params.getCategoryId();
        Mockito.when(categoryService.inactivate(Mockito.any(Params.class)))
                .thenReturn(messageToReturn);

        // act
        ResponseEntity<SuccessResponse> successResponse = categoryController.inactivate(params);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertEquals(successResponse.getBody().getData(), messageToReturn);
    }

    @Test
    public void activateTest() {
        // arrange
        Params params = new Params();
        params.setCategoryId(1L);
        String messageToReturn = "Categoría activada " + params.getCategoryId();
        Mockito.when(categoryService.activate(Mockito.any(Params.class)))
                .thenReturn(messageToReturn);

        // act
        ResponseEntity<SuccessResponse> successResponse = categoryController.activate(params);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertEquals(successResponse.getBody().getData(), messageToReturn);
    }

    @Test
    public void createTest() {
        // arrange
        Params params = new Params();
        params.setDescription("Ropa");
        String messageToShow = "Categoría creada con éxito";
        Mockito.when(categoryService.create(Mockito.any(Params.class)))
                .thenReturn(messageToShow);

        // act
        ResponseEntity<SuccessResponse> successResponse = categoryController.create(params);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertEquals(successResponse.getBody().getData(), messageToShow);
    }

    @Test
    public void findAllTes() {
        // arrange
        Params params = new Params();
        params.setDescription("Ropa");
        Mockito.when(categoryService.findAll()).thenReturn(new ArrayList<>());

        // act
        ResponseEntity<SuccessResponse> successResponse = categoryController.findAll();

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
    }
}
