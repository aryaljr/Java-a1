package A1;

import java.util.*;
import java.util.stream.Collectors;


public class Menu {
    private final List<Coffee> allCoffees = new ArrayList<>();

    public void addCoffee(Coffee coffee) {
        this.allCoffees.add(coffee);
    }

    public Set<String> getAllExtras() {
        Set<String> allExtras = allCoffees.stream()
                .flatMap(c -> c.getExtras().stream()) // Flatten list of extras
                .collect(Collectors.toSet());

        List<String> extrasList = new ArrayList<>(allExtras);

        extrasList.add(extrasList.size() - 1, "skip");

        allExtras = new HashSet<>(extrasList);
        return allExtras;
    }

    public double getLowestPrice() {
        return allCoffees.stream()
                .mapToDouble(Coffee::getPrice)
                .min()
                .orElse(0.0); // Return 0 if list is empty
    }

    // Method to get the highest price from the coffee menu
    public double getHighestPrice() {
        return allCoffees.stream()
                .mapToDouble(Coffee::getPrice)
                .max()
                .orElse(0.0); // Return 0 if list is empty
    }

    public List<Coffee> findMatch(Coffee coffeeCriteria) {
        List<Coffee> matchingCoffees = new ArrayList<>();

        for (Coffee coffee : allCoffees) {
            if (coffee.getNumberOfShots() != coffeeCriteria.getNumberOfShots()) {
                continue;
            }

            if (!coffee.getHasSuger().equals(coffeeCriteria.getHasSuger())) {
                continue;
            }

            if (!coffee.getIsIced().equals(coffeeCriteria.getIsIced())) {
                continue;
            }

            if (coffee.getPrice() < coffeeCriteria.getMinPrice() || coffee.getPrice() > coffeeCriteria.getMaxPrice()) {
                continue;
            }
            if (coffeeCriteria.getMilkTypes().contains(Milk.NONE)) {
                if (!coffee.getMilkTypes().isEmpty()) {
                    continue;
                }
            } else if (!coffee.getMilkTypes().isEmpty() &&
                    coffee.getMilkTypes().stream().noneMatch(coffeeCriteria.getMilkTypes()::contains)) {
                continue;
            }

            if (coffeeCriteria.getExtras().contains("skip")) {
            } else if (!coffee.getExtras().isEmpty() &&
                    coffee.getExtras().stream().noneMatch(coffeeCriteria.getExtras()::contains)) {
                continue;
            }

            matchingCoffees.add(coffee);
        }

        return matchingCoffees;
    }
}