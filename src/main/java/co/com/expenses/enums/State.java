package co.com.expenses.enums;

public enum State {

    ACTIVE("Activo"), INACTIVE("Inactivo");

    private String state;

    State(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
