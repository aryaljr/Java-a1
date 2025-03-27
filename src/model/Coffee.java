package src.model;

import src.enums.*;
import java.util.*;

public class Coffee {
    private final String id;
    private final String name;
    private final double price;
    private final int numberOfShots;
    private final Sugar sugar;
    private final Temperature temperature;
    private final List<MilkType> milkOptions;
    private final List<Extra> extras;
    private final String description;

    public Coffee(String id, String name, double price, int numberOfShots, Sugar sugar, Temperature temperature,
                 List<MilkType> milkOptions, List<Extra> extras, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.numberOfShots = numberOfShots;
        this.sugar = sugar;
        this.temperature = temperature;
        this.milkOptions = new ArrayList<>(milkOptions);
        this.extras = new ArrayList<>(extras);
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getNumberOfShots() { return numberOfShots; }
    public Sugar getSugar() { return sugar; }
    public Temperature getTemperature() { return temperature; }
    public List<MilkType> getMilkOptions() { return new ArrayList<>(milkOptions); }
    public List<Extra> getExtras() { return new ArrayList<>(extras); }
    public String getDescription() { return description; }

    public String describe() {
        return String.format("%s - %s (Price: $%.2f)", name, description, price);
    }

    public boolean matches(Coffee dreamCoffee) {
        if (dreamCoffee.getNumberOfShots() > 0 && this.numberOfShots != dreamCoffee.getNumberOfShots()) {
            return false;
        }
        if (dreamCoffee.getSugar() != null && this.sugar != dreamCoffee.getSugar()) {
            return false;
        }
        if (dreamCoffee.getTemperature() != null && this.temperature != dreamCoffee.getTemperature()) {
            return false;
        }
        if (!dreamCoffee.getMilkOptions().isEmpty() && 
            !dreamCoffee.getMilkOptions().contains(MilkType.NONE) &&
            Collections.disjoint(this.milkOptions, dreamCoffee.getMilkOptions())) {
            return false;
        }
        if (!dreamCoffee.getExtras().isEmpty() && 
            !dreamCoffee.getExtras().contains(Extra.NONE) &&
            !this.extras.containsAll(dreamCoffee.getExtras())) {
            return false;
        }
        if (dreamCoffee.getPrice() > 0 && this.price > dreamCoffee.getPrice()) {
            return false;
        }
        return true;
    }
}