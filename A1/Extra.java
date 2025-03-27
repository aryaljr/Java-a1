package A1;

public enum Extra {
    CHOCOLATE_POWDER,
    CINNAMON,
    VANILLA_SYRUP,
    WHIPPED_CREAM,
    VANILLA_ICE_CREAM, SKIP;

    public String toString() {
        return switch (this) {
            case CHOCOLATE_POWDER -> "Chocolate powder";
            case CINNAMON -> "Cinnamon";
            case VANILLA_SYRUP -> "Vanilla syrup";
            case WHIPPED_CREAM -> "Whipped cream";
            case VANILLA_ICE_CREAM -> "Vanilla ice cream";
            case SKIP -> "skip";
        };
    }
}
