package src.service;

import src.model.*;
import src.enums.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MenuSearcher {
    private final Scanner scanner;
    private Menu menu;

    public MenuSearcher() {
        this.scanner = new Scanner(System.in);
    }

    public Menu loadMenu(String filename) throws IOException {
        Menu loadedMenu = new Menu();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length < 9) continue;
                
                String id = parts[0].trim();
                String name = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                int shots = Integer.parseInt(parts[3].trim());
                Sugar sugar = Sugar.fromString(parts[4].trim());
                Temperature temp = Temperature.fromString(parts[5].trim());
                
                List<MilkType> milkOptions = Arrays.stream(parts[6].replace("[", "").replace("]", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(MilkType::fromString)
                    .collect(Collectors.toList());
                
                List<Extra> extras = Arrays.stream(parts[7].replace("[", "").replace("]", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Extra::fromString)
                    .collect(Collectors.toList());
                
                String description = parts[8].trim().replace("\"", "");
                
                loadedMenu.addCoffee(new Coffee(id, name, price, shots, sugar, temp, milkOptions, extras, description));
            }
        }
        return loadedMenu;
    }

    public Coffee getDreamCoffee() {
        System.out.println("\nLet's find your dream coffee!");
        
        Set<MilkType> availableMilkTypes = menu.getAllMilkTypes();
        List<MilkType> selectedMilkTypes = new ArrayList<>();
        
        System.out.println("\nAvailable milk types:");
        for (MilkType type : availableMilkTypes) {
            System.out.println("- " + type.getDisplayName());
        }
        
        System.out.print("\nEnter milk types (comma separated) or 'none': ");
        String milkInput = scanner.nextLine().trim();
        if (!milkInput.equalsIgnoreCase("none")) {
            for (String milk : milkInput.split(",")) {
                MilkType type = MilkType.fromString(milk.trim());
                if (type != MilkType.NONE) {
                    selectedMilkTypes.add(type);
                }
            }
        }
        
        int shots = getIntInput("Enter number of shots (0 to skip): ", 0, 10);
        
        Sugar sugar = null;
        System.out.print("Sugar required? (yes/no/any): ");
        String sugarInput = scanner.nextLine().trim();
        if (!sugarInput.equalsIgnoreCase("any")) {
            sugar = Sugar.fromString(sugarInput);
        }
        
        Temperature temp = null;
        System.out.print("Iced? (yes/no/any): ");
        String tempInput = scanner.nextLine().trim();
        if (!tempInput.equalsIgnoreCase("any")) {
            temp = Temperature.fromString(tempInput);
        }
        
        Set<Extra> availableExtras = menu.getAllExtras();
        List<Extra> selectedExtras = new ArrayList<>();
        
        System.out.println("\nAvailable extras:");
        for (Extra extra : availableExtras) {
            System.out.println("- " + extra.getDisplayName());
        }
        
        System.out.print("\nEnter extras (comma separated) or 'none': ");
        String extraInput = scanner.nextLine().trim();
        if (!extraInput.equalsIgnoreCase("none")) {
            for (String extra : extraInput.split(",")) {
                Extra e = Extra.fromString(extra.trim());
                if (e != Extra.NONE) {
                    selectedExtras.add(e);
                }
            }
        }
        
        double maxPrice = getDoubleInput("Enter maximum price (0 for no limit): $", 0, 100);
        
        return new Coffee("DREAM", "Dream Coffee", maxPrice, shots, sugar, temp, selectedMilkTypes, selectedExtras, "");
    }

    public Geek getGeekInfo(Coffee selectedCoffee) {
        System.out.println("\nAlmost done! Just need some details...");
        String name = getStringInput("Your name: ");
        String email = getStringInput("Your email: ");
        return new Geek(name, email, selectedCoffee);
    }

    public void saveOrder(Geek geek) throws IOException {
        String filename = "order_" + geek.name().replaceAll("\\s+", "_") + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("abracadabra-doo! Here's a coffee for you!");
            writer.println("Name: " + geek.name());
            writer.println("Email: " + geek.email());
            writer.println("Selected Coffee: " + geek.selectedCoffee().describe());
        }
        System.out.println("Order saved to " + filename);
    }

    public int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %.2f and %.2f\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void close() {
        scanner.close();
    }
}