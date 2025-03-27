package src.enums;


public enum MilkType {
    FULL_CREAM("Full-cream"),
    SKIM("Skim"),
    SOY("Soy"),
    OAT("Oat"),
    ALMOND("Almond"),
    CASHEW("Cashew"),
    NONE("None");

    private final String displayName;

    MilkType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MilkType fromString(String text) {
        for (MilkType type : MilkType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return NONE;
    }
}