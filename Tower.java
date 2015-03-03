package com.entropy.bazzinga;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;

public class Tower {
    
    private int qNum;
    private String answer;
    private boolean beginning;
    private int numOfDisplayLetters;
    private int position;
    private String displayLetters;
    
    
    Tower() {
        qNum = -1;
        answer = "";
        beginning = false;
        numOfDisplayLetters = 0;
        position = -1;
        displayLetters = "";
    }
    
    public int get_qNum()   {
        return qNum;
    }
    
    public String get_answer()   {
        return answer;
    }
    
    public boolean get_beginning()   {
        return beginning;
    }
    
    public int get_numOfDisplayLetters()   {
        return numOfDisplayLetters;
    }
    
    public int get_position()   {
        return position;
    }
    
    public String get_displayLetters()  {
        return displayLetters;
    }
    
    public boolean set_beginning() {
        Random r = new Random();
        int torf = r.nextInt(2);
        if (torf == 1) return true;
        else return false;
    }
    
    public int set_numOfDisplayLetters() {
        int length = this.answer.length();
        if (length >= 3 && length < 6) return 2;
        else if (length >= 6 && length < 9) return 3;
        return -1;
    }
    
    public int set_position(int previousPosition){
        Random r = new Random();
        int posn = r.nextInt(8);
        
        while(posn == previousPosition){
            posn = r.nextInt(8);
        }
        
        /*if (posn == previousPosition)   {
            this.set_position(previousPosition);
            return 
        }
        else
            return posn;*/
        
        return posn;
    }
    
    public String set_displayLetters()  {        
        String subStr;
        if (beginning == true){
            //System.out.println("answer.substring = " + answer.substring(0, numOfDisplayLetters));
            return answer.substring(0, numOfDisplayLetters);
        }
        else    {
            //System.out.println("answer.length() = " + answer.length());
            //System.out.println("numOfDisplayLetters = " + numOfDisplayLetters);
            
            subStr = answer.substring(answer.length()-numOfDisplayLetters, answer.length());
            //System.out.println("subStr = " + subStr);
            StringBuffer buf = new StringBuffer(subStr);
            //System.out.println("buf.reverse().toString() = " + buf.reverse().toString());
            return buf.reverse().toString();
        }
    }        
    
    @Override
    public String toString()    {
        String properties = "qNum: " + qNum + " ";
        properties += "answer: " + answer + " ";
        properties += "beginning: " + beginning + " ";
        properties += "numOfDisplayLetters: " + numOfDisplayLetters + " ";
        properties += "position: " + position + " ";
        properties += "dislpayLetters: " + displayLetters;
        return properties;
    }
    
    public void fillIn(int previousQNum, int[] questionNums, HashMap answers, int previousPosition)    {
        
        Random r = new Random();         
        int Qindex = r.nextInt(questionNums.length);     
                
        if (questionNums[Qindex] == previousQNum) 
            this.fillIn(previousQNum, questionNums, answers, previousPosition);
        else    {
            this.qNum = questionNums[Qindex];
            this.answer = (String)answers.get(qNum);
            this.beginning = set_beginning();
            this.numOfDisplayLetters = set_numOfDisplayLetters();
            this.position = set_position(previousPosition);
            Randomizer.occupiedCols[position] = 1;
            this.displayLetters = set_displayLetters();
        }
        
    }
                
}

