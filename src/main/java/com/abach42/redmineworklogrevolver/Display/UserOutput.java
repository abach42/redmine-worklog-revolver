package com.abach42.redmineworklogrevolver.Display;

public class UserOutput {
    
    public void consoleLog(String row) {
        consoleLog(row, 0);
    }

    /*
     * a little animation effect helps user to see result 
     *  change on screen
     */
    public void consoleLog(String row, int waitMillisec) {
        for (int i = 0; i < row.length(); i++) {
            System.out.print(row.charAt(i));
            wait(waitMillisec);
        }
        System.out.println();
    }

    public void consoleLogInline(String message) {
        System.out.print(message);
    }

    public void printProgressBar(int progress, int max) {
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