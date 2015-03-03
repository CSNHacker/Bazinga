package com.entropy.bazzinga;

public class RandomLetter {
    private char letter;
    private int freq;
    private double percentage;
    private int lower;
    private int upper;
    
    public RandomLetter()   {
        letter = '&';
        freq = 0;
        percentage = 0.0;
    }
    
    public char get_letter() {
        return letter;
    }
    
    public void set_letter(char ch) {
        letter = ch;
    }
    
    public void inc_freq()  {
        freq++;
    }
    
    public void set_percentage(int total)   {
        double Dtotal = (double)total;
        //System.out.println("total = " + total + "freq = " + freq);
        percentage = (freq/Dtotal) * 100;
        //System.out.println("percentage = " + percentage);
    }
    
    public void set_weight(int low, int high)    {
        lower = low;
        upper = high;
    }
    
    public double get_percentage()  {
        return percentage;
    }
    
    public int get_lower()  {
        return lower;
    }
    
    public int get_upper()  {
        return upper;
    }
    
    @Override
    public String toString()    {
        String properties = "letter: " + letter + " ";
        properties += "freq: " + freq + " ";
        properties += "percetage: " + percentage + " ";
        properties += "lower: " + lower + " ";
        properties += "upper: " + upper + " ";
        return properties;
    }
}

