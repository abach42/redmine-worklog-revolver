package com.abach42.redmineworklogrevolver.Display;

import java.util.Scanner;

/*
 * Console action input to contact user. 
 */
public class UserInput implements ConsoleInputInterface {
    protected static String WRONG_USER_INPUT_MSG = "That wasn't an expected number.";
    public static String DEFAULT_INPUT_STRING = "";
    public static int DEFAULT_INPUT_INT = 1;
    protected static Scanner scanner = new Scanner(System.in);

    @Override
    public String getStringFromUser() {
        String userInput = "";
        userInput = contactInputScanner();
        userInput = fallbackToDefaultString(userInput);

        return userInput;
    }

    protected String contactInputScanner() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "";
    }

    @Override
    public int getIntFromUser() {
        int userInput;
        String userInputString = getStringFromUser();

        try {
            userInput = Integer.parseInt(userInputString);
        } catch (NumberFormatException e) {
            userInput = DEFAULT_INPUT_INT;
        }
        
        return userInput;
    }

    protected String fallbackToDefaultString(String input) {
        if(input.isBlank()) {
            input = DEFAULT_INPUT_STRING;
        }

        return input;
    }
}