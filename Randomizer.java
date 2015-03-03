package com.entropy.bazzinga;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;

public class Randomizer	{
	
	public static final int NUM_OF_Q = 3;
	
	public static HashMap<Integer, String> answers = new HashMap();
	public static HashMap<Integer, String> questions = new HashMap();
	protected int[] questionNums = new int[NUM_OF_Q];
        protected static String[] wordList = new String[NUM_OF_Q];
        protected static String[] ansList = new String[NUM_OF_Q];
        protected Tower[] towers = new Tower[2]; //Two answers for which pre-exising towers will be placed
        protected IndividualBlocks[] preX = new IndividualBlocks[2];
        protected RandomLetter[] inAns = new RandomLetter[26];
        static int totalWeight = 0;
        
        protected static char grid[][] = new char[8][8];        
        public static int occupiedCols[] = new int[8];
	
	static final int totalQs = 5;
        static final int RATIO = 90;
                
        static final int PRIORITY_RATIO = 50;
        protected static char[] priorityList = new char[26];
	
	Randomizer()	{
            for (int p=0; p<towers.length; p++)
                towers[p] = new Tower();

            for (int p=0; p<preX.length; p++)
                preX[p] = new IndividualBlocks();

            for (int p=0; p<inAns.length; p++)
                inAns[p] = new RandomLetter();

            for (int i=0; i<questionNums.length; i++)
                    questionNums[i] = -1;

            for (int i=0; i<grid[0].length; i++)    {
                for (int j=0; j<grid[0].length; j++)
                    grid[i][j] = '*';
            }
            
            for (int i=0; i<priorityList.length; i++) {
                    priorityList[i] = '*';
            }

            	answers.put(0, "PLUTO");
            	questions.put(0, "In 2006, this was no longer counted as being a planet.");
//            answers.put(1, "SATURN");
            	answers.put(1, "VENUS");
            	questions.put(1, "Which is the hottest planet in our solar system?");
//            answers.put(3, "GRAVITY");
           		answers.put(2, "DOG");
           		questions.put(2, "Which was the first living creature to be launched into space?");
 				answers.put(3, "BELT");
 				questions.put(3, "The solar system’s main asteroid ____ is between Mars and Jupiter.");
//            answers.put(6, "ORBIT");
 				answers.put(4, "RAT");
 				questions.put(4, "What was the first animal born in space?");
 				answers.put(5, "GAS");
 				questions.put(5, "Saturn and Jupiter are known as ___ giants.");
//            answers.put(9, "METEOR");
//            answers.put(10, "CHARON");
//            answers.put(11, "MERCURY");
//            
//            answers.put(13, "VENUS");
 				answers.put(6, "LUNA");
 				questions.put(6, "What is the name of Earth's moon?");
 				answers.put(7, "OZONE");
 				questions.put(7, "This is a protective layer surrounding the Earth.");
 				answers.put(8, "EARTH");
 				questions.put(8, "The only planet not named after a Roman or Greek god.");
//            answers.put(15, "SATURN");
//            answers.put(16, "HELIUM");
//            answers.put(17, "SEVEN");
//            answers.put(18, "MERCURY");
//            answers.put(19, "SATURN");
//            answers.put(20, "METHANE");
//            answers.put(21, "URANUS");
//            answers.put(22, "NEPTUNE");
//            answers.put(23, "POSEIDON");
//            answers.put(24, "NEPTUNE");
//            answers.put(25, "SCOOTER");
//            answers.put(26, "HUBBLE");
//            answers.put(27, "TRITON");
//            answers.put(28, "NEPTUNE");
//            answers.put(29, "BURNEY");
//            answers.put(30, "DWARF");
//            answers.put(31, "DOUBLE");
//            answers.put(32, "ORBIT");
//            answers.put(33, "CERES");
//            answers.put(34, "SUNLIGHT");
//            answers.put(35, "METALS");
//            answers.put(36, "CARBON");
//            answers.put(37, "FIVE");
//            answers.put(38, "CERES");
//            answers.put(39, "IDA");
//            answers.put(40, "TAIL");
//            answers.put(41, "WIND");
//            answers.put(42, "COMET");
//            answers.put(43, "KUIPER");
//            answers.put(44, "INNER");
//            answers.put(45, "WARMER");
//            answers.put(46, "SHOOTING");
//            answers.put(47, "RADIANT");
//            answers.put(48, "IRON");
//            answers.put(49, "DEBRIS");                               
	}
	
