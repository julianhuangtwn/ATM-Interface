import java.util.Scanner;

/**
 * Class that contains input validation methods
 */
public class Validate {
    /**
     * Private constructor as no instances of Validate should be created
     * All methods will be static/class level
     */
    private Validate(){};

    /**
     * Validates user input integer 
     * @param sc Scanner
     * @return
     */
    public static int validateInt(Scanner sc){
        int choice;

        //Checks if scanner has an integer, this effectively asked for input
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            sc.nextLine();
        }
        choice = sc.nextInt();
        sc.nextLine();

        return choice;
    }

    /**
     * Validates user input integer within range
     * @param min min of range
     * @param max max of range
     * @param sc  Scanner
     * @return
     */
    public static int validateIntRange(int min, int max, Scanner sc){
        int choice;

        do {
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.nextLine();
            }
            choice = sc.nextInt();
            sc.nextLine();
            if (choice < min || choice > max) {
                System.out.print("Invalid choice. Please enter " + min + " ~ " + max + ": ");
            }
        } while (choice < min || choice > max);

        return choice;
    }

    /**
     * Validates user input double 
     * @param sc Scanner
     * @return
     */
    public static double validateAmount(Scanner sc){
        double amount;

        while (!sc.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a number: ");
            sc.nextLine();
        }
        amount = sc.nextDouble();
        sc.nextLine();

        return amount;
    }

    /**
     * Validate a yes no answer
     * @param sc Scanner
     * @return   True or false
     */
    public static boolean validateYesNo(Scanner sc){
        char choice;

        //Gets a single char from next()
        choice = sc.next().charAt(0);
        while ((choice != 'Y' && choice != 'N' && choice != 'y' && choice != 'n')){
            System.out.print("Invalid input. Please enter y/Y or n/N: ");
            choice = sc.next().charAt(0);
        }

        if (choice == 'Y' || choice == 'y'){
            return true;
        } else {
            return false;
        }
    }
}
