package A1;

/**
 * @author Dr Andreas Shepley (asheple2@une.edu.au | andreashepley01@gmail.com)
 * created for COSC120 (Trimester 1 2022)
 * last revised: Trimester 1 2024
 */

import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MenuSearcher {

    // fields
    private final static String appName = "Brewed Brilliance";
    private final static String filePath = "./menu.txt";
    private final static String iconPath = "./icon.jpg";
    private static Menu menu;
    private static ImageIcon icon;


    /**
     * main method used to allow the user to search Pinkman's database of dogs, and
     * place an adoption request
     * 
     * @param args none required
     */
    public static void main(String[] args) {
        menu = loadCoffees();
        icon = new ImageIcon(iconPath);
        JOptionPane.showMessageDialog(null, "Welcome to Pinkman's Pets Dog Finder!\n\tTo start, click OK.", appName,
                JOptionPane.QUESTION_MESSAGE, icon);
        Coffee coffeeCriteria = getUserCriteria(menu);

        List<Coffee> potentialMatches = menu.findMatch(coffeeCriteria);

        System.out.println(potentialMatches);

        if (potentialMatches.size() > 0) { // if the list is not empty
            Map<String, Coffee> options = new HashMap<>();
            StringBuilder infoToShow = new StringBuilder(
                    "Matches found!! The following coffees meet your criteria:\n\n");

            for (Coffee potentialMatch : potentialMatches) {
                infoToShow
                .append("******************************\n\n")
                .append(potentialMatch.getName()).append(" (").append(potentialMatch.getId()).append(")\n")
                        .append(potentialMatch.getDescription())
                        // .append("\n\n")
                        .append("Ingredients:\n")
                        .append("Milk: ").append(String.join(", ",
                                potentialMatch.getMilkTypes().stream()
                                        .map(Milk::toString)
                                        .collect(Collectors.toList())))
                        .append("\n")
                        .append("Number of Shots: ").append(potentialMatch.getNumberOfShots()).append("\n")
                        .append("Sugar: ").append(potentialMatch.getHasSuger().equalsIgnoreCase("yes") ? "Yes" : "No")
                        .append("\n")
                        .append("Iced: ").append(potentialMatch.getIsIced().equalsIgnoreCase("yes") ? "Yes" : "No")
                        .append("\n")
                        .append("Extras: ").append(String.join(", ", potentialMatch.getExtras())).append("\n")
                        .append("Price: $").append(String.format("%.2f", potentialMatch.getPrice())).append("\n\n")
                        // .append("******************************\n\n")
                        ;

                options.put(potentialMatch.getName(), potentialMatch);
            }

            System.out.println(infoToShow);

            String selectedCoffeeName = (String) JOptionPane.showInputDialog(null,
            infoToShow + "\n\nPlease select which (if any) coffee you'd like:",
            appName, JOptionPane.QUESTION_MESSAGE, icon,
            options.keySet().toArray(), "");
            if (selectedCoffeeName == null)
            System.exit(0);
            else {
            Coffee selectedCoffee = options.get(selectedCoffeeName);
            Geek geek = getUserDetails();
            writeOrderToFile(geek, selectedCoffee, coffeeCriteria);
            JOptionPane.showMessageDialog(null, "Thank you! Your order has been submitted. ", appName,
            JOptionPane.QUESTION_MESSAGE,
            icon);
            }
        } else
            JOptionPane.showMessageDialog(null, "Unfortunately none of our coffee meet your criteria :(" +
                    "\n\tTo exit, click OK.", appName, JOptionPane.QUESTION_MESSAGE, icon);
        System.exit(0);
    }

    private static Menu loadCoffees() {
        Menu menu = new Menu();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                List<String> fields = parseCsvLine(line);

                if (fields.size() >= 9) {

                    Long id = Long.parseLong(fields.get(0));
                    String name = fields.get(1);
                    double price = Double.parseDouble(fields.get(2));
                    int shots = Integer.parseInt(fields.get(3));
                    String sugar = fields.get(4);
                    String iced = fields.get(5);

                    // Parse milk options array
                    String milkStr = fields.get(6).replaceAll("[\\[\\]]", "");
                    List<Milk> milkOptions = Arrays.stream(milkStr.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(Milk::fromString)
                            .collect(Collectors.toList());

                    // Parse extras array
                    String extrasStr = fields.get(7).replaceAll("[\\[\\]]", "");
                    List<String> extras = Arrays.stream(extrasStr.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());

                    String description = fields.get(8).replaceAll("[\\[\\]]", "");

                    // System.out.println("Sugar: " + sugar);
                    // System.out.println("Temperature: " + iced);
                    // System.out.println("shots: " + shots);
                    // System.out.println("price: " + price);
                    // System.out.println("name: " + name);
                    // System.out.println("id: " + id);
                    // System.out.println(milkStr);
                    // System.out.println(extrasStr);
                    // System.out.println(description);
                    // System.out.println(extras);
                    // System.out.println(milkOptions);
                    menu.addCoffee(new Coffee(id, name, price, shots, sugar, iced, milkOptions,
                            extras, description));
                }
            }
            // System.out.println(menu.getAllExtras());
        } catch (IOException io) {
            System.out.println("Could not load the file. \nError message: " + io.getMessage());
            System.exit(0);
        }

        return menu;
    }

    private static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        boolean inArray = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == '[') {
                inArray = true;
                currentField.append(c);
            } else if (c == ']') {
                inArray = false;
                currentField.append(c);
            } else if (c == ',' && !inQuotes && !inArray) {
                fields.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        fields.add(currentField.toString().trim());

        return fields;
    }

    private static Coffee getUserCriteria(Menu menu) {

        double menuLowestPrice = menu.getLowestPrice();
        double menuHighestPrice = menu.getHighestPrice();

        Milk selectedMilk = (Milk) JOptionPane.showInputDialog(null, "Please select your preferred type of milk.",
                appName, JOptionPane.OK_CANCEL_OPTION, icon, Milk.values(), Milk.FULL_CREAM);
        if (selectedMilk == null)
            System.exit(0);
        
        List<Milk> milkList = new ArrayList<>();
        milkList.add(selectedMilk);

        // Get number of shots
        int minShots = 0;
        while (minShots < 1) {
            String input = JOptionPane.showInputDialog(
                null,
                "Please enter the number of shots you desire",
                appName,
                JOptionPane.QUESTION_MESSAGE
            );
            
            // Handle cancel or window close
            if (input == null) {
                System.exit(0);
            }
            
            try {
                minShots = Integer.parseInt(input);
                if (minShots < 1) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Please enter a number greater than 0",
                        appName,
                        JOptionPane.ERROR_MESSAGE,
                        icon
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Invalid input. Please enter a whole number.",
                    appName,
                    JOptionPane.ERROR_MESSAGE,
                    icon
                );
            }
        }

        int sugarResponse = JOptionPane.showConfirmDialog(null, "Do you want to add sugar?", appName,
                JOptionPane.YES_NO_OPTION,  JOptionPane.QUESTION_MESSAGE,
                icon);
        String sugar = (sugarResponse == JOptionPane.YES_OPTION) ? "yes" : "no";

        // Get iced coffee preference (YES/NO)
        int icedResponse = JOptionPane.showConfirmDialog(null, "Would you like an iced coffee?", appName,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                icon);
        String iced = (icedResponse == JOptionPane.YES_OPTION) ? "yes" : "no";

        // Get extras (ask repeatedly until user selects "No")
        List<String> extrasList = new ArrayList<>();
        boolean addMoreExtras = true;

        while (addMoreExtras) {
            String extra = (String) JOptionPane.showInputDialog(null, "Please select an extra.",
                    appName, JOptionPane.QUESTION_MESSAGE, icon, menu.getAllExtras().toArray(), "");
            if (extra == null)
                System.exit(0);

            extrasList.add(extra);

            // Ask if they want another extra
            int response = JOptionPane.showConfirmDialog(null, "Would you like to add another extra?",
                    appName, JOptionPane.YES_NO_OPTION,  JOptionPane.QUESTION_MESSAGE,
                    icon);
            if (response == JOptionPane.NO_OPTION) {
                addMoreExtras = false;
            }
        }

        // Ask for the Lowest Price with Cancel Option
        double lowestPrice = 0.0;
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "Please enter the lowest price. The cheapest item is $" + menuLowestPrice,
                    appName, JOptionPane.QUESTION_MESSAGE);

            if (input == null)
                System.exit(0); // Exit if canceled

            try {
                lowestPrice = Double.parseDouble(input);
                if (lowestPrice >= menuLowestPrice)
                    break; // Ensure it's at least the menu's lowest price
                JOptionPane.showMessageDialog(null, "The lowest price must be at least $" + menuLowestPrice, appName, JOptionPane.INFORMATION_MESSAGE, icon);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", appName, JOptionPane.ERROR_MESSAGE, icon);
            }
        }

        // Ask for the Highest Price with Cancel Option
        double highestPrice = 0.0;
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "Please enter the highest price (max $" + menuHighestPrice + "):",
                    appName, JOptionPane.QUESTION_MESSAGE);

            if (input == null)
                System.exit(0); // Exit if canceled

            try {
                highestPrice = Double.parseDouble(input);
                if (highestPrice >= lowestPrice && highestPrice <= menuHighestPrice)
                    break; // Ensure it's within range
                JOptionPane.showMessageDialog(null,
                        "The highest price must be between $" + lowestPrice + " and $" + menuHighestPrice, appName, JOptionPane.INFORMATION_MESSAGE, icon);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", appName, JOptionPane.ERROR_MESSAGE, icon);
            }
        }

        // Print for testing (Replace with actual Coffee object creation)
        System.out.println("Lowest Price: $" + lowestPrice);
        System.out.println("Highest Price: $" + highestPrice);

        Coffee coffeeCriteria = new Coffee(0, "Custom Coffee", 0.0, minShots, sugar, iced, milkList, extrasList,
                "User-selected coffee");
        coffeeCriteria.setMinPrice(lowestPrice);
        coffeeCriteria.setMaxPrice(highestPrice);

        System.out.println("userCoffee:" + coffeeCriteria);

        return coffeeCriteria;
    }

    private static Geek getUserDetails() {

        String name;
        do {
            name = JOptionPane.showInputDialog("Please enter your full name (in format firstname surname): ");
            if (name == null)
                System.exit(0);
            // EDIT 19: call the new method as the while condition - if it returns false,
            // the name is invalid, and must be re-entered
        } while (!isValidFullName(name));

        // Using Regex is a better idea than hard-coding in your logic
        // }while (!name.contains(" "));

        String phoneNumber;
        do {
            phoneNumber = JOptionPane
                    .showInputDialog("Please enter your phone number (10-digit number in the format 0412345678): ");
            if (phoneNumber == null)
                System.exit(0);
        } while (!isValidPhoneNumber(phoneNumber));

        // replace this with regex too!
        /*
         * long phoneNumber = 0;
         * String phoneNumberInput;
         * do {
         * phoneNumberInput = JOptionPane.showInputDialog(
         * null,"Please enter your phone number. Format: 0412838475",appName,JOptionPane
         * .QUESTION_MESSAGE);
         * if (phoneNumberInput == null) System.exit(0);
         * //EDIT - PART 3.3: try-catch to handle invalid input
         * try {
         * phoneNumber = Long.parseLong(phoneNumberInput);
         * } catch (NumberFormatException n) {
         * JOptionPane.showMessageDialog(null,
         * "Invalid entry. Please enter your 10 digit phone number.");
         * }
         * }while(phoneNumberInput.length()!=10);
         */

        String email;
        do {
            email = JOptionPane.showInputDialog(null, "Please enter your email address.", appName,
                    JOptionPane.QUESTION_MESSAGE);
            if (email == null)
                System.exit(0);
        } while (!isValidEmail(email)); // replace with regex
        // }while (!email.contains("@"));

        return new Geek(name, phoneNumber, email);
    }

    /**
     * a very simple regex for full name in Firstname Surname format
     * 
     * @param fullName the candidate full name entered by the user
     * @return true if name matches regex/false if not
     */
    public static boolean isValidFullName(String fullName) {
        String regex = "^[A-Z][a-z]+\\s[A-Z][a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    // EDIT 20: create a method to check the validity of the user's phone number
    /**
     * a regex matcher that ensures that the user's entry starts with a 0 and is
     * followed by 9 digits
     * 
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
     * source:
     * <a href="https://www.baeldung.com/java-email-validation-regex">...</a>
     * 
     * @param email the candidate email entered by the user
     * @return true if email matches regex/false if not
     */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // private static void writeOrderToFile(Geek geek, Coffee coffee, Coffee coffeeCriteria) {
    //     String fileName = "order_details.txt";
    //     try {
    //         Files.writeString(Path.of(fileName), 
    //             "Order details:\n" +
    //             "       Name: " + geek.name() + " (" + geek.phoneNumber() + ")\n" +
    //             "       Email: " + geek.emailAddress() + "\n" +
    //             "       Item: " + coffee.getName() + " (" + coffee.getId() + ") - " + 
    //                          (coffee.getIsIced().equalsIgnoreCase("yes") ? "ICED" : "HOT") + "\n" +
    //             "       Milk: " + coffeeCriteria.getMilkTypes().get(0) + "\n" +
    //             "       Extras: " + String.join(", ", coffeeCriteria.getExtras()) + "\n"
    //         );
    //         JOptionPane.showMessageDialog(null, "Order successfully saved to " + fileName, 
    //             appName, JOptionPane.INFORMATION_MESSAGE, icon);
    //     } catch (IOException e) {
    //         JOptionPane.showMessageDialog(null, "Failed to save order: " + e.getMessage(), 
    //             appName, JOptionPane.ERROR_MESSAGE, icon);
    //     }
    // }
    private static void writeOrderToFile(Geek geek, Coffee coffee, Coffee coffeeCriteria) {
        String fileName = "order_details.txt";
        try {
            // Create the order content with separators
            String orderContent = "\n\n" +  // Add spacing before new order
                "******************************\n" +
                "Order details:\n" +
                "       Name: " + geek.name() + " (" + geek.phoneNumber() + ")\n" +
                "       Email: " + geek.emailAddress() + "\n" +
                "       Item: " + coffee.getName() + " (" + coffee.getId() + ") - " + 
                             (coffee.getIsIced().equalsIgnoreCase("yes") ? "ICED" : "HOT") + "\n" +
                "       Milk: " + coffeeCriteria.getMilkTypes().get(0) + "\n" +
                "       Extras: " + String.join(", ", coffeeCriteria.getExtras()) + "\n" +
                "******************************";
    
            // Check if file exists to determine write mode
            Path path = Path.of(fileName);
            if (Files.exists(path)) {
                // Append to existing file
                Files.writeString(path, orderContent, java.nio.file.StandardOpenOption.APPEND);
            } else {
                // Create new file
                Files.writeString(path, orderContent);
            }
    
            // JOptionPane.showMessageDialog(null, 
            //     "Order successfully saved to " + fileName, 
            //     appName, 
            //     JOptionPane.INFORMATION_MESSAGE, 
            //     icon);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Failed to save order: " + e.getMessage(), 
                appName, 
                JOptionPane.ERROR_MESSAGE, 
                icon);
        }
    }
}
