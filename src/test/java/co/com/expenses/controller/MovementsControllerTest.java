package co.com.expenses.controller;

import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import co.com.expenses.dto.MovementSummary;
import co.com.expenses.dto.PageableSummary;
import co.com.expenses.dto.Params;
import co.com.expenses.dto.SuccessResponse;
import co.com.expenses.service.MovementService;

@RunWith(MockitoJUnitRunner.class)
public class MovementsControllerTest {

    @InjectMocks
    MovementsController movementsController;

    @Mock
    MovementService movementService;

    @Test
    public void createTest() {
        // arrange
        Params params = new Params();
        params.setTypeId(1L);
        params.setCategoryId(1L);
        params.setValue(BigDecimal.ONE);
        MovementSummary movementSummary = new MovementSummary();
        movementSummary.setObservations("Observations example");
        Mockito.when(movementService.create(Mockito.any(Params.class))).thenReturn(movementSummary);

        // act
        ResponseEntity<SuccessResponse> successResponse = movementsController.create(params);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertEquals(successResponse.getBody().getData(), movementSummary);
    }

    @Test
    public void findByFiltersTest() {
        // arrange
        Long value = 1000L;
        Integer year = 2020;
        Integer month = 1;
        String sortType = "asc";
        PageableSummary pageableSummary = new PageableSummary();
        pageableSummary.setNumberOfElements(10L);
        Mockito.when(movementService.byFilters(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),
                Mockito.anyInt(), Mockito.anyString())).thenReturn(pageableSummary);

        // act
        ResponseEntity<SuccessResponse> successResponse = movementsController.byFilters(value, year, month, 1, 1, sortType);

        // assert
        Assert.assertEquals(OK, successResponse.getStatusCode());
        Assert.assertNotNull(successResponse.getBody().getData());
        Assert.assertEquals(successResponse.getBody().getData(), pageableSummary);
    }

}
