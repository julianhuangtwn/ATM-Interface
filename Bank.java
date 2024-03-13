import java.util.ArrayList;
import java.util.Random;

public class Bank {
    /**
     * Bank name
     */
    private String m_name;

    /**
     * List of Bank Users
     */
    private ArrayList<User> m_users;

    /**
     * List of Bank Accounts
     */
    private ArrayList<Account> m_accounts;

    /**
     * Constructor, sets bank name
     * @param name
     */
    public Bank(String name){
        m_name = name;
        //Initialize m_users and m_accounts to empty state
        m_users = new ArrayList<User>();
        m_accounts = new ArrayList<Account>();
    }

    /**
     * Acquires a random 5 digit User ID that is unique to the user
     * @return id
     */
    public int getNewUserID() {
        int id;
        Random rand = new Random();
        boolean unique = true;

        //Generates User ID until no repeats 
        do {
            /*
            nextInt generates a number from 0 to 90000, 90000 exclusive
            Since I need 5 digits, the min case where 0 is generated, 
            id will be 0 + 10000 = 10000, max case would be 89999 + 10000 = 99999
            */
            id = rand.nextInt(90000) + 10000;
             unique = true;

            for (User user: m_users) {
                if (id == user.getUserID()){
                    unique = false;
                    break;
                }
            }
        } while (!unique);

        return id;
    }

    /**
     * Acquires a random 7 digit account num that is unique to the account
     * @return id
     */
    public String getNewAccNum() {
        String accNum;
        Random rand = new Random();
        boolean unique = true;
        StringBuilder accNumStr;

        //Generates account number until no repeats 
        do {
            accNum = String.valueOf(rand.nextInt(9000000) + 1000000);
            accNumStr = new StringBuilder(accNum);
            accNumStr.insert(3, "-");
            accNum = accNumStr.toString();
            unique = true;

            for (Account account: m_accounts) {
                if (accNum.equals(account.getAccNum())){
                    unique = false;
                    break;
                }
            }
        } while (!unique);
        
        return accNum;
    }

    /**
     * Add newAccount to m_accounts
     * @param newAccount
     */
    public void addAccount(Account newAccount){
        m_accounts.add(newAccount);
    }

    /**
     * Returns m_name
     * @return m_name
     */
    public String getName(){
        return m_name;
    }

    /**
     * Adds a new User to the bank
     * @param fName First name
     * @param lName Last name
     * @param pin   User pin
     * @return      new User
     */
    public User addUser(String fName, String lName, String pin) {
        //Create new User and add to m_users
        User newUser = new User(fName, lName, pin, this);
        m_users.add(newUser);

        //Create new Account and add to m_accounts, and the new User
        Account newAccount = new Account("Chequing", this);
        m_accounts.add(newAccount);
        newUser.addAccount(newAccount);

        return newUser;
    }

    /**
     * 
     * @param userID User ID
     * @param pin    User PIN
     * @return       User or null for non-matched
     */
    public User login(int userID, String pin){
        for (User user: m_users){
            if ((user.getUserID() == userID) && user.validatePIN(pin)){
                return user;
            }
        }

        return null;
    }
}
