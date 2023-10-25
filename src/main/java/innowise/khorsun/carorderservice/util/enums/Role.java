package innowise.khorsun.carorderservice.util.enums;

public enum Role {
    MANAGER("MANAGER"),
    CUSTOMER("CUSTOMER");
    private final String val;

    Role(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
