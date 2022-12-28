package com.example.cmpt276assignmen3.GameLogic;

/**
 *  Set up the basic attributes for the Game class
 */
public class Game {
    private int numRows;
    private int numCols;
    private int numBuns;

    public Game(int numRows, int numCols, int numBuns) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.numBuns = numBuns;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public int getNumBuns() {
        return numBuns;
    }

    public void setNumBuns(int numBuns) {
        this.numBuns = numBuns;
    }
}
