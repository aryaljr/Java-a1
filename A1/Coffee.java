
package A1;
import java.util.*;

/**
 * @author Dr Andreas Shepley (asheple2@une.edu.au | andreashepley01@gmail.com)
 * created for COSC120 (Trimester 1 2022)
 * last revised: Trimester 1 2024
 */

 public class Coffee {

    // menu item ID,menu item name,price,numberOfShots,sugar,iced,milk,extras,description
    //fields
    private final long id;
    private final String name;
    private final double price;
    private final int numberOfShots;
    private final boolean sugar;
    private final boolean iced;

    // private final Milk milk;
    // private final Extra extra;

    private final List<Milk> milkTypes;
    private final List<Extra> extras;

    private final String description;

    private double minPrice;
    private double maxPrice;

    public Coffee(long id, String name, double price, int numberOfShots, boolean sugar, boolean iced, List<Milk> milkTypes, List<Extra> extras, String description, double minPrice, double maxPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.numberOfShots = numberOfShots;
        this.sugar = sugar;
        this.iced = iced;
        // this.milk = milk;
        // this.extra = extra;
        this.milkTypes = milkTypes;
        this.extras = extras;
        this.description = description;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    //getter and setters methods for all

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getNumberOfShots() {
        return numberOfShots;
    }

    public boolean getHasSuger() {
        return sugar;
    }

    public boolean getIsIced() {
        return iced;
    }

    // public Milk getMilk() {
    //     return milk;
    // }
    // public Extra getExtra() {
    //     return extra;
    // }

    public List<Milk> getMilkTypes() {
        return milkTypes;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public String getDescription() {
        return description;
    }
    public double getMinPrice() {
        return minPrice;
    }
    public double getMaxPrice() {
        return maxPrice;
    }
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }
    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String toString() {
        // return "Coffee{" +
        //         "id=" + id +
        //         ", name='" + name + '\'' +
        //         ", price=" + price +
        //         ", numberOfShots=" + numberOfShots +
        //         ", sugar=" + sugar +
        //         ", iced=" + iced +
        //         ", milk=" + milk +
        //         ", extra=" + extra +
        //         ", description='" + description + '\'' +
        //         ", minPrice=" + minPrice +
        //         ", maxPrice=" + maxPrice +
        //         '}';
        
        //print string representation
        return String.format("ID: %d, Name: %s, Price: $%.2f, Shots: %d, Sugar: %s, Iced: %s, Milk: %s, Extras: %s, Description: %s, Min Price: $%.2f, Max Price: $%.2f",
                id, name, price, numberOfShots, sugar, iced, milkTypes, extras, description, minPrice, maxPrice);
    } 

}