package co.com.expenses.enums;

public enum State {

    ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

    private String state;

    State(String state) {
        this.state = state;
    }

    public String get() {
        return state;
    }
}
