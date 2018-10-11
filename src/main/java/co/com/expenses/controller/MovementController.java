package co.com.expenses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.expenses.dto.MovementSummary;
import co.com.expenses.dto.Params;
import co.com.expenses.service.MovementService;

@RestController
@RequestMapping("/api/movement")
public class MovementController {

    @Autowired
    MovementService movementService;

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public ResponseEntity<String> create(@RequestBody Params params) {
        return new ResponseEntity<>(movementService.create(params), HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<MovementSummary>> create() {
        return new ResponseEntity<>(movementService.findAll(), HttpStatus.OK);
    }
}