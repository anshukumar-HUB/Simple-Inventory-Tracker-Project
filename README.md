Simple Inventory Tracker

A console-based Java application that helps a small shop manage its stock. Items are stored as objects in an ArrayList, and the data is saved to a file (inventory.txt) so nothing is lost when the program closes.

PS No. 25 | Language: Java | Concepts: ArrayList, Searching, Sorting, Methods, File Handling

Features
Add item: add a product with a name, quantity and price. IDs are generated automatically (I101, I102, ...).
Display items: show every item in the inventory.
Search item: find items by ID or by name (linear search, not case sensitive).
Sell item: reduce stock, with a check for insufficient stock.
Low stock report: list items with quantity below 5.
Sort by quantity: show items from lowest to highest quantity (bubble sort on a copy, so the original list is unchanged).
File handling: data is loaded from inventory.txt at start-up and saved when you exit.
Input validation: quantity can never become negative, price must be greater than 0, and wrong input (letters instead of numbers, unknown ID, invalid menu choice) is rejected with a clear message.
Requirements
JDK 8 or above
Terminal or Command Prompt

No external libraries are needed.

How to Run
bash
javac Main.java
java Main
Menu
===== Simple Inventory Tracker =====
1. Add item
2. Display items
3. Search item
4. Sell item
5. Low stock report
6. Sort by quantity
7. Exit

Use option 7 (Exit) to save your changes to inventory.txt.

Sample Run
Enter choice: 1
Name: Notebook
Quantity: 50
Price: 40
Item added. I101 | Notebook | Qty: 50 | 40
Enter choice: 4
Item ID: I101
Quantity to sell: 20
Insufficient stock. Available: 10
File Format

Each line of inventory.txt stores one item:

ID,Name,Quantity,Price
I101,Notebook,50,40
Project Structure
.
├── Main.java        # Item class and Main class (all features)
├── inventory.txt    # Created automatically on first exit
└── README.md
Limitations
Single-user, console only.
Data is saved only when you exit through option 7; closing the window abruptly loses that session's changes.
Item names containing a comma can break the file format.
There is no separate restock, edit or delete option.
Future Scope
Graphical interface (Swing or JavaFX) or web front end
Database (MySQL or SQLite) instead of a text file
Restock, edit and delete options
Sales history, reports and billing
User login with roles (owner and staff)
Automatic reorder alerts
