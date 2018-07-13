package co.com.expenses.enums;

public enum Type {

    EXPENSE(2L), INCOME(1L);

    private Long type;

    Type(Long type) {
        this.type = type;
    }

    public Long get() {
        return type;
    }
}
