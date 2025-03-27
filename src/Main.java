package src;

import src.service.MenuSearcher;
import src.model.Menu;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        MenuSearcher searcher = new MenuSearcher();
        
        try {
            Menu menu = searcher.loadMenu("menu.txt");
            searcher.setMenu(menu);
            System.out.println("Menu loaded successfully!");
            
            var dreamCoffee = searcher.getDreamCoffee();
            var matches = menu.findMatches(dreamCoffee);
            
            if (matches.isEmpty()) {
                System.out.println("\nNo matching coffees found.");
                return;
            }
            
            System.out.println("\nFound " + matches.size() + " matching coffees:");
            int index = 1;
            for (var coffee : matches) {
                System.out.println(index++ + ". " + coffee.describe());
            }
            
            int choice = searcher.getIntInput("\nEnter your choice (1-" + matches.size() + ") or 0 to cancel: ", 
                                             0, matches.size());
            
            if (choice == 0) {
                System.out.println("Order cancelled.");
                return;
            }
            
            var selected = matches.get(choice - 1);
            System.out.println("\nYou selected: " + selected.describe());
            
            var geek = searcher.getGeekInfo(selected);
            searcher.saveOrder(geek);
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            searcher.close();
        }
    }
}