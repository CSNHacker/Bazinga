package com.entropy.bazzinga;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;

public class IndividualBlocks {
    private char letter;
    private int row;
    private int col;
    
    public IndividualBlocks()   {
        letter = '&';
        row = 7;
        col = -1;
    }
    
    public int get_row()    {
        return row;
    }
    
    public int get_col()    {
        return col;
    }
    
    public char get_letter() {
        return letter;
    }
   
    public void fillIn()    {
        Random r = new Random();
        int ascii = r.nextInt(26) + 65;
        letter = (char)ascii;
        int posn = r.nextInt(8);
        while (Randomizer.occupiedCols[posn] == 1)
            posn = r.nextInt(8);
        if (Randomizer.occupiedCols[posn] == 2) {
            row--;
        }
        Randomizer.occupiedCols[posn] = 2;
        col = posn;
              
        //Tower.occupiedCols[position] = true;
    }
    
}
