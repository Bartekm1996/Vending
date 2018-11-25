package machine;

import java.util.Scanner;

public class CommandLine{

    private Scanner scanner = new Scanner(System.in);

    public void userType(){

        boolean cont = true;
        while(cont) {
            System.out.println("Select User Type\n1. Normal User\n2. Operator\n3. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": {
                    normalUserMenu();
                    cont = false;
                    break;
                }
                case "2": {
                    userVer();
                    cont = false;
                    break;
                }
                case "3":
                    cont = false;
                    break;
            }
        }
    }

    private void userVer(){
        System.out.print("Enter User Name : ");
        String userName = scanner.nextLine();
        if(VendingMachineMenu.getHashtable().containsKey(userName)){
            System.out.print("Enter Password : ");
            String password = scanner.nextLine();
            if(VendingMachineMenu.getHashtable().get(userName).equals(password)){
                adminMenu(userName);
            }else{
                System.out.println("Wrong Password Entered");
            }
        }else {
            System.out.println("User Doesn't Exist");
        }
    }

    private void normalUserMenu(){

        boolean more = true;
        while (more)
        {

            System.out.println("S)Show products  I)Insert coin  B)Buy  C)Get Change Q)uit");
            String command = scanner.nextLine().toUpperCase();

            if (command.toUpperCase().equals("S")) {
               VendingMachineMenu.vendingMachine.printProducts("user");
            }
            else if (command.toUpperCase().equals("I")) {
                while (true) {
                    System.out.print("Enter coins value or name : Enter Q to stop adding");
                    System.out.println("Allowed coins - \n\t5 cents\n\t10 cents\n\t20 cents\n\t50 cents\n\t1 euro\n\t2 euro");
                    System.out.print("Enter : ");
                    String choice = scanner.nextLine();
                    if(choice.toUpperCase().equals("Q"))break;
                    else if(choice.equals(".05") || choice.equals("5 cents"))VendingMachineMenu.vendingMachine.getCoinSet().addCoinsToTable(0.05,1);
                    else if(choice.equals(".1") || choice.equals("10 cents"))VendingMachineMenu.vendingMachine.getCoinSet().addCoinsToTable(0.1,1);
                    else if(choice.equals(".2") || choice.equals("20 cents"))VendingMachineMenu.vendingMachine.getCoinSet().addCoinsToTable(0.2,1);
                    else if(choice.equals(".5") || choice.equals("50 cents"))VendingMachineMenu.vendingMachine.getCoinSet().addCoinsToTable(0.5,1);
                    else if(choice.equals("1") || choice.equals("1 euro"))VendingMachineMenu.vendingMachine.getCoinSet().addCoinsToTable(1.0,1);
                    else if(choice.equals("2") || choice.equals("2 euro"))VendingMachineMenu.vendingMachine.getCoinSet().addCoinsToTable(2.0,1);
                    else System.out.println("Invalid Input !!");
                    System.out.println("Coin Added Current Total " + VendingMachineMenu.vendingMachine.getCoinSet().getTotal());
                }

                System.out.println("Current Total = " + VendingMachineMenu.vendingMachine.getCoinSet().getTotal());

            }
            else if (command.toUpperCase().equals("B")) {
                System.out.print("Select product you would like to buy");
                VendingMachineMenu.vendingMachine.printProducts("user");
                String productSelected = scanner.nextLine();
                if(VendingMachineMenu.vendingMachine.getProductsTable().containsKey(productSelected)){
                    if(VendingMachineMenu.vendingMachine.getProductsTable().get(productSelected).getAmount() >  0 && VendingMachineMenu.vendingMachine.getCoinSet().getTotal() >= VendingMachineMenu.vendingMachine.getProductsTable().get(productSelected).getProduct().getPrice()){
                        System.out.print(productSelected + " purchased");
                        VendingMachineMenu.vendingMachine.purchaseProduct(productSelected);
                        System.out.print("Remaninig balance left " + VendingMachineMenu.vendingMachine.getCoinSet().getTotal());
                    }else if(VendingMachineMenu.vendingMachine.getProductsTable().get(productSelected).getAmount() > 0 && VendingMachineMenu.vendingMachine.getCoinSet().getTotal() < VendingMachineMenu.vendingMachine.getProductsTable().get(productSelected).getProduct().getPrice()){
                        System.out.print("Insufficient Funds");
                    }else if(VendingMachineMenu.vendingMachine.getProductsTable().get(productSelected).getAmount() == 0){
                        System.out.print("Product Sold Out");
                    }
                }else {
                    System.out.print("Product Not Contained");
                }
            }
            else if(command.toUpperCase().equals("C")){
                double change = VendingMachineMenu.vendingMachine.giveChange();
                if(change > 0){
                    throw new VendingException("Full change could not be given " + change);
                }else{
                    System.out.println("Cancel Pressed  " + VendingMachineMenu.vendingMachine.getCoinSet().getTotal());
                }
            }
            else if (command.toUpperCase().equals("Q")) {

                System.out.println("GoodBye ");
                more = false;
            }
        }
    }

