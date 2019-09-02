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
import co.com.expenses.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/{id}/inactivate", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> inactivate(@PathVariable("id") Long id) {
        return buildResponse(categoryService.inactivate(id));
    }

    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> activate(@PathVariable("id") Long id) {
        return buildResponse(categoryService.activate(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> create(@RequestBody Params params) {
        return buildResponse(categoryService.create(params));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<SuccessResponse> update(@PathVariable("id") Long id, @RequestBody Params params) {
        return buildResponse(categoryService.update(id, params));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> byId(@PathVariable("id") Long id) {
        return buildResponse(categoryService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> findAll() {
        return buildResponse(categoryService.findAll());
    }

    private ResponseEntity<SuccessResponse> buildResponse(Object data) {
        return new ResponseEntity<>(SuccessResponse.builder().data(data).build(), HttpStatus.OK);
    }

}