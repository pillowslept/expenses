package co.com.expenses.util;

public class Constants {

    // JAVA COMMANDS
    public static final String JAVA_EXECUTE_COMMAND = "java -cp ";
    public static final String JAVA_COMPILE_COMMAND = "javac ";
    public static final String JAVA_COMPILE_COMMAND_RESOURCES = "javac -cp ";
    // PYTHON COMMANDS
    public static final String PYTHON_EXECUTE_COMMAND = "python ";
    // R COMMANDS
    public static final String R_EXECUTE_COMMAND = "R CMD BATCH ";
    // QSUB COMMANDS
    public static final String QSUB_COMMAND = "qsub ";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String PDF_FILE_EXTENSION = "pdf";

    public static final String SPACE = " ";

    private Constants() {
    }

}
