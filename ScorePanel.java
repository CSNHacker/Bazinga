package com.entropy.bazzinga;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ScorePanel  {
	
	private boolean[] isFound = new boolean[5];
	

	QuestionDB qdb = new QuestionDB(1,"Name of 1st Planet", "Mercury");
	QuestionDB qdb1 = new QuestionDB(2,"Name of 2nd Planet", "Venus");
	QuestionDB qdb2 = new QuestionDB(3,"Name of 3rd Planet","Earth");
	QuestionDB qdb3 = new QuestionDB(4,"Name of 4th Planet", "Mars");	
	QuestionDB qdb4 = new QuestionDB(1,"Name of 5th Planet", "Jupiter");	
	
	public ScorePanel(DatabaseHandler db){
		db.addQuestion(qdb);
		db.addQuestion(qdb1);
		db.addQuestion(qdb2);
		db.addQuestion(qdb3);
		db.addQuestion(qdb4);
		for(int i=0;i<5;i++){
			isFound[i]=false;
		}
	}
	
	

}
