package co.com.expenses.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import co.com.expenses.dto.Params;
import co.com.expenses.dto.SuccessResponse;
import co.com.expenses.dto.Util;
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
        Long id = 1L;
        Util util = new Util();
        util.setDescription("Test");
        Mockito.when(categoryService.inactivate(Mockito.any(Long.class)))
                .thenReturn(util);

        // act
        ResponseEntity<SuccessResponse> successResponse = categoryController.inactivate(id);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertEquals(successResponse.getBody().getData(), util);
    }

    @Test
    public void activateTest() {
        // arrange
        Long id = 1L;
        Util util = new Util();
        util.setDescription("Test");
        Mockito.when(categoryService.activate(Mockito.any(Long.class)))
                .thenReturn(util);

        // act
        ResponseEntity<SuccessResponse> successResponse = categoryController.activate(id);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertEquals(successResponse.getBody().getData(), util);
    }

    @Test
    public void createTest() {
        // arrange
        Params params = new Params();
        params.setDescription("Ropa");
        Util util = new Util();
        util.setDescription("Test");
        Mockito.when(categoryService.create(Mockito.any(Params.class)))
                .thenReturn(util);

        // act
        ResponseEntity<SuccessResponse> successResponse = categoryController.create(params);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertEquals(successResponse.getBody().getData(), util);
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
