package co.com.expenses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.expenses.dto.Util;
import co.com.expenses.service.FiltersService;

@RestController
@RequestMapping("/api/filters")
public class FiltersController {

    @Autowired
    FiltersService filtersService;

    @RequestMapping(value = "/months/{languageTag}", method = RequestMethod.GET)
    public ResponseEntity<List<Util>> months(@PathVariable("languageTag") String languageTag) {
        return new ResponseEntity<>(filtersService.getMonthsByLocale(languageTag), HttpStatus.OK);
    }
}