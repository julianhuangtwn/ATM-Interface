import java.util.Date;

public class Transaction {
    /**
     * Location of the transaction
     */
    private String m_location;

    /**
     * Amount of transaction
     */
    private double m_amount;

    /**
     * Timestamp of transaction
     */
    private Date m_date;

    /**
     * An optional memo
     */
    private String m_memo;

    /**
     * Constructor, sets location, amount, srcAccount; memo is blank
     * @param m_location    Location of transaction
     * @param m_amount      Amount of transaction
     * @param m_srcAccount  Account where transaction happens
     */
    public Transaction(String location, double amount){
        m_location = location;
        m_amount = amount;
        m_date = new Date();
        m_memo = "";
    }

    /**
     * Constructor that calls 3 arg constructor, sets memo
     * @param m_location    Location of transaction
     * @param m_amount      Amount of transaction
     * @param m_srcAccount  Account where transaction happens
     * @param memo          Additional memo for transaction
     */
    public Transaction(String location, double amount, String memo){
        //Calls and uses the 3 arg constructor to set other members
        this(location, amount);
        m_memo = memo;
    }

    /**
     * Display details of transaction
     */
    public void showTrans(){
        System.out.println("Date: " + m_date);
        System.out.println("Location: " + m_location);
        System.out.println("Amount: $" + m_amount);
        if (m_memo != ""){
            System.out.println("Memo: " + m_memo);
        } else {
            System.out.println("No memo");
        }
    }
}
