import java.io.*;
import java.util.*;

public class ATMService {
    private Map<String, User> users = new HashMap<>();
    private final String USER_FILE = "users.txt";
    private final String STATEMENT_DIR = "statements/";

    public ATMService() {
        loadUsers();
        File dir = new File(STATEMENT_DIR);
if (!dir.exists()) dir.mkdirs();

    }

   @SuppressWarnings("unchecked")
public void loadUsers() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
        users = (Map<String, User>) ois.readObject();
    } catch (Exception e) {
        users = new HashMap<>();
    }
}


    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    public User authenticate(String cardNumber, String pin) {
        User user = users.get(cardNumber);
        return (user != null && user.getPin().equals(pin)) ? user : null;
    }

    public void registerUser(String cardNumber, String pin) {
        if (users.containsKey(cardNumber)) {
            System.out.println("User already exists.");
            return;
        }
        User user = new User(cardNumber, pin, 0.0);
        users.put(cardNumber, user);
        saveUsers();
        System.out.println("User registered successfully!");
    }

    public void deposit(User user, double amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount.");
            return;
        }
        user.setBalance(user.getBalance() + amount);
        saveUsers();
        writeStatement(user, "Deposited: ₹" + amount);
        System.out.println("Deposited successfully!");
    }

    public void withdraw(User user, double amount) {
        if (amount <= 0 || amount > user.getBalance()) {
            System.out.println("Insufficient balance or invalid amount.");
            return;
        }
        user.setBalance(user.getBalance() - amount);
        saveUsers();
        writeStatement(user, "Withdrawn: ₹" + amount);
        System.out.println("Withdrawn successfully!");
    }

    public void checkBalance(User user) {
        System.out.println("Current Balance: ₹" + user.getBalance());
    }

    public void miniStatement(User user) {
        String filename = STATEMENT_DIR + user.getCardNumber() + "_statement.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            System.out.println("\n--- Mini Statement ---");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("-----------------------\n");
        } catch (IOException e) {
            System.out.println("No statement found.");
        }
    }

    private void writeStatement(User user, String action) {
        String filename = STATEMENT_DIR + user.getCardNumber() + "_statement.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(new Date() + " - " + action + " | Balance: ₹" + user.getBalance());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing statement.");
        }
    }
}
