package co.com.expenses.service;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import co.com.expenses.dto.Util;

@Service
public class FiltersService {

    public List<Util> getMonthsByLocale(String languageTag) {
        List<Util> months = new ArrayList<>();

        Locale locale = Locale.getDefault();
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        if (languageTag != null && !languageTag.isEmpty()) {
            dateFormatSymbols = new DateFormatSymbols(Locale.forLanguageTag(languageTag)); // "es-ES"
        }

        long cont = 1L;
        for (String month : dateFormatSymbols.getMonths()) {
            if (!month.isEmpty()) {
                Util util = new Util();
                util.setDescription(month);
                util.setId(cont);
                months.add(util);
                cont++;
            }
        }

        return months;
    }

}
