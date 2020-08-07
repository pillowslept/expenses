package co.com.expenses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.expenses.dto.Params;
import co.com.expenses.dto.SuccessResponse;
import co.com.expenses.service.MovementService;

@RestController
@RequestMapping("/api/movements")
public class MovementsController {

    @Autowired
    MovementService movementService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> create(@RequestBody Params params) {
        return this.buildResponse(movementService.create(params));
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> update(@PathVariable("id") Long id, @RequestBody Params params) {
        return this.buildResponse(movementService.update(id, params));
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> byId(@PathVariable("id") Long id) {
        return this.buildResponse(movementService.findByIdMapped(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> findAllPageable(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return this.buildResponse(movementService.findAll(pageNumber, pageSize));
    }

    @RequestMapping(value = "/month/{month:[1-9]|1[0-2]}/year/{year:^[0-9]{4}$}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> byMonthAndYear(@PathVariable("month") int month,
            @PathVariable("year") int year, @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return this.buildResponse(movementService.findByCreationDateBetween(month, year, pageNumber, pageSize));
    }

    @RequestMapping(value = "/filters", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> byFilters(
            @RequestParam(name = "value", required = false) Long value,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "month", required = false) Integer month,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {

        return this.buildResponse(movementService.byFilters(value, month, year, pageSize, pageNumber));
    }

    private ResponseEntity<SuccessResponse> buildResponse(Object data) {
        return new ResponseEntity<>(SuccessResponse.builder().data(data).build(), HttpStatus.OK);
    }

}