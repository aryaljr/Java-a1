/**
 * @author Dr Andreas Shepley (asheple2@une.edu.au | andreashepley01@gmail.com)
 * created for COSC120 (Trimester 1 2022)
 * last revised: Trimester 1 2024
 */

public class Dog {
    //fields
    private final String name;
    private final long microchipNumber;
    private final int age;
    private final String breed;
    //EDIT - PART 3.1 - change to enum type
    //private final String sex;
    //private final String deSexed;
    private final Sex sex;
    private final DeSexed deSexed;

    //EDIT - PART 4.2 - create min and max age fields
    private int minAge;
    private int maxAge;

    /**
     * constructor to create a Dog object
     * @param name the dog's name
     * @param microchipNumber the dog's microchip number - unique 9-digit number
     * @param age the dog's age in years
     * @param breed the dog's breed
     * @param sex the dog's sex (male or female)
     * @param deSexed the dog's de-sexed status - true if de-sexed, false if not
     */
    public Dog(String name, long microchipNumber, int age, String breed, Sex sex, DeSexed deSexed){
        this.name=name;
        this.microchipNumber=microchipNumber;
        this.age=age;
        this.breed=breed;
        this.sex=sex;
        this.deSexed=deSexed;
    }

    //getters
    /**
     * @return the dog's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the dog's microchip number - unique 9-digit number
     */
    public long getMicrochipNumber() {
        return microchipNumber;
    }

    /**
     * @return the dog's age in years
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the dog's breed
     */
    public String getBreed() {
        return breed;
    }

    /**
     * @return the dog's sex (male or female)
     */
    public Sex getSex() { return sex;} //EDIT PART 3.1

    /**
     * @return the dog's de-sexed status
     */
    public DeSexed isDeSexed() { return deSexed; } //EDIT PART 3.1

    //EDIT - PART 4.2 - create min and max age setters and getters
    /**
     * @param maxAge the max age a user is willing to adopt
     */
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
    /**
     * @param minAge the min age a user is willing to adopt
     */
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }
    /**
     * @return a 'dream' dog's min age
     */
    public int getMinAge() {
        return minAge;
    }
    /**
     * @return a 'dream' dog's max age
     */
    public int getMaxAge() {
        return maxAge;
    }
}