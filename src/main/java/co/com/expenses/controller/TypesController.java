package co.com.expenses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/{id:[0-9]+}/inactivate", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> inactivate(@PathVariable("id") Long id) {
        return buildResponse(typeService.inactivate(id));
    }

    @RequestMapping(value = "/{id:[0-9]+}/activate", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> activate(@PathVariable("id") Long id) {
        return buildResponse(typeService.activate(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> create(@RequestBody Params params) {
        return buildResponse(typeService.create(params));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> findAll() {
        return buildResponse(typeService.findAll());
    }

    private ResponseEntity<SuccessResponse> buildResponse(Object data) {
        return new ResponseEntity<>(SuccessResponse.builder().data(data).build(), HttpStatus.OK);
    }

}