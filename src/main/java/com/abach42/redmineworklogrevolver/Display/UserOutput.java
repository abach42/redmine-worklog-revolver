package com.abach42.redmineworklogrevolver.Display;

public class UserOutput {
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_GREEN = "\u001B[32m";

    public void writeException(String message) {
        write(ANSI_RED);
        write(message);
        write(ANSI_RESET);
    }

    public void writeNotice(String message) {
        write(ANSI_GREEN);
        write(message);
        write(ANSI_RESET);
    }

    public void write(String message) {
        write(message, 0);
    }

    /*
     * a little animation effect helps user to see result 
     *  change on screen
     */
    public void write(String message, int retardMilliSec) {
        for (int i = 0; i < message.length(); i++) {
            print(message.charAt(i));
            wait(retardMilliSec);
        } 
        System.out.println();
    }

    public void writeInLine(String message) {
        print(message);
    }

    public void drawProgessBar(int progress, int max) {
        int percentComplete = (int) ((double) progress / max * 100);
        print("\r[" + "#".repeat(percentComplete) + " ".repeat(100 - percentComplete) + "] " + percentComplete + "%");
    }

    public void wait(int waitMillisec) {
        try {
            Thread.sleep(waitMillisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void print(char character) {
        print(String.valueOf(character));
    }

    protected void print(String message) {
        System.out.print(message);
    }
}
