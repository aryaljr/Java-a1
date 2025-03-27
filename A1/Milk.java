package A1;
/**
 * @author Dr Andreas Shepley (asheple2@une.edu.au | andreashepley01@gmail.com)
 * created for COSC120 (Trimester 1 2022)
 * last revised: Trimester 1 2024
 */

//EDIT - PART 3.1:  in cases where you only want the user to select from a finite range of options, create an enum
public enum Milk {
    FULL_CREAM, SKIM, SOY, ALMOND, OAT, CASHEW, None; 

    /**
     * @return a prettified version of the relevant enum constant
     */
    public String toString() {
        return switch (this) {
            case FULL_CREAM -> "Full-cream";
            case SKIM -> "skim";
            case SOY -> "Soy";
            case ALMOND -> "Almond";
            case OAT -> "Oat";
            case CASHEW -> "Cashew";
            case None -> "None";
        };
    }
}
