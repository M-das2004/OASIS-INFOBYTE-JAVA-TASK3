import java.util.*;

// Class 1: Transaction
class Transaction {
    String type;
    int amount;
    String recipient;

    Transaction(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    Transaction(String type, int amount, String recipient) {
        this.type = type;
        this.amount = amount;
        this.recipient = recipient;
    }

    public String toString() {
        if (recipient != null) {
            return type + " of Rs." + amount + " to " + recipient;
        }
        return type + " of Rs." + amount;
    }
}

// Class 2: TransactionHistory
class TransactionHistory {
    private List<Transaction> transactions;

    TransactionHistory() {
        transactions = new ArrayList<>();
    }

    void addTransaction(Transaction t) {
        transactions.add(t);
    }

    void showHistory() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction t : transactions) {
                System.out.println("- " + t);
            }
        }
    }
}

// Class 3: User
class User {
    String userID;
    int pin;
    int balance;
    TransactionHistory history;

    User(String userID, int pin, int balance) {
        this.userID = userID;
        this.pin = pin;
        this.balance = balance;
        this.history = new TransactionHistory();
    }

    boolean validatePin(int inputPin) {
        return this.pin == inputPin;
    }

    void deposit(int amount) {
        balance += amount;
        System.out.println("Successfully deposited Rs." + amount);
        history.addTransaction(new Transaction("Deposit", amount));
    }

    boolean withdraw(int amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Please collect your cash.");
            history.addTransaction(new Transaction("Withdraw", amount));
            return true;
        } else {
            System.out.println("Insufficient balance.");
            return false;
        }
    }

    boolean transfer(User recipient, int amount) {
        if (amount <= balance) {
            balance -= amount;
            recipient.balance += amount;
            history.addTransaction(new Transaction("Transfer", amount, recipient.userID));
            recipient.history.addTransaction(new Transaction("Received", amount, this.userID));
            System.out.println("Successfully transferred Rs." + amount + " to " + recipient.userID);
            return true;
        } else {
            System.out.println("Insufficient balance.");
            return false;
        }
    }

    void showBalance() {
        System.out.println("Current Balance: Rs." + balance);
    }

    void showHistory() {
        history.showHistory();
    }
}

// Class 4: Bank
class Bank {
    private Map<String, User> users;

    Bank() {
        users = new HashMap<>();
        // Preloaded users
        users.put("user1", new User("user1", 1234, 10000));
        users.put("user2", new User("user2", 5678, 8000));
    }

    User authenticate(String userID, int pin) {
        User user = users.get(userID);
        if (user != null && user.validatePin(pin)) {
            return user;
        }
        return null;
    }

    User getUser(String userID) {
        return users.get(userID);
    }
}

// Class 5: ATM (Main Class)
public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        System.out.println("==== Welcome to the ATM System ====");

        // Login Loop
        User currentUser = null;
        while (currentUser == null) {
            System.out.print("Enter User ID: ");
            String userID = sc.nextLine();
            System.out.print("Enter PIN: ");
            int pin = sc.nextInt();
            sc.nextLine(); // consume newline

            currentUser = bank.authenticate(userID, pin);
            if (currentUser == null) {
                System.out.println("Invalid User ID or PIN. Please try again.\n");
            }
        }

        System.out.println("\nLogin successful! Welcome " + currentUser.userID + "\n");

        // ATM Menu
        int choice = 0;
        do {
            System.out.println("====== ATM Menu ======");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    currentUser.showHistory();
                    break;

                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    int withdrawAmount = sc.nextInt();
                    sc.nextLine();
                    currentUser.withdraw(withdrawAmount);
                    break;

                case 3:
                    System.out.print("Enter amount to deposit: ");
                    int depositAmount = sc.nextInt();
                    sc.nextLine();
                    currentUser.deposit(depositAmount);
                    break;

                case 4:
                    System.out.print("Enter recipient User ID: ");
                    String recipientID = sc.nextLine();
                    User recipient = bank.getUser(recipientID);
                    if (recipient == null) {
                        System.out.println("Recipient not found.");
                    } else {
                        System.out.print("Enter amount to transfer: ");
                        int transferAmount = sc.nextInt();
                        sc.nextLine();
                        currentUser.transfer(recipient, transferAmount);
                    }
                    break;

                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }

            System.out.println();
        } while (choice != 5);

        sc.close();
    }
}