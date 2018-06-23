package co.com.expenses.component;

import org.springframework.stereotype.Component;

@Component
public class FileUtilities {

    public String createFileName(String fileName, String extension){
        return String.format("%s.%s", fileName, extension);
    }

}
