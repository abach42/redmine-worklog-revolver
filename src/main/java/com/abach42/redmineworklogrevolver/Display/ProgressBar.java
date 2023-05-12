package com.abach42.redmineworklogrevolver.Display;

public class ProgressBar extends UserOutput {
    protected int progressIterator = 0;
    protected int max = 0;

    public ProgressBar(int max) {
        setMax(max);
    }

    public void drawProgessBar() {
        int percentComplete = (int) ((double) progressIterator / max * 100);
        print("\r[" + "#".repeat(percentComplete) + " ".repeat(100 - percentComplete) + "] " + percentComplete + "%");
    }

    public void setProgressIterator(int progressIterator) {
        this.progressIterator = progressIterator;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void incrementProgressIterator() {
        progressIterator++;
    }
}
