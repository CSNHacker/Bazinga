package com.entropy.bazzinga;

public class QuestionDB {
	 
    //private variables
    int id;
    String question;
    String answer;
 
    // Empty constructor
    public QuestionDB(){
 
    }
    // constructor
    public QuestionDB(int id, String question, String answer){
        this.id = id;
        this.question = question;
        this.answer = answer;
    }
 
    // constructor
    public QuestionDB(String question, String answer){
        this.question = answer;
        this.answer = answer;
    }
    // getting ID
    public int getID(){
        return this.id;
    }
 
    // setting id
    public void setID(int id){
        this.id = id;
    }
 
    // getting name
    public String getQuestion(){
        return this.question;
    }
 
    // setting name
    public void setQuestion(String question){
        this.question = question;
    }
 
    // getting phone number
    public String getAnswer(){
        return this.answer;
    }
 
    // setting phone number
    public void setAnswer(String answer){
        this.answer = answer;
    }
}

