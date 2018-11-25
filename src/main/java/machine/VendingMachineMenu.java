package machine;

import javafx.application.Application;
import org.jetbrains.annotations.Contract;
import java.util.Scanner;
import java.util.SortedMap;

public class VendingMachineMenu{

    public static SortedMap<String,String> logInDetails;
    public static VendingMachine vendingMachine;


    public static void main(String[] args) {
        logInDetails = Util.logInDetails("logIn.txt");
        vendingMachine = new VendingMachine("Products.txt","Money.txt");
        boolean found = true;
        while (found) {
            System.out.println("What type of interface \n 1. Graphical Interface \n 2. Command Line Interface\n 3. Programme Exit");
            Scanner scanner = new Scanner(System.in);
            String string = scanner.nextLine();
            if (string.matches("1")) {
                Application.launch(Graphical.class,args);
                found = false;
            } else if (string.matches("2")) {
                CommandLine commandLine = new CommandLine();
                            commandLine.userType();
            }else if(string.matches("3")){
               closeAll();
               System.out.println("GoodBye");
               System.exit(0);
            } else {
                System.out.print("No Valid choice has been entered\n");
            }
        }


    }


    protected static void closeAll(){
        Util.writeToCsv("logIn.txt",VendingMachineMenu.getHashtable());
        Util.writeToCsv("Money.txt",vendingMachine.getCoinSet().getCoinsTable());
        Util.writeProductsToFile("Products.txt",vendingMachine.getProductsTable());
    }


    @Contract(pure = true)
    protected static SortedMap<String,String> getHashtable(){
        return logInDetails;
    }

    /*
    public static void addLoginDetails(String username,String password){
        getHashtable().put(username,password);
    }
    */



}
