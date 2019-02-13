package co.com.expenses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.expenses.dto.MovementSummary;
import co.com.expenses.dto.Params;
import co.com.expenses.service.MovementService;

@RestController
@RequestMapping("/api/movements")
public class MovementsController {

    @Autowired
    MovementService movementService;

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> create(@RequestBody Params params) {
        return new ResponseEntity<>(movementService.create(params), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MovementSummary> byId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(movementService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MovementSummary>> findAllPageable(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return new ResponseEntity<>(movementService.findAll(pageNumber, pageSize), HttpStatus.OK);
    }

    @RequestMapping(value = "/month/{month:[1-9]|1[0-2]}/year/{year:^[0-9]{4}$}", method = RequestMethod.GET)
    public ResponseEntity<List<MovementSummary>> byMonthAndYear(@PathVariable("month") int month,
            @PathVariable("year") int year, @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return new ResponseEntity<>(
                movementService.findByCreationDateBetween(month, year, pageNumber, pageSize), HttpStatus.OK);
    }

}