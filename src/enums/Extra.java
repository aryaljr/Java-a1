package src.enums;

public enum Extra {
    CHOCOLATE_POWDER("Chocolate powder"),
    CINNAMON("Cinnamon"),
    VANILLA_SYRUP("Vanilla syrup"),
    WHIPPED_CREAM("Whipped cream"),
    VANILLA_ICE_CREAM("Vanilla ice cream"),
    CHOCOLATE_SYRUP("Chocolate syrup"),
    CARAMEL_SYRUP("Caramel syrup"),
    NONE("None");

    private final String displayName;

    Extra(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Extra fromString(String text) {
        for (Extra extra : Extra.values()) {
            if (extra.displayName.equalsIgnoreCase(text)) {
                return extra;
            }
        }
        return NONE;
    }
}