package co.com.expenses.component;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * Helper to simplify accessing i18n messages in code.
 * 
 * This finds messages automatically found from src/main/resources (files named messages_*.properties)
 * 
 * This example uses hard-coded English locale.
 *
 * @author Joni Karppinen
 * @since 2015-11-02
 */
@Component
public class Messages {

    @Value("${app.language}")
    private String language;

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource);
    }

    public String get(String code) {
        return accessor.getMessage(code, new Locale(language));
    }

    public String get(String code, Locale locale) {
        return accessor.getMessage(code, locale);
    }

}