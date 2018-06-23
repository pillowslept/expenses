package co.com.expenses.util;

import java.math.BigDecimal;
import java.util.List;

public class Validations {

    public static boolean field(String field) {
        return field == null || field.isEmpty();
    }

    public static boolean field(Long field) {
        return field == null || field <= 0;
    }

    public static boolean field(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean field(BigDecimal field) {
        return field == null || field.compareTo(BigDecimal.ZERO) <= 0;
    }
}
