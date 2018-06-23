package co.com.expenses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}