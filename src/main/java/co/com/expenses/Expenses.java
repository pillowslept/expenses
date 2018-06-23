package co.com.expenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "co.com.expenses" })
public class Expenses {

    public static void main(String[] args) {
        SpringApplication.run(Expenses.class, args);
    }

}