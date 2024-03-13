import java.util.Scanner;
import java.util.ArrayList;
//MessageDigest provides functions for hashing
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    /**
     * User's first name
     */
    private String m_fName;

    /**
     * User's last name
     */
    private String m_lName;

    /**
     * User ID
     */
    private int m_userID;

    /**
     * SHA-256 hash of user's PIN
     * Hashing is one way, hence unable to reverse engineer the original PIN
     */
    private byte m_pin[];

    /**
     * List of user's accounts
     */
    private ArrayList<Account> m_accounts;

    /**
     * Constructor, creates new user
     * @param fName User's first name
     * @param lName User's last name
     * @param pin   User's PIN
     * @param bank  Bank which the User is customer of
     */
    public User(String fName, String lName, String pin, Bank bank){
        m_fName = fName;
        m_lName = lName;

        // Stores PIN's SHA-256 hash
        try{
            //This acquires a SHA-256 algorithm instance as md
            //getInstance could throw NoSuchAlgorithmException if not supported
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //digest returns a byte array by operating on PIN in bytes
            m_pin = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No Such Algorithm Exception");
            //Prints stack trace for debugging
            e.printStackTrace();
            //Exits program with status 1
            System.exit(1);
        }

        //Generates uuid for a new user
        m_userID = bank.getNewUserID();

        //Creates instance storing Accounts for m_accounts 
        m_accounts = new ArrayList<Account>();

        System.out.println("New user created");
        System.out.println("\nWelcome to " + bank.getName());
        System.out.println("-------------------------------");
        System.out.println("User: " + m_fName + " " + m_lName);
        System.out.println("ID: " + m_userID);
        System.out.println();
    }

    /**
     * Adds newAccount to m_accounts
     * @param newAccount new Account that was created
     */
    public void addAccount(Account newAccount){
        m_accounts.add(newAccount);
    }

    /**
     * Returns full name
     * @return User name
     */
    public String getName(){
        return m_fName + " " + m_lName;
    }

    /**
     * Gets m_userID
     * @return m_userID
     */
    public int getUserID(){
        return m_userID;
    }

    /**
     * Get number of accounts User has
     * @return number of accounts
     */
    public int getNumOfAcc(){
        return m_accounts.size();
    }

    /**
     * Returns chosen account number
     * @param accIndex  Account index in m_accounts
     * @return          Account number
     */
    public String getAccNum(int accIndex){
        return m_accounts.get(accIndex).getAccNum();
    }

    /**
     * Returns chosen account type
     * @param accIndex  Account index in m_accounts
     * @return          Account type
     */
    public String getAccType(int accIndex){
        return m_accounts.get(accIndex).getAccType();
    }

    /**
     * Get account balance
     * @param account   Account index
     * @return
     */
    public double getAccBalance(int accIndex){
        return m_accounts.get(accIndex).getAccBalance();
    }

    /**
     * Validates PIN with MessageDigest
     * @return true for matched pin
     */
    public boolean validatePIN(String pin){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //Returns a bool to equate two hashed values, which uses the isEqual method in MessageDigest
            return MessageDigest.isEqual(md.digest(pin.getBytes()), m_pin);
        } catch (NoSuchAlgorithmException e){
            System.err.println("No Such Algorithm Exception");
            //Debug purposes
            e.printStackTrace();
            //Exits system if exception occurs
            System.exit(1);
        }

        return false;
    }

     /**
     * Displays selected account info and transactions
     * @param sc Scanner
     */
    public void showAccountInfo(Scanner sc){
        int accIndex = chooseAccount(sc);
        if (accIndex >= 0){
            if (transactionExist(accIndex)){
                m_accounts.get(accIndex).accSummary();
            } else {
                System.out.println("\nNo transactions\n");
            }
        } else if (accIndex == -2){
            System.out.println("No accounts exist\n");
        } else {
            System.out.println("Operation cancelled");
        }
    }

    /**
     * Checks if at least one transaction exists
     * @param accIndex
     * @return
     */
    public boolean transactionExist(int accIndex){
        return (m_accounts.get(accIndex).getNumOfTransaction() != 0);
    }

    /**
     * Deposit amount entered by user into account chosen
     * @param sc Scanner
     */
    public void deposit(Scanner sc){
        int accIndex = chooseAccount(sc);
        Account acc =  m_accounts.get(accIndex);
        double balance = getAccBalance(accIndex);
        double amount = 0;
        if (accIndex >= 0){
            System.out.println("Account: " + getAccNum(accIndex));
            System.out.println("Account balance: " + balance);
            System.out.print("Enter deposit amount: ");
            amount = Validate.validateAmount(sc);
            acc.credit(amount);
            acc.addTransaction("ATM", amount, "Deposit");
        } else if (accIndex == -2){
            System.out.println("No accounts exist\n");
        } else {
            System.out.println("Operation cancelled");
        }
    }
    
    /**
     * Withdraw amount entered by user from account chosen
     * @param sc Scanner
     */
    public void withdraw(Scanner sc){
        int accIndex = chooseAccount(sc);
        Account acc = m_accounts.get(accIndex);
        double balance = getAccBalance(accIndex);
        double amount = 0;
        if (balance != 0){
            if (accIndex >= 0){
                System.out.println("Account: " + getAccNum(accIndex));
                System.out.println("Account balance: " + balance);
                System.out.print("Enter withdrawal amount: ");
                do {
                    amount = Validate.validateAmount(sc);
                    if (amount > balance){
                        System.out.print("Transfer amount exceeds source account balance! Please re-enter: ");
                    }
                } while (amount > balance);
                acc.debit(amount);
                acc.addTransaction("ATM", (amount - amount * 2), "Withdraw");
            } else if (accIndex == -2){
                System.out.println("No accounts exist\n");
            } else {
                System.out.println("Operation cancelled");
            }
        } else {
            System.out.println("Account balance is 0, please try another account.\n");
        }
    }
    
    /**
     * Transfer amount from srcAccIndex to destAccIndex
     * @param srcAccIndex   Source account index
     * @param destAccIndex  Destination account index
     * @param amount        Amount to transfer
     */
    public void transfer(Scanner sc){
        System.out.println("\nSource account");
        int srcAccIndex = chooseAccount(sc);
        int destAccIndex = 0;
        double amount = 0;
        if (srcAccIndex >= 0){
            do {
                System.out.println("\nDestination account");
                destAccIndex = chooseAccount(sc);
                if (destAccIndex == -1){
                    System.out.println("Operation cancelled");
                    break;
                }else if (srcAccIndex == destAccIndex){
                    System.out.print("You cannot pick the same accounts! Please choose an account that is not # " + srcAccIndex + 1 + ": ");
                }
            } while (srcAccIndex == destAccIndex);
        } else if (srcAccIndex == -2){
            System.out.println("No accounts exist\n");
        } else {
            System.out.println("Operation cancelled");
        }

        if (srcAccIndex >= 0 && destAccIndex >= 0) {
            Account srcAcc = m_accounts.get(srcAccIndex);
            Account destAcc = m_accounts.get(destAccIndex);
            double srcAccBalance = getAccBalance(srcAccIndex);
            double destAccBalance = getAccBalance(destAccIndex);
            System.out.println("Source account balance: " + srcAccBalance);
            System.out.println("Destination account balance: " + destAccBalance);

            if (srcAccBalance >= 0){
                System.out.print("Enter the amount to transfer (enter 0 amount to cancel): ");
                do {
                    amount = Validate.validateAmount(sc);
                    if ((amount > srcAccBalance)){
                        System.out.print("Transfer amount exceeds source account balance! Please re-enter: ");
                    }
                } while (amount > srcAccBalance);
                srcAcc.debit(amount);
                srcAcc.addTransaction("ATM", (amount - (amount * 2)), "Transfer to " + srcAcc.getAccNum());
                destAcc.credit(amount);
                destAcc.addTransaction("ATM", amount, "Transfer from " + srcAcc.getAccNum());
            } else {
                System.out.println("Source account balance is 0, please try another account.");
            }
        }
    }

     /**
     * Add a transaction with location and amount. memo optional
     * @param sc
     */
    public void addTransaction(Scanner sc){
        int accIndex = chooseAccount(sc);
        Account chosenAcc = m_accounts.get(accIndex);
        if (accIndex >= 0) {
            System.out.print("Enter location of transaction: ");
            String location = sc.nextLine();
            System.out.print("Enter amount of transaction: ");
            double amount = Validate.validateAmount(sc);
            System.out.print("(Optional) Enter additional memo (Click enter for no memo): ");
            String memo = sc.nextLine();
            chosenAcc.debit(amount);
            amount -= amount * 2;
            if (memo == ""){
                chosenAcc.addTransaction(location, amount);
            } else {
                chosenAcc.addTransaction(location, amount, memo);
            }
            
        } else if (accIndex == -2){
            System.out.println("No accounts exist\n");
        } else {
            System.out.println("Operation cancelled");
        }
        System.out.println("");
    }

    /**
     * Deletes an account
     * @param sc Scanner
     */
    public void deleteAccount(Scanner sc)
    {
        int accIndex = chooseAccount(sc);
        if (accIndex >= 0){
            Account chosenAcc = m_accounts.get(accIndex);
            chosenAcc.accSummary();
            if (chosenAcc.getAccBalance() != 0)
            {
                System.out.println("Account needs to have 0 balance before deletion! Please withdraw first\n");
            } else {
                System.out.print("Are you sure you want to delete this account (Y/N): ");
                if (Validate.validateYesNo(sc)){
                    System.out.println("\nAccount " + chosenAcc.getAccNum() + " deleted\n");
                    m_accounts.remove(accIndex);
                } else {
                    System.out.println("\nOperation cancelled\n");
                }
            }
        } else if (accIndex == -2){
            System.out.println("No accounts exist\n");
        } else {
            System.out.println("Operation cancelled");
        }
        
    }

    /**
     * Choose account for some methods
     * @param sc Scanner
     * @return Account index choice
     */
    private int chooseAccount(Scanner sc){
        int choice = 0, numOfAcc = getNumOfAcc();
        if (numOfAcc != 0){
            for (int i = 0; i < getNumOfAcc(); i++){
                System.out.println((i + 1) + ": " + getAccNum(i) + " " + getAccType(i));
            }
            if (numOfAcc > 1){
                System.out.print("Please choose an account " + "(1 ~ " + getNumOfAcc() + ", 0 to cancel): ");
                choice = Validate.validateIntRange(0, getNumOfAcc(), sc);
            } else if (numOfAcc == 1){
                choice = 1;
            }
            return choice - 1;
        } else {
            return -2;
        }    
    }
}