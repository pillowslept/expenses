package co.com.expenses.enums;

public enum ReportType {

    PDF("pdf"), EXCEL("xls");

    private String report;

    ReportType(String report) {
        this.report = report;
    }

    public String get() {
        return report;
    }
}
