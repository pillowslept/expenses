package co.com.expenses.util;

import static java.util.Locale.US;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

public class CurrencyUtilities {

    private CurrencyUtilities() {
    }

    public static String formatValue(BigDecimal value) {
        return Currency.getInstance(US).getSymbol() + NumberFormat.getNumberInstance(US).format(value);
    }
}
