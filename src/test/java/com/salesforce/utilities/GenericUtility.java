package com.salesforce.utilities;

import com.salesforce.base.BaseClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class GenericUtility extends BaseClass {
    /**
     * Field to store all the environment params from /src/main/resources/manifest.json
     */

    private static String pwd = System.getProperty("user.dir");
    /**
     * Converts the InputStream into a String
     * @param responseInputStream Http Response in the InputStream format
     * @return Response in String format
     */
    public static String getStringFromInputStream(InputStream responseInputStream){
        StringBuilder strResponse = new StringBuilder("");
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(responseInputStream));
            strResponse = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                strResponse.append(line);
                strResponse.append('\r');
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResponse.toString();
    }

    /**
     * Utility method to get a random number
     * @return randInt, a random number generated with a seed value of 1000
     */
    public static String getRandomNumber() {
        Random rand = new Random();
        int randInt = rand.nextInt(1000);
        return String.valueOf(randInt);
    }

    /**
     * These overloading method will wait for page to load
     * @param num - can enter any number of seconds to wait for page to load
     * @throws InterruptedException
     */
    public static void waitForPageToLoad(int num) {
        try {
            Thread.sleep(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets number of seconds from "/src/main/resources/manifest.json" file
     * @throws InterruptedException
     */
    public static void waitForPageToLoad()  {
        int num =  Integer.valueOf(manifestJsonObject.get("waitSeconds").toString());
        try {
            Thread.sleep(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * generates random phone number
     * @return
     */
    public static String getPhoneNumber(){
        int num1, num2, num3; //3 numbers in area code
        int set2, set3; //sequence 2 and 3 of the phone number
        Random generator = new Random();
        //Area code number; Will not print 8 or 9
        num1 = generator.nextInt(7) + 1; //add 1 so there is no 0 to begin
        num2 = generator.nextInt(8); //randomize to 8 because 0 counts as a number in the generator
        num3 = generator.nextInt(8);
        /* Sequence two of phone number
         the plus 100 is so there will always be a 3 digit number
         randomize to 643 because 0 starts the first placement so if i randomized up to 642 it would only go up yo 641 plus 100
         and i used 643 so when it adds 100 it will not succeed 742*/
        set2 = generator.nextInt(643) + 100;
        /*Sequence 3 of number
         add 1000 so there will always be 4 numbers
         8999 so it wont succeed 9999 when the 1000 is added*/
        set3 = generator.nextInt(8999) + 1000;
        return "+1"+"(" + num1 + "" + num2 + "" + num3 + ")" + set2 + "-" + set3;
    }

    /**
     * generates random website name
     * @return
     */
    public static String getNewWebsite(){
        String string = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder generatedString = new StringBuilder();
        Random random = new Random();
        //you can change 'n' to any number as needed
        int n = 10;
        for(int i=0; i<n;i++){
            int number = random.nextInt(string.length()-1);
            generatedString.append(string.charAt(number));
        }
        return generatedString.toString()+".com";
    }

    /**
     * will generate random string
     * @return
     */
    public static String getNewEmail() {
        String string = "abcdefghijklmnopqrstuvwxyz" + "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        //you can change 'n' to any number as needed
        int n = 10;
        for (int i = 0; i < n; i++) {
            int number = random.nextInt(string.length() - 1);
            stringBuilder.append(string.charAt(number));
        }
        int number = random.nextInt(3) + 1;
        switch (number) {
            case 1:
                return stringBuilder + "@yahoo.com";
            case 2:
                return stringBuilder + "@gmail.com";
            default:
                return stringBuilder + "@outlook.com";
        }
    }

    /**
     * will generate random string
     * @return
     */
    public static String getRandomString(){
        String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"abcdefghijklmnopqrstuvwxyz";
        StringBuilder generatedString = new StringBuilder();
        Random random = new Random();
        //you can change 'n' to any number as needed
        int n = 10;
        for(int i=0; i<n;i++){
            int number = random.nextInt(string.length()-1);
            generatedString.append(string.charAt(number));
        }
        return generatedString.toString();
    }

    /**
     * creates random new name
     * @return
     */
    public static String getNewName(){
        String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"abcdefghijklmnopqrstuvwxyz";
        StringBuilder generatedName = new StringBuilder();
        Random random = new Random();
        //you can change 'n' to any number as needed
        int n = 10;
        for(int i=0; i<n;i++){
            int number = random.nextInt(string.length()-1);
            generatedName.append(string.charAt(number));
        }
        return generatedName.toString();
    }
}