    private void adminMenu(String userName){

        System.out.println("Hello " + userName);
        while (true) {
            System.out.println("Select One Of The Options ");
            System.out.println("\tS)Show products\n\tAP) Add products\n\tR)Remove Product\n\tA)Add New Product\n\tT)Total Coins \n\tRC)Remove Coins\n\tL)Log out\n\tQ)Quit");

            String command = scanner.nextLine();
            if (command.toUpperCase().equals("S")) {
                VendingMachineMenu.vendingMachine.printProducts("admin");
            } else if (command.toUpperCase().equals("R")) {
                VendingMachineMenu.vendingMachine.printProducts("admin");
                System.out.println("Enter What product you want to remove");
                String product = scanner.nextLine();
                if (VendingMachineMenu.vendingMachine.getProductsTable().containsKey(product)) {
                    VendingMachineMenu.vendingMachine.removeProduct(product);
                }
            }else if(command.toUpperCase().equals("A")){
                String name,imgSrc;int qunatity;double price;
                System.out.println("Enter the name of the product to add");
                    name = scanner.nextLine();
                System.out.println("Enter the quantity you wish to add");
                            qunatity = scanner.nextInt();
                System.out.println("Enter " + name + " price : ");
                    price = scanner.nextDouble();
                System.out.println("Enter the image source of the product : ");
                    imgSrc = scanner.nextLine();
                    if(VendingMachineMenu.vendingMachine.addProduct(name,name,qunatity,price,imgSrc))System.out.println("Product added");
                    else System.out.println("Failed to add product");

            }else if(command.toUpperCase().equals("AP")){
                  VendingMachineMenu.vendingMachine.printProducts("admin");
                  System.out.println("What product would you like to re stock ?");
                  String choice = scanner.nextLine();
                  if(VendingMachineMenu.vendingMachine.getProductsTable().containsKey(choice)){
                      System.out.println("Enter amount you want to add");
                      int amount = scanner.nextInt();
                      if((amount + VendingMachineMenu.vendingMachine.getProductsTable().get(choice).getAmount()) > VendingMachine.MAX_AMOUNT) {
                          System.out.println("Too many products trying to be add");
                      }else {
                          VendingMachineMenu.vendingMachine.getProductsTable().get(choice).addProducts(amount);
                      }
                  }
            }
            else if(command.toUpperCase().equals("RC")){
                System.out.println("Would you like to remove all coins ? Y) Yes N)No");
                String choic = scanner.nextLine();
                if(choic.toUpperCase().equals("Y")){
                    VendingMachineMenu.vendingMachine.getCoinSet().removeAllCoins();
                }

            }else if(command.toUpperCase().equals("T")){
                System.out.println("Total coins in machine " + VendingMachineMenu.vendingMachine.getCoinSet().getCoinsAmount() + " Total value " + VendingMachineMenu.vendingMachine.getCoinSet().getTotalValue());
            }else if(command.toUpperCase().equals("L")){
                System.out.println("Successfully logged out");
                userType();
            }else if(command.toUpperCase().equals("Q")){
                System.out.println("Goodbye " + userName);
                break;
            }
        }
    }




}