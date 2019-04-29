package co.com.expenses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.expenses.dto.Params;
import co.com.expenses.dto.SuccessResponse;
import co.com.expenses.service.TypeService;

@RestController
@RequestMapping("/api/types")
public class TypesController {

    @Autowired
    TypeService typeService;

    @RequestMapping(value = "/inactivate", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> inactivate(@RequestBody Params params) {
        return new ResponseEntity<>(buildResponse(typeService.inactivate(params)), HttpStatus.OK);
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> activate(@RequestBody Params params) {
        return new ResponseEntity<>(buildResponse(typeService.activate(params)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> create(@RequestBody Params params) {
        return new ResponseEntity<>(buildResponse(typeService.create(params)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> findAll() {
        return new ResponseEntity<>(buildResponse(typeService.findAll()), HttpStatus.OK);
    }

    private SuccessResponse buildResponse(Object data) {
        return SuccessResponse.builder().data(data).build();
    }
}