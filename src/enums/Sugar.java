package src.enums;


public enum Sugar {
    YES("yes"),
    NO("no");

    private final String value;

    Sugar(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Sugar fromString(String text) {
        for (Sugar sugar : Sugar.values()) {
            if (sugar.value.equalsIgnoreCase(text)) {
                return sugar;
            }
        }
        return NO;
    }
}