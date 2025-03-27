package src.model;

import java.util.*;

import src.enums.*;

public class Menu {
    private final List<Coffee> coffees = new ArrayList<>();

    public void addCoffee(Coffee coffee) {
        coffees.add(coffee);
    }

    public Stack<Coffee> findMatches(Coffee dreamCoffee) {
        Stack<Coffee> matches = new Stack<>();
        for (Coffee coffee : coffees) {
            if (coffee.matches(dreamCoffee)) {
                matches.push(coffee);
            }
        }
        return matches;
    }

    public Set<MilkType> getAllMilkTypes() {
        Set<MilkType> allMilkTypes = new HashSet<>();
        for (Coffee coffee : coffees) {
            allMilkTypes.addAll(coffee.getMilkOptions());
        }
        return allMilkTypes;
    }

    public Set<Extra> getAllExtras() {
        Set<Extra> allExtras = new HashSet<>();
        for (Coffee coffee : coffees) {
            allExtras.addAll(coffee.getExtras());
        }
        return allExtras;
    }
}