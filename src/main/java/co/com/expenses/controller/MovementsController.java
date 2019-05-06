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

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> create(@RequestBody Params params) {
        return new ResponseEntity<>(SuccessResponse.builder().message(movementService.create(params)).build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> byId(@PathVariable("id") Long id) {
        return buildResponse(movementService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> findAllPageable(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return buildResponse(movementService.findAll(pageNumber, pageSize));
    }

    @RequestMapping(value = "/month/{month:[1-9]|1[0-2]}/year/{year:^[0-9]{4}$}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> byMonthAndYear(@PathVariable("month") int month,
            @PathVariable("year") int year, @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return buildResponse(movementService.findByCreationDateBetween(month, year, pageNumber, pageSize));
    }

    private ResponseEntity<SuccessResponse> buildResponse(Object data) {
        return new ResponseEntity<>(SuccessResponse.builder().data(data).build(), HttpStatus.OK);
    }

}