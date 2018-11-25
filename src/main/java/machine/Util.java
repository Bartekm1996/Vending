package machine;

import java.io.*;
import java.util.*;

public class Util{

    private static File file = new File(System.getProperty("user.home") + "/" + ".vendingMachine");

    public static SortedMap<String,String> logInDetails(String fileName){
        SortedMap<String,String> loginDet = new TreeMap<>();

        String[] det;
        String line;
        File fileLogins = checkFile(fileName);
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLogins))){

            while((line = bufferedReader.readLine()) != null){

                det = line.split(",");
                if(det[0].startsWith("\"") && det[0].endsWith("\"")){
                    removeQuotes(det);
                }

                loginDet.put(det[0],det[1]);

            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return loginDet;
    }



    public static void writeToCsv(String fileName,SortedMap<String,String> table){

        File fileToWriteToFile = checkFile(fileName);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToWriteToFile))){

            Set<String> keys = table.keySet();
            for(String key : keys){
                bufferedWriter.write(key + "," + table.get(key));
                bufferedWriter.newLine();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static File checkFile(String fileName){
        if(!file.exists())file.mkdir();
        File fileCre = new File(file + "/" + fileName);
        if(!fileCre.exists()){
            try{
                fileCre.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return fileCre;
    }

    public static Hashtable<String,LineItem> readProductsFromFile(String fileName){

        String[] det;
        String line;
        Hashtable<String,LineItem> products = new Hashtable<>();

        File fileToreadProdcuts =  checkFile(fileName);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToreadProdcuts))){

            while((line = bufferedReader.readLine()) != null){
                det = line.split(",");
                if(det[0].startsWith("\"") && det[0].endsWith("\"")){
                    removeQuotes(det);
                }

                if(det.length == 5){
                    products.put(det[0],new LineItem(det[0],det[1],Integer.parseInt(det[2]),Double.parseDouble(det[3]),det[4]));
                }else if(det.length == 2){
                    products.put(det[0],new LineItem(det[0],Integer.parseInt(det[1])));
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return products;

    }

    public static void writeProductsToFile(String fileName,Hashtable<String,LineItem> productsTable){

        File fileToWriteToFile = checkFile(fileName);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToWriteToFile))){

            Set<String> keys = productsTable.keySet();
            for(String key : keys){
                LineItem lineItem = productsTable.get(key);
                bufferedWriter.write(key + "," + lineItem.getProduct().getName() + "," + Integer.toString(lineItem.getAmount()) + "," + Double.toString(lineItem.getProduct().getPrice()) + ","
                        + lineItem.getProduct().getImage().impl_getUrl());
                bufferedWriter.newLine();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void removeQuotes(String[] split){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < split.length; i++){
            char[] chars = split[i].toCharArray();
            for(int j = 1; j < chars.length - 1;j++){
                stringBuilder.append(chars[j]);
            }
            split[i] = stringBuilder.toString();
            stringBuilder.setLength(0);
        }
    }





}
