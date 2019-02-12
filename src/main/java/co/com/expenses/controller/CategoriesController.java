package co.com.expenses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.expenses.dto.Params;
import co.com.expenses.dto.Util;
import co.com.expenses.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/inactivate", method = RequestMethod.POST)
    public ResponseEntity<String> inactivate(@RequestBody Params params) {
        return new ResponseEntity<>(categoryService.inactivate(params), HttpStatus.OK);
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public ResponseEntity<String> activate(@RequestBody Params params) {
        return new ResponseEntity<>(categoryService.activate(params), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> create(@RequestBody Params params) {
        return new ResponseEntity<>(categoryService.create(params), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Util>> findAll() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

}