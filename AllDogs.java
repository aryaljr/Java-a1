/**
 * @author Dr Andreas Shepley (asheple2@une.edu.au | andreashepley01@gmail.com)
 * created for COSC120 (Trimester 1 2022)
 * last revised: Trimester 1 2024
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllDogs {
    //fields
    private final List<Dog> allDogs = new ArrayList<>();

    //default constructor used, therefore no need to declare it

    //methods
    /**
     * method to add a Dog object to the database (allDogs)
     * @param dog a Dog object
     */
    public void addDog(Dog dog){
        this.allDogs.add(dog);
    }

    //EDIT - PART 4.1 - a method to return a set of all breeds in the dataset (no duplicates)
    /**
     * a method to return a set of all breeds in the dataset (no duplicates)
     * @return Set</String> of available breeds
     */
    public Set<String> getAllBreeds(){
        Set<String> allBreeds = new HashSet<>();
        for(Dog d: allDogs){
            allBreeds.add(d.getBreed());
        }
        return allBreeds;
    }
    //END EDIT

    /**
     * returns a collection of Dog objects that meet all the user's requirements
     * @param dogCriteria a Dog object representing a user's preferred Dog
     * @return a Dog object
     */
    //EDIT - PART 4.5.1: change the method header/return type to ArrayList<Dog> as we're now returning a list of Dogs
    //public Dog findMatch(Dog dogCriteria){
     public List<Dog> findMatch(Dog dogCriteria){
        //EDIT - PART 4.5.1: create a collection in which to store compatible Dog objects
        List<Dog> compatibleDogs = new ArrayList<>();
        for(Dog dog: this.allDogs){
            if(!dog.getBreed().equals(dogCriteria.getBreed())) continue;
            if(!dog.getSex().equals(dogCriteria.getSex())) continue;
            //EDIT - PART 4.4: search for a dog within the age range, rather than of a specific age
            if(dog.getAge()<dogCriteria.getMinAge() || dog.getAge()> dogCriteria.getMaxAge()) continue;
            //if(dog.getAge()!= dogCriteria.getAge()) continue;
            if(!dog.isDeSexed().equals(dogCriteria.isDeSexed())) continue;
            //return dog;
            compatibleDogs.add(dog); //EDIT - PART 4.5.1: add the compatible Dogs to the collection instead of returning
        }
        //EDIT - PART 4.5.1: return the collection of compatible Dogs
        //return null;
         return compatibleDogs;
    }

}
