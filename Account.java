import java.util.ArrayList;
import java.util.Scanner;

public class Account {

    /**
     * Type of account, such as chequing, savings...
     */
    private String m_type;

    /**
     * Balance of account
     */
    private double m_balance;

    /**
     * Account number
     */
    private String m_accNum;

    /*
     * List of Transactions of the account
     */
    private ArrayList<Transaction> m_transactions;


    /**
     * Constructor, sets type, owner, and accNum
     * Also adds this account to Bank list and owner list
     * @param type  Type of account, such as chequing
     * @param owner User of the account
     * @param bank  The bank where account belongs to
     */
    public Account(String type, Bank bank){
        //Sets type and owner
        m_type = type;
        m_accNum = bank.getNewAccNum();
        m_transactions = new ArrayList<Transaction>();
    }

    /**
     * Returns m_type
     * @return m_type
     */
    public String getAccType(){
        return m_type;
    }

    /**
     * Gets m_accNum
     * @return m_accNum
     */
    public String getAccNum(){
        return m_accNum;
    }

    /**
     * Returns m_balance
     * @return  m_balance
     */
    public double getAccBalance(){
        return m_balance;
    }

    /**
     * Get number of transactions stored
     * @return num of transaction
     */
    public int getNumOfTransaction(){
        return m_transactions.size();
    }

    /**
     * Displays the account summary with type, accNum, balance, and all transactions
     */
    public void accSummary(){
        System.out.println("Account Type: " + m_type);
        System.out.println("Account Number: " + m_accNum);
        System.out.println("Balance: $" + m_balance);
        System.out.println("------------------------");
        for (Transaction trans : m_transactions){
            trans.showTrans();
            System.out.println("------------------------");
        }
        System.out.println("");
    }

    /**
     * Debits the account of amount
     * It is possible for m_balance to go into negative (overdraft)
     * @param amount The amount to debit
     */
    public void debit(double amount){
        m_balance -= amount;
        System.out.println(m_accNum + " New balance: " + m_balance + "\n");
    }

    /**
     * Credits the account of amount
     * @param amount The amount to credit
     */
    public void credit(double amount){
        m_balance += amount;
        System.out.println(m_accNum + " New balance: " + m_balance + "\n");
    }

    /**
     * Add a new Transaction with no memo
     * @param location  Location of transaction
     * @param amount    Amount of transaction
     * @param src       Source account, which is 'this'
     */
    public void addTransaction(String location, double amount){
        Transaction transaction = new Transaction(location, amount);
        m_transactions.add(transaction);
    }

    /**
     * Add a new Transaction with no memo
     * @param location  Location of transaction
     * @param amount    Amount of transaction
     * @param src       Source account, which is 'this'
     * @param memo      Additional memo of transaction
     */
    public void addTransaction(String location, double amount, String memo){
        Transaction transaction = new Transaction(location, amount, memo);
        m_transactions.add(transaction);
    }

    /**
     * Choose an account type
     * @param sc    Scanner
     * @return      String of account type
     */
    public static String pickAccountType(Scanner sc)
    {
        int choice, numOfAccountTypes = 2;
        System.out.println("1: Chequing");
        System.out.println("2: Savings");
        //System.out.println("3: Line of Credit");
        System.out.print("Please choose an account type (1 ~ " + numOfAccountTypes + ", 0 to cancel): ");
        choice = Validate.validateIntRange(0, numOfAccountTypes, sc);

        String type = "";
        switch (choice)
        {
        case 1:
            type = "Chequing";
            break;
        case 2:
            type = "Savings";
            break;
        }
        
        return type;
    }
}