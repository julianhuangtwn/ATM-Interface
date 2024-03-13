import java.util.Scanner;

public class ATM {
    /**
     * Bank object
     */
    private static Bank m_bank = new Bank("Bank of Money");
    
    /**
     * The current active User
     */
    private static User m_activeUser = null;

    public static void main(String[] args) {
        //Scanner instance which will be passed around
        //If Scanner is a static member there is a chance that it can be called twice
        //With multi-threading, causing issues
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        int choice;
        while (!exit){
            //The main menu of ATM
            choice = menu(sc);
            if (choice == 0){
                System.out.println("Thank you for visiting " + m_bank.getName());
                exit = true;
            }
        }
        sc.close();
    }

    /**
     * Menu of ATM
     * @param sc Scanner
     * @return choice
     */
    private static int menu(Scanner sc){
        //The number of menu choices
        int numOfMenuChoices = 10;

        System.out.println("1: Create User" +
            "\n2: Login" +
            "\n3: Show transaction history" +
            "\n4: Deposit" +
            "\n5: Withdraw" +
            "\n6: Transfer" +
            "\n7: Add transaction" +
            "\n8: Add account" +
            "\n9: Delete account" +
            "\n10: Logout" +
            "\n----------------" +
            "\n0: Exit");
        
        //Displays current User
        if (m_activeUser == null) {
            System.out.println("\nNot logged in");
        } else {
            System.out.println("\nHello " + m_activeUser.getName());
        }
        System.out.print("Please pick one of the above options (1 ~ " + numOfMenuChoices + ", 0 to exit): ");

        //An integer input validation method
        int choice = Validate.validateIntRange(0, numOfMenuChoices, sc);

        switch (choice) {
            case 1:
                //Create user
                inputAddUser(sc);
                break;
            case 2:
                //Login
                inputLogin(sc);
                break;
            default:
                //Other choices will be routed differently with activeUser 
                checkLoggedIn(choice, sc);
        }

        return choice;
    }

    /**
     * Checks if there is active User
     * @param choice Menu choice
     * @param sc     Scanner
     */
    private static void checkLoggedIn(int choice, Scanner sc){
        if (m_activeUser != null){
            switch(choice){
                case 3:
                    //Display account summary
                    m_activeUser.showAccountInfo(sc);
                    break;
                case 4:
                    m_activeUser.deposit(sc);
                    break;
                case 5:
                    m_activeUser.withdraw(sc);
                    break;
                case 6:
                    m_activeUser.transfer(sc);
                    break;
                case 7:
                    m_activeUser.addTransaction(sc);
                    break;
                case 8:
                    //Create new account
                    Account newAccount = new Account(Account.pickAccountType(sc), m_bank);
                    m_activeUser.addAccount(newAccount);
                    System.out.println("New account created: ");
                    newAccount.accSummary();
                    break;
                case 9:
                    m_activeUser.deleteAccount(sc);
                    break;
                case 10:
                    //Logs user out
                    m_activeUser = null;
                    System.out.println("\nLogged out\n");
            }
        } else {
            System.out.println("\nPlease log in first by choosing \"Login\" in the menu\n");
        }
    }

    /**
     * Adds a user with name and PIN, also creates a chequing account by default
     * @param bank
     * @param sc
     */
    private static void inputAddUser(Scanner sc){
        System.out.print("Enter first name: ");
        String fName = sc.nextLine();
        System.out.print("Enter last name: ");
        String lName = sc.nextLine();
        System.out.println("\nSet up your new PIN");
        String pin = pinInput(sc);
        m_bank.addUser(fName, lName, pin);   
    }

    /**
     * User inputs a 4 digit integer PIN number 
     * @param sc Scanner
     * @return   The entered pin
     */
    private static String pinInput(Scanner sc){
        String pin = "";
        boolean validPIN = false;
        System.out.print("Please enter a 4 digit pin: ");
        while (!validPIN){
            pin = sc.nextLine();
            //Regex matching 4 digits in a row
            if (pin.matches("[0-9]{4}")){
                validPIN = true;
            } else{
                System.out.print("Invalid PIN, please enter a 4 digit number: ");
            }
        }

        return pin;
    }

        /**
     * User inputs login ID and PIN, and will set active user to the found User
     * @param sc Scanner
     */
    private static void inputLogin(Scanner sc){
        System.out.print("\nPlease input user ID: ");
        int userID = Validate.validateIntRange(10000, 99999, sc);
        String pin = pinInput(sc);
        m_activeUser = m_bank.login(userID, pin);
        if (m_activeUser != null){
            System.out.println("\nWelcome! " + m_activeUser.getName() + "\n");
        } else {
            System.out.println("\nUser ID or password is incorrect\n");
        }
    }
}