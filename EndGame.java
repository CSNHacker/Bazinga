package com.entropy.bazzinga;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EndGame extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_activity);

        int score = BazzingaActivity.Num_ans_found *10;
        
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        long prefName = myPrefs.getLong("Score",0);
        if(myPrefs!=null && prefName < score){
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putLong("Score", score);
        prefsEditor.commit();
        
        }
        TextView score1 = (TextView) findViewById(R.id.score);
        score1.setText("Your score is : "+ score);
        
        TextView q1 = (TextView) findViewById(R.id.qu1);
        q1.setText(Randomizer.wordList[0]);
        
        TextView q2 = (TextView) findViewById(R.id.qu2);
        q2.setText(Randomizer.wordList[1]);
        
        TextView q3 = (TextView) findViewById(R.id.qu3);
        q3.setText(Randomizer.wordList[2]);
        
        TextView a1 = (TextView) findViewById(R.id.an1);
        a1.setText(Randomizer.ansList[0]);
        
        TextView a2 = (TextView) findViewById(R.id.an2);
        a2.setText(Randomizer.ansList[1]);
        
        TextView a3 = (TextView) findViewById(R.id.an3);
        a3.setText(Randomizer.ansList[2]);
        
        ImageView img1 = (ImageView) findViewById(R.id.cor1);
        ImageView img2 = (ImageView) findViewById(R.id.cor2);
        ImageView img3 = (ImageView) findViewById(R.id.cor3);
        
        if(BazzingaActivity.found !=null){
        	for(int i=0;i<=BazzingaActivity.Num_ans_found;i++){
        		if(a1.getText().equals(BazzingaActivity.found[i])){
        			img1.setImageResource(R.drawable.correct);
        		}
        		else if(a2.getText().equals(BazzingaActivity.found[i])){
        			img2.setImageResource(R.drawable.correct);
        		}
        		else if(a3.getText().equals(BazzingaActivity.found[i])){
        			img3.setImageResource(R.drawable.correct);
        		}
        	}
        }
        
        Button menu = (Button) findViewById(R.id.menu);
        menu.setText("Go to Menu");
        menu.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent main = new Intent (getApplicationContext(), MainActivity.class);
				startActivity(main);
			}
        });
}
}