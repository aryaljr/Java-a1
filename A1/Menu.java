package A1;

import java.util.*;

public class Menu {
    private final List<Coffee> allCoffees = new ArrayList<>();

    public void addCoffee(Coffee coffee) {
        this.allCoffees.add(coffee);
    }

    // public set<String> getAllExtras() {
    //     Set<String> allExtras = new HashSet<>();
    //     for (Coffee coffee : allCoffees) {
    //         allExtras.addAll(coffee.getExtras());
    //     }
    //     return allExtras;
    // }

    public List<Coffee> findMatch(Coffee coffeeCriteria) {
        List<Coffee> matchingCoffees = new ArrayList<>();
        for (Coffee coffee : allCoffees) {
            if(coffee.getNumberOfShots() != (coffeeCriteria.getNumberOfShots())) continue;

            if(coffee.getHasSuger() != (coffeeCriteria.getHasSuger())) continue;

            if(coffee.getIsIced() != (coffeeCriteria.getIsIced())) continue;

            if(coffee.getPrice() < coffeeCriteria.getMinPrice() || coffee.getPrice() > coffeeCriteria.getMaxPrice()) continue;

            if (!coffee.getMilkTypes().isEmpty() && 
            coffee.getMilkTypes().stream().noneMatch(coffeeCriteria.getMilkTypes()::contains)) continue;

            if (!coffee.getExtras().isEmpty() && 
            coffee.getExtras().stream().noneMatch(coffeeCriteria.getExtras()::contains)) continue;

            matchingCoffees.add(coffee);
        }
        return matchingCoffees;
    }
}