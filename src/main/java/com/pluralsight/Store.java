
package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starter code for the Online Store workshop.
 * Students will complete the TODO sections to make the program work.
 */
public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: id|name|price)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited):
     * id|name|price
     * <p>
     * Example line:
     * A17|Wireless Mouse|19.99
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        try {
            BufferedReader reader =new BufferedReader(new FileReader("products.csv"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                String id = tokens[0];
                String name = tokens[1];
                double price = Double.parseDouble(tokens[2]);

                Product product = new Product(id, name, price);

                inventory.add(product);
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {

        System.out.printf("%-10s %-25s %10s%n", "ID", "Name", "Price");
        System.out.println("----------------------------------------------------------");

        for (Product product : inventory) {
            System.out.printf("%-10s %-25s $%9.2f%n",
                    product.getId(),
                    product.getName(),
                    product.getPrice());
        }

        while (true) {
            System.out.println("Pick a product by id to enter into cart or enter 'b' to go back:");
            String id = scanner.nextLine();

            if (id.equalsIgnoreCase("b")) {
                return;
            }

            Product selectedProduct = findProductById(id, inventory);

            if (selectedProduct != null) {
                cart.add(selectedProduct);
                System.out.println(selectedProduct.getName() + " has been added to cart!");
            } else {
                System.out.println("Item not found.");
            }

            System.out.println();
        }

    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        Double total = 0.0;

        for (Product product : cart) {
            System.out.println(product.getName() + " - $" + product.getPrice());
            total += product.getPrice();
        }

        System.out.println("The total is - $" + total);

        boolean check = false;
        while (!check) {
            System.out.println("Enter (C) to checkout or (X) to return.");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("C")) {
                check = true;
                checkOut(cart, total, scanner);
            } else if (input.equalsIgnoreCase("X")) {
                return;
            } else {
                System.out.println("Invalid Entry");
                check = false;
            }
        }
    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {

        boolean check = false;
        while (!check) {
            System.out.println("Are you sure you want to checkout? (yes or no)");
            String yesOrNo = scanner.nextLine();
            if (yesOrNo.equalsIgnoreCase("yes")) {
                System.out.println("Enter your name:");
                String name = scanner.nextLine();

                System.out.println("Enter payment info:");
                String payment = scanner.nextLine();

                printReceipt(name, scanner, cart);

                check = true;
            } else if (yesOrNo.equalsIgnoreCase("no")) {
                return;
            } else {
                System.out.println("Choose yes to checkout or no to go back.");
                check = false;
            }
        }
        cart.clear();
    }

    public static void printReceipt(String name, Scanner scanner, ArrayList<Product> cart) {
        System.out.println("================= Receipt: =================");
        Double total = 0.0;

        for (Product product : cart) {
            System.out.println(product.getName() + " - $" + product.getPrice());
            total += product.getPrice();
        }

        System.out.println("The total is - $" + total);
        System.out.println("Paid by: " + name);
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product product : inventory) {
            if (id.equalsIgnoreCase(product.getId())) {
                return product;
            }
        }
        // TODO: loop over the list and compare ids
        return null;
    }
}

 