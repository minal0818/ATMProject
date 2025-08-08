import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATMService atmService = new ATMService();

        System.out.println("------ Welcome to ATM System ------");

        while (true) {
            System.out.println("\n1. Login\n2. Register\n3. Exit");
            System.out.print("Enter option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Card Number: ");
                String card = sc.nextLine();
                System.out.print("Enter PIN: ");
                String pin = sc.nextLine();

                User user = atmService.authenticate(card, pin);
                if (user == null) {
                    System.out.println("Invalid credentials!");
                } else {
                    System.out.println("Login successful!");
                    boolean session = true;
                    while (session) {
                        System.out.println("\n1. Deposit\n2. Withdraw\n3. Check Balance\n4. Mini Statement\n5. Logout");
                        System.out.print("Select option: ");
                        int option = sc.nextInt();

                        switch (option) {
                            case 1:
                                System.out.print("Enter amount to deposit: ₹");
                                double dep = sc.nextDouble();
                                atmService.deposit(user, dep);
                                break;
                            case 2:
                                System.out.print("Enter amount to withdraw: ₹");
                                double wd = sc.nextDouble();
                                atmService.withdraw(user, wd);
                                break;
                            case 3:
                                atmService.checkBalance(user);
                                break;
                            case 4:
                                atmService.miniStatement(user);
                                break;
                            case 5:
                                session = false;
                                System.out.println("Logged out.");
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                    }
                }

            } else if (choice == 2) {
                System.out.print("Enter new Card Number: ");
                String card = sc.nextLine();
                System.out.print("Set 4-digit PIN: ");
                String pin = sc.nextLine();
                atmService.registerUser(card, pin);
            } else if (choice == 3) {
                System.out.println("Thank you for using ATM.");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }
}
