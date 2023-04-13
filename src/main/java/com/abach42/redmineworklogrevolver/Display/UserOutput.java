package com.abach42.redmineworklogrevolver.Display;

public class UserOutput {

    public void write(String message) {
        write(message, 0);
    }

    /*
     * a little animation effect helps user to see result 
     *  change on screen
     */
    public void write(String message, int retardMilliSec) {
        for (int i = 0; i < message.length(); i++) {
            System.out.print(message.charAt(i));
            wait(retardMilliSec);
        } 
        System.out.println();
    }

    public void writeInLine(String message) {
        System.out.print(message);
    }

    public void writeProgressBar(int progress, int max) {
        int percentComplete = (int) ((double) progress / max * 100);
        System.out.print("\r[" + "#".repeat(percentComplete) + " ".repeat(100 - percentComplete) + "] " + percentComplete + "%");
    }

    public void wait(int waitMillisec) {
        try {
			Thread.sleep(waitMillisec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}