	// Function to select 10 random questions from Qbank
	public String[] selectQs() 	{
            Random r = new Random();
            int num = -1, flag=0;
            for (int i=0; i<questionNums.length; i++)	{
                    flag = 0;
                    num = r.nextInt(totalQs);
                    for (int j=0; j<=i; j++)	{
                            if (questionNums[j] == num)	{
                                    i--;
                                    flag = 1;
                            }
                    }
                    if (flag == 0)
                            questionNums[i] = num;
            }

            // Loop to display the 10 randomly selected answers to questions
            for (int p=0; p<questionNums.length; p++)   {               
                wordList[p] = questions.get(questionNums[p]);
                ansList[p] = answers.get(questionNums[p]);
            }
            
            Arrays.sort(wordList);
            
            for (int p=0; p<wordList.length; p++)
                System.out.println(wordList[p] + " p: " + p);
            System.out.println();
            
            return wordList;
	}
        
        public void getLetters()    {
            int j, totalLetters = 0;
            char ch;
            String answer;
            for (int i=0; i<questionNums.length; i++)   {
                j = 0;
                if (questionNums[i] != -1)  {
                    answer = answers.get(questionNums[i]);
                    totalLetters += answer.length();
                    while (j < answer.length())   {
                        ch = answer.charAt(j);
                        insertToInAns(ch);
                        j++;
                    }
                }
            }
            
            //System.out.println("Total Letters: " + totalLetters);
            
            //Calculate percentage
            for (j=0; j<inAns.length; j++)
                inAns[j].set_percentage(totalLetters);
            
            //Distribute weight
            totalWeight = distributeWeight();
                        
            //Display letters in answers
            System.out.println();
            for (j=0; j<inAns.length; j++)
                System.out.print(inAns[j] + " ");
            System.out.println();
        }
        
        public int distributeWeight()  {
            int cumWt = 0, weight;
            for (int i=0; i<inAns.length; i++)  {
                if (inAns[i].get_letter() == '&') break;
                weight = cumWt + (int)inAns[i].get_percentage();
                inAns[i].set_weight(cumWt, weight);
                cumWt = weight+1;
            }
            return cumWt;
        }
        
        public void insertToInAns(char ch)   {
            int i=0;
            while (inAns[i].get_letter() != '&')  {
                if (ch == inAns[i].get_letter())    {
                    //inAns[i].set_letter(ch);
                    inAns[i].inc_freq();
                    return;
                }
                i++;
            }
            inAns[i].set_letter(ch);
            inAns[i].inc_freq();   
        }
        
        public void towerAnswers()  {                       
            
            for (int k=0; k<towers.length; k++) {
                if (k == 0)
                    towers[k].fillIn(-1, questionNums, answers, 0);
                else
                    towers[k].fillIn(towers[k-1].get_qNum(), questionNums, answers, towers[k-1].get_position());
            }
            
            /*System.out.println("Tower properties");
            for (int p=0; p<towers.length; p++)
            	System.out.println(towers[p]);*/
        }
        
        public void getPreX()   {
            for (int i=0; i<preX.length; i++)   {
                preX[i].fillIn();
            }
        }
        
        public void fillGrid()  {
            for (int i=0; i<towers.length; i++) {
                for (int j=7, k=0; j>=(8-towers[i].get_numOfDisplayLetters()); j--, k++)   {
                    grid[j][towers[i].get_position()] = towers[i].get_displayLetters().charAt(k);                    
                }
            }
            
            for (int i = 0; i < preX.length; i++) {
                grid[preX[i].get_row()][preX[i].get_col()] = preX[i].get_letter();
            }                
        }
        
