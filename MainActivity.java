package com.entropy.bazzinga;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Resources;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	//DatabaseHandler myDB = new DatabaseHandler(this.getApplicationContext());
	BazzingaActivity b = new BazzingaActivity();
	 public static MediaPlayer mp = null;
	
	 @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        Display display = getWindowManager().getDefaultDisplay(); 
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			int window_width= width;  
			int window_height = height; 
			
	        setContentView(R.layout.main_activity);
	        
	        LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
	        
	        ImageView img = (ImageView) findViewById(R.id.logo);
	        ImageView img1 = (ImageView) findViewById(R.id.new_game);
	        ImageView img2 = (ImageView) findViewById(R.id.load_game);
	        ImageView img3 = (ImageView) findViewById(R.id.high_score);
	        ImageView img4 = (ImageView) findViewById(R.id.instruction);
	        ImageView img6 = (ImageView) findViewById(R.id.quit);
	        final ToggleButton img5 = (ToggleButton) findViewById(R.id.sound);
	        
	         img5.setOnClickListener(new OnClickListener(){
	        	 
	        	 @Override
	        	 public void onClick(View v){
	        		 
	        		 if ( !img5.isSelected() && mp == null) {
	                     mp = MediaPlayer.create(getApplicationContext(), R.raw.background);
	                     mp.setLooping(true);
	                     mp.start();
	                 } else {
	                     mp.stop();
	                     mp = null;
	                 }
	        	 }
	        	 });

			
	        LinearLayout.LayoutParams vp0 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		       vp0.topMargin= (int) (window_height*0.01);
		      // System.out.println(window_height *0.02);
		       vp0.leftMargin = (int) (window_width/4 - 100 );
		       img.setLayoutParams(vp0);
		       
	       LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	       vp.topMargin= (int) (window_height*0.015);
	      // System.out.println(window_height *0.02);
	       vp.leftMargin = (int) (window_width/4 - img1.getWidth());
	       img1.setLayoutParams(vp);
	        
	        LinearLayout.LayoutParams vp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        vp1.topMargin= (int) (window_height*0.02);
	      //  System.out.println(window_height *0.04);
		       vp1.leftMargin = (int) (window_width/4- img2.getWidth());
		       img2.setLayoutParams(vp1);
     
	        LinearLayout.LayoutParams vp2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        vp2.topMargin= (int) (window_height*0.025);
	        //System.out.println(window_height *0.06);
		       vp2.leftMargin = (int) (window_width/4 - img3.getWidth());
		       img3.setLayoutParams(vp2);
	        
	        LinearLayout.LayoutParams vp3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        vp3.topMargin= (int) (window_height*0.030);
	     //   System.out.println(window_height *0.08);
		       vp3.leftMargin = (int) (window_width/4 - img4.getWidth());
		       img4.setLayoutParams(vp3);
	        
		       LinearLayout.LayoutParams vp4 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		        vp4.topMargin= (int) (window_height*0.01);
		     //   System.out.println(window_height *0.10);
			       vp4.leftMargin = (int) (window_width*0.75);
			       img5.setLayoutParams(vp4);
		      
			       LinearLayout.LayoutParams vp5 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			        vp5.topMargin= (int) (window_height*0.035);
			     //   System.out.println(window_height *0.10);
				       vp5.leftMargin = (int) (window_width/4 - img6.getWidth());
				       img6.setLayoutParams(vp5);
				       
				       img6.setOnClickListener( new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							 android.os.Process.killProcess(android.os.Process.myPid());
							 finish();
							
						}
				    	   
				       });
				       final SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
				       img3.setOnClickListener( new OnClickListener(){

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								
						        if(myPrefs!=null){
								long prefName = myPrefs.getLong("Score", 0);					    
								Toast.makeText(getApplicationContext(), "Your Highest Score is "+ prefName, Toast.LENGTH_SHORT).show();
						        }
						        else{
						        	Toast.makeText(getApplicationContext(), "No High Score Recorded!", Toast.LENGTH_SHORT).show();
						        }
							}

							
					    	   
					       });
	        DatabaseHandler db = new DatabaseHandler(getApplicationContext());	    
	   	 	ScorePanel sp = new ScorePanel(db);
	
	 }
	  public void New(View view) {
	        Intent i = new Intent(this, BazzingaActivity.class);
	        startActivity(i);
	    }
	 
	 
//	 public void Sound(View view){
//		 Resources r = getResources();
//		 int imgId = this.getResources().getIdentifier("unmute.png", "drawable", "com.mypackage.namehere");
//		// int imgId = r.getIdentifier("unmute", "drawable", "com.entropy.bazzinga");
//		 System.out.println(imgId);
//		 ImageView image = (ImageView)findViewById(R.id.sound);
//		 image.setBackgroundResource(imgId);
//	 }
//	  
//	  public void Resume(View view){
//		  b.onResumeGame();
//	  }
}