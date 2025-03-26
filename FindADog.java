/**
 * @author Dr Andreas Shepley (asheple2@une.edu.au | andreashepley01@gmail.com)
 * created for COSC120 (Trimester 1 2022)
 * last revised: Trimester 1 2024
 */

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindADog {

    //fields
    private final static String appName = "Pinkman's Pets Dog Finder";
    private final static String filePath = "./allDogs.txt";
    private final static String iconPath = "./icon.jpg";
    private static AllDogs allDogs;

    /**
     * main method used to allow the user to search Pinkman's database of dogs, and place an adoption request
     * @param args none required
     */
    public static void main(String[] args) {
        allDogs = loadDogs();
        ImageIcon icon = new ImageIcon(iconPath);

        JOptionPane.showMessageDialog(null, "Welcome to Pinkman's Pets Dog Finder!\n\tTo start, click OK.", appName, JOptionPane.QUESTION_MESSAGE, icon);
        Dog dogCriteria = getUserCriteria();
        //EDIT - PART 4.5.2-4: Now that findMatch returns a collection, we'll have to show the user all options
        List<Dog> potentialMatches = allDogs.findMatch(dogCriteria);
        //Dog potentialMatch = allDogs.findMatch(dogCriteria);
        //if (potentialMatch != null) {
        if(potentialMatches.size()>0){ //if the list is not empty
            //this will map the dog names to the corresponding Dog objects
            Map<String,Dog> options = new HashMap<>();
            //build a text description of the dogs for the user to view
            StringBuilder infoToShow = new StringBuilder("Matches found!! The following dogs meet your criteria: \n");
            for (Dog potentialMatch : potentialMatches) {
                infoToShow.append(potentialMatch.getName()).append(" (").append(potentialMatch.getMicrochipNumber()).append(") is a ").append(potentialMatch.getAge()).append(" year old ").append(potentialMatch.getSex()).append(" ").append(potentialMatch.getBreed()).append(". De-sexed: ").append(potentialMatch.isDeSexed()).append("\n");
                options.put(potentialMatch.getName() + " (" + potentialMatch.getMicrochipNumber() + ")", potentialMatch);
            }
            String adopt = (String) JOptionPane.showInputDialog(null,infoToShow+"\n\nPlease select which (if any) dog you'd like to adopt:","Pinkman's Pets Dog Finder", JOptionPane.QUESTION_MESSAGE,null,options.keySet().toArray(), "");
            if(adopt==null) System.exit(0);
            else{
                Dog chosenDog = options.get(adopt);
                Person applicant = getUserDetails();
                writeAdoptionRequestToFile(applicant, chosenDog);
                JOptionPane.showMessageDialog(null, "Thank you! Your adoption request has been submitted. " +
                        "One of our friendly staff will be in touch shortly.", appName, JOptionPane.QUESTION_MESSAGE, icon);
            }
            //END EDIT - PART 4.5.2-4

            /* NO LONGER NEEDED
            String adopt = JOptionPane.showInputDialog(null, "Match found!!\n\nWould you like to adopt " + potentialMatch.getName() +
                    " (" + potentialMatch.getMicrochipNumber() + ")?", appName, JOptionPane.QUESTION_MESSAGE);
            if (adopt.equals("yes")) {
                Person applicant = getUserDetails();
                writeAdoptionRequestToFile(applicant, potentialMatch);
                JOptionPane.showMessageDialog(null, "Thank you! Your adoption request has been submitted. " +
                        "One of our friendly staff will be in touch shortly.", appName, JOptionPane.QUESTION_MESSAGE, icon);
            } else {
                JOptionPane.showMessageDialog(null, "That's really sad. We're sure " + potentialMatch.getName()
                                + " would make a wonderful furry friend. Please reconsider and come back again if you change your mind.",
                        appName, JOptionPane.QUESTION_MESSAGE, icon);
            }
            */
        } else JOptionPane.showMessageDialog(null, "Unfortunately none of our pooches meet your criteria :(" +
                "\n\tTo exit, click OK.", appName, JOptionPane.QUESTION_MESSAGE, icon);
        System.exit(0);
    }

    /**
     * method to get user to input name, ph num and email, with appropriate input validation
     * @return a Person object representing the user of the program
     */
    private static Person getUserDetails(){

        String name;
        do{
            name = JOptionPane.showInputDialog("Please enter your full name (in format firstname surname): ");
            if(name==null) System.exit(0);
            //EDIT 19: call the new method as the while condition - if it returns false, the name is invalid, and must be re-entered
        } while(!isValidFullName(name));

        //Using Regex is a better idea than hard-coding in your logic
        //}while (!name.contains(" "));

        String phoneNumber;
        do{
            phoneNumber = JOptionPane.showInputDialog("Please enter your phone number (10-digit number in the format 0412345678): ");
            if(phoneNumber==null) System.exit(0);}
        while(!isValidPhoneNumber(phoneNumber));

        //replace this with regex too!
        /*
        long phoneNumber = 0;
        String phoneNumberInput;
        do {
            phoneNumberInput = JOptionPane.showInputDialog(null,"Please enter your phone number. Format: 0412838475",appName,JOptionPane.QUESTION_MESSAGE);
            if (phoneNumberInput == null) System.exit(0);
            //EDIT - PART 3.3: try-catch to handle invalid input
            try {
                phoneNumber = Long.parseLong(phoneNumberInput);
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(null, "Invalid entry. Please enter your 10 digit phone number.");
            }
        }while(phoneNumberInput.length()!=10);
         */

        String email;
        do {
            email = JOptionPane.showInputDialog(null, "Please enter your email address.", appName, JOptionPane.QUESTION_MESSAGE);
            if (email == null) System.exit(0);
        }while(!isValidEmail(email));    //replace with regex
        //}while (!email.contains("@"));

        return new Person(name, phoneNumber, email);
    }

    /**
     * a very simple regex for full name in Firstname Surname format
     * @param fullName the candidate full name entered by the user
     * @return true if name matches regex/false if not
     */
    public static boolean isValidFullName(String fullName) {
        String regex = "^[A-Z][a-z]+\\s[A-Z][a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    //EDIT 20: create a method to check the validity of the user's phone number
    /**
     * a regex matcher that ensures that the user's entry starts with a 0 and is followed by 9 digits
     * @param phoneNumber the candidate phone number entered by the user
     * @return true if phone number matches regex/false if not
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^0\\d{9}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * a regex matcher that ensures that the user's entry complies with RFC 5322
     * source: <a href="https://www.baeldung.com/java-email-validation-regex">...</a>
     * @param email the candidate email entered by the user
     * @return true if email matches regex/false if not
     */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /**
     * generates JOptionPanes requesting user input for dog breed, sex, de-sexed status and age
     * @return a Dog object representing the user's desired dog criteria
     */
    private static Dog getUserCriteria(){

        //EDIT - PART 4.1: use the new method added to AllDogs to populate a dropdown list
        String breed  = (String) JOptionPane.showInputDialog(null,"Please select your preferred breed.","Pinkman's Pets Dog Finder", JOptionPane.QUESTION_MESSAGE,null,allDogs.getAllBreeds().toArray(), "");
        if(breed==null) System.exit(0);
        /*NO LONGER NEEDED - much tidier now!
        String breed;
        do {
            breed = JOptionPane.showInputDialog(null, "Please enter your preferred breed.", appName, JOptionPane.QUESTION_MESSAGE);
            if(breed==null) System.exit(0);
        }while (breed.length()==0);
        //EDIT - PART 3.1: convert user breed input to lowercase
        breed=breed.toLowerCase();
         */

        //EDIT - PART 3.1: change both sex and de-sexed to the enum and dropdown list - no longer any need for the loops!
        Sex sex = (Sex) JOptionPane.showInputDialog(null,"Please select your preferred sex:",appName, JOptionPane.QUESTION_MESSAGE,null,Sex.values(),Sex.FEMALE);
        if(sex==null) System.exit(0);
        /*
        String sex;
        do{
            sex = JOptionPane.showInputDialog(null, "Would you like your dog to be male or female?", appName, JOptionPane.QUESTION_MESSAGE);
            if(sex==null) System.exit(0); //if the user closes/cancels the dialog, exit normally
        } while (!sex.equalsIgnoreCase("male") && !sex.equalsIgnoreCase("female"));
         */

        DeSexed deSexed = (DeSexed) JOptionPane.showInputDialog(null,"Would you like your dog to be de-sexed or not?",appName, JOptionPane.QUESTION_MESSAGE,null,DeSexed.values(),DeSexed.YES);
        if(deSexed==null) System.exit(0);
        /*
        String deSexed;
        do{
            deSexed = JOptionPane.showInputDialog(null, "Would you like your dog to be de-sexed or not(yes/no)?", appName, JOptionPane.QUESTION_MESSAGE);
            if(deSexed==null) System.exit(0);
        } while (!deSexed.equalsIgnoreCase("yes") && !deSexed.equalsIgnoreCase("no"));
         */

        //EDIT - PART 4.2 - create min and max age fields
        int minAge = -1, maxAge = -1;
        while(minAge==-1) {
            try {
                minAge = Integer.parseInt(JOptionPane.showInputDialog(null,"What is the age (years) of the youngest dog you'd like to adopt ",appName,JOptionPane.QUESTION_MESSAGE));
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.");
            }
        }
        //max age cannot be less than min age
        while(maxAge<minAge) {
            try {
                maxAge = Integer.parseInt(JOptionPane.showInputDialog(null,"What is the age (years) of the oldest dog you'd be willing to adopt ",appName,JOptionPane.QUESTION_MESSAGE));
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Invalid input. Please try again.");
            }
            if(maxAge<minAge) JOptionPane.showMessageDialog(null,"Max age must be >= min age.");
        }
        /* NO LONGER NEEDED
        int age = -1;
        do {
            String preferredAge = JOptionPane.showInputDialog(null,"How old would you like your pooch to be (years)? ",appName,JOptionPane.QUESTION_MESSAGE);
            if (preferredAge == null) System.exit(0);
            //EDIT - PART 3.2 - use a try-catch to handle invalid input
            try {
                age = Integer.parseInt(preferredAge);
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(null, "Invalid entry. Please enter an integer above 0.");
            }
        }while(age < 0);
         */

        //EDIT PART 4.2: create a new Dog object, change age to -1, then use the setters to set the age range, then return the Dog object
        //return new Dog("", 0, age, breed, sex, deSexed);
        Dog dogCriteria = new Dog("", 0, -1, breed, sex, deSexed);
        dogCriteria.setMinAge(minAge);
        dogCriteria.setMaxAge(maxAge);
        return dogCriteria;
    }

    /**
     * method to load all dog data from file, storing it as Dog objects in an instance of AllDogs
     * @return an AllDogs object - functions as database of Dogs, with associated methods
     */
    private static AllDogs loadDogs() {
        AllDogs allDogs = new AllDogs();
        Path path = Path.of(filePath);

        List<String> dogData = null;
        try{
            dogData = Files.readAllLines(path);
        }catch (IOException io){
            System.out.println("Could not load the file. \nError message: "+io.getMessage());
            System.exit(0);
        }

        for (int i=1;i<dogData.size();i++) {
            String[] elements = dogData.get(i).split(",");
            String name = elements[0];
            long microchipNumber = 0;
            try{
                microchipNumber = Long.parseLong(elements[1]);
            }
            catch (NumberFormatException n){
                System.out.println("Error in file. Microchip number could not be parsed for dog on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            //EDIT - PART 3.1: read the data from the file as enum
            Sex sex = Sex.valueOf(elements[2].toUpperCase());
            DeSexed deSexed = DeSexed.valueOf(elements[3].toUpperCase());
            //String sex = elements[2];
            //String deSexed = elements[3];

            int age = 0;
            try{
                age = Integer.parseInt(elements[4]);
            }catch (NumberFormatException n){
                System.out.println("Error in file. Age could not be parsed for dog on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }
            //EDIT - PART 3.1: change to lower case for better String comparison
            String breed = elements[5].toLowerCase();

            Dog dog = new Dog(name, microchipNumber,age, breed, sex, deSexed);
            allDogs.addDog(dog);
        }
        return allDogs;
    }

    /**
     * provides Pinkman's Pets with a file containing the user's adoption request
     * @param person a Person object representing the user
     * @param dog a Dog object representing the dog that the user wants to adopt
     */
    private static void writeAdoptionRequestToFile(Person person, Dog dog) {
        String filePath = person.name().replace(" ","_")+"_adoption_request.txt";
        Path path = Path.of(filePath);
        String lineToWrite = person.name()+" wishes to adopt "+dog.getName()+" ("+dog.getMicrochipNumber()+
                "). Their phone number is "+person.phoneNumber()+" and their email address is "+person.emailAddress();
        try {
            Files.writeString(path, lineToWrite);
        }catch (IOException io){
            System.out.println("File could not be written. \nError message: "+io.getMessage());
            System.exit(0);
        }
    }

}