        public void displayGrid()   {
            for (int i=0; i<grid[0].length; i++) {
                for (int j=0; j<grid[0].length; j++)
                    System.out.print(grid[i][j] + " ");
                System.out.println();
            }
        }
        
        public int getRatioValue() {
            Random r = new Random();
            int decideArray = r.nextInt(100);
            return decideArray;
        }
	
        public char generateLetter(int ratioValue)  {
            Random r = new Random();
            int priorityChoice;            
            int priorityLength;
            char ch;
            
            //Variable to choose between priorityList and list of letters
            priorityChoice = r.nextInt(100);
            
            /*System.out.print("Priority list : ");
            for (int q=0; q<priorityList.length; q++)
            	System.out.print(priorityList[q] + " ");
            System.out.println();*/
            
            // If priority list is not empty
            if (priorityList[0] != '*')          {
                
                //Choose a letter from the priorityList
                if (priorityChoice < PRIORITY_RATIO)    {
                    priorityLength = getPriorityListLength();
                    System.out.println("priorityLength = " + priorityLength);
                    ch = priorityList[r.nextInt(priorityLength)];
                    return ch;
                }
                
                //Choose a letter from list of letters or all letters
                    return nonPriorityLetter(ratioValue);
            }
            
            //If priority list is empty, choose between letter list and all letters
            else 
                return nonPriorityLetter(ratioValue);
        }
        
        public char nonPriorityLetter(int ratioValue)   {
            
            int weight, ascii;
            Random r = new Random();
            if (ratioValue < RATIO) {
                weight = r.nextInt(totalWeight);
                //System.out.println("weight = " + weight);
                return getLetterByWeight(weight);
            }
            else {
                ascii = r.nextInt(26) + 65;
                return (char)ascii;
            }
        }
        
        public char getLetterByWeight(int weight)   {
            for (int i = 0; i < inAns.length; i++)  {
                if (weight >= inAns[i].get_lower() && weight <= inAns[i].get_upper())
                    return inAns[i].get_letter();
            }
            return '&';
        }
        
        public void clearPriorityList() {
        	System.out.println("Clearing priority list");
            for (int i=0; i<priorityList.length; i++)
                priorityList[i] = '*';
        }
        
        public int getPriorityListLength()  {
            for (int i=0; i<priorityList.length; i++)   {
                if (priorityList[i] == '*')
                    return i;
            }
            return 0;
        }
        
        public void resetRandomizerArrays(String foundWord)	{
        	int i;
        	
        	//Assumption - no two questions have the same answer
        	
        	//Replacing the found word with * and its corresponding question number with -1
        	for (i=0; i<wordList.length; i++)	{
        		if (wordList[i].equalsIgnoreCase(foundWord) == true)	{
        			wordList[i] = "*";        			
        		}
        		if (questionNums[i] != -1 && answers.get(questionNums[i]).equalsIgnoreCase(foundWord) == true)	{
        			questionNums[i] = -1;
        		}
        	}
        	
        	Arrays.sort(wordList);
        	getLetters();
        }
        
  /*      
	public static void main(String[] args)	{
		Randomizer mainObj = new Randomizer();
                Find findObj = new Find();
		//select questions
		mainObj.selectQs();
                mainObj.towerAnswers();
                mainObj.getPreX();
                mainObj.fillGrid();
                mainObj.displayGrid();
                mainObj.getLetters();
                
                int n = 0, ratioValue;
                char theLetter;
                Scanner sc = new Scanner(System.in);
                int entry = 1;
                while (entry != 0)    {  
                    mainObj.clearPriorityList();
                    findObj.find();
                    ratioValue = mainObj.getRatioValue();
                    theLetter = mainObj.generateLetter(ratioValue);
                    System.out.println("theLetter: " + theLetter);
                    entry = sc.nextInt();
                }
	}
	*/
}
