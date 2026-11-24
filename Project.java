import java.io.*;
import java.util.*;

/** One product in the shop. */
class Item {
    private String id;
    private String name;
    private int quantity;
    private double price;

    public Item(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /** Prints 40 instead of 40.0 when the price is a whole number. */
    public String priceText() {
        if (price == Math.floor(price)) {
            return String.valueOf((long) price);
        }
        return String.valueOf(price);
    }

    @Override
    public String toString() {
        return id + " | " + name + " | Qty: " + quantity + " | " + priceText();
    }
}

public class Project {
    private static final String FILE = "inventory.txt";
    private static ArrayList<Item> items = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1: addItem(); break;
                case 2: displayItems(); break;
                case 3: searchItem(); break;
                case 4: sellItem(); break;
                case 5: lowStockReport(); break;
                case 6: sortByQuantity(); break;
                case 7:
                    saveToFile();
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Enter a number from 1 to 7.");
            }
        }
    }

    static void showMenu() {
        System.out.println();
        System.out.println("===== Simple Inventory Tracker =====");
        System.out.println("1. Add item");
        System.out.println("2. Display items");
        System.out.println("3. Search item");
        System.out.println("4. Sell item");
        System.out.println("5. Low stock report");
        System.out.println("6. Sort by quantity");
        System.out.println("7. Exit");
    }

    // ---------- input helpers (re-ask until the value is valid) ----------
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.hasNextLine() ? sc.nextLine().trim() : null;
            if (line == null) {          // end of input: leave the program cleanly
                saveToFile();
                System.exit(0);
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.hasNextLine() ? sc.nextLine().trim() : null;
            if (line == null) {
                saveToFile();
                System.exit(0);
            }
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    static String readLine(String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) {
            saveToFile();
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    // ---------- features ----------
    static String nextId() {
        int max = 100;                   // so the first ID is I101
        for (Item it : items) {
            try {
                int n = Integer.parseInt(it.getId().substring(1));
                if (n > max) max = n;
            } catch (NumberFormatException e) {
                // ignore IDs that do not follow the pattern
            }
        }
        return "I" + (max + 1);
    }

    static void addItem() {
        String name = readLine("Name: ");
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        int qty = readInt("Quantity: ");
        if (qty < 0) {
            System.out.println("Quantity cannot be negative.");
            return;
        }
        double price = readDouble("Price: ");
        if (price <= 0) {
            System.out.println("Price must be greater than 0.");
            return;
        }
        Item item = new Item(nextId(), name, qty, price);
        items.add(item);
        System.out.println("Item added. " + item);
    }

    static void displayItems() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("ID | Name | Qty | Price");
        for (Item it : items) {
            System.out.println(it);
        }
    }

    static void searchItem() {
        String key = readLine("Enter item ID or name: ").toLowerCase();
        boolean found = false;
        for (Item it : items) {                       // linear search
            if (it.getId().toLowerCase().equals(key)
                    || it.getName().toLowerCase().contains(key)) {
                System.out.println(it);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Item not found.");
        }
    }

    static Item findById(String id) {
        for (Item it : items) {
            if (it.getId().equalsIgnoreCase(id)) return it;
        }
        return null;
    }

    static void sellItem() {
        String id = readLine("Item ID: ");
        Item it = findById(id);
        if (it == null) {
            System.out.println("Item not found.");
            return;
        }
        int qty = readInt("Quantity to sell: ");
        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }
        if (qty > it.getQuantity()) {
            System.out.println("Insufficient stock. Available: " + it.getQuantity());
            return;
        }
        it.setQuantity(it.getQuantity() - qty);
        System.out.println("Stock updated. " + it.getId() + " | " + it.getName()
                + " | Qty: " + it.getQuantity());
        if (it.getQuantity() == 0) {
            System.out.println("Item is now out of stock.");
        }
    }

    static void lowStockReport() {
        int count = 0;
        for (Item it : items) {
            if (it.getQuantity() < 5) {
                count++;
                System.out.println(count + ". " + it.getId() + " | " + it.getName()
                        + " | Qty: " + it.getQuantity());
            }
        }
        if (count == 0) {
            System.out.println("No items are low on stock.");
        }
    }

    static void sortByQuantity() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        ArrayList<Item> copy = new ArrayList<>(items);   // original list stays unchanged
        for (int i = 0; i < copy.size() - 1; i++) {      // bubble sort
            for (int j = 0; j < copy.size() - 1 - i; j++) {
                if (copy.get(j).getQuantity() > copy.get(j + 1).getQuantity()) {
                    Item temp = copy.get(j);
                    copy.set(j, copy.get(j + 1));
                    copy.set(j + 1, temp);
                }
            }
        }
        for (int i = 0; i < copy.size(); i++) {
            Item it = copy.get(i);
            System.out.println((i + 1) + ". " + it.getId() + " | " + it.getName()
                    + " | " + it.getQuantity());
        }
    }

    // ---------- file handling ----------
    static void loadFromFile() {
        File f = new File(FILE);
        if (!f.exists()) return;                         // first run: nothing to load
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length != 4) continue;             // skip damaged lines
                try {
                    items.add(new Item(p[0].trim(), p[1].trim(),
                            Integer.parseInt(p[2].trim()),
                            Double.parseDouble(p[3].trim())));
                } catch (NumberFormatException e) {
                    // skip lines with bad numbers
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + FILE + ": " + e.getMessage());
        }
    }

    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (Item it : items) {
                pw.println(it.getId() + "," + it.getName() + ","
                        + it.getQuantity() + "," + it.priceText());
            }
        } catch (IOException e) {
            System.out.println("Could not save " + FILE + ": " + e.getMessage());
        }
    }
}