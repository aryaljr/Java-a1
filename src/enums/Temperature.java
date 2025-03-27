package src.enums;

public enum Temperature {
    HOT("no"),
    ICED("yes");

    private final String value;

    Temperature(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Temperature fromString(String text) {
        for (Temperature temp : Temperature.values()) {
            if (temp.value.equalsIgnoreCase(text)) {
                return temp;
            }
        }
        return HOT;
    }
}