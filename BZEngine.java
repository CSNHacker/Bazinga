package com.entropy.bazzinga;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class BZEngine {
	/*Constants that will be used in the game*/
	public static final int GAME_THREAD_DELAY = 4000;
	public static final int MENU_BUTTON_ALPHA = 0;
	public static final boolean HAPTIC_BUTTON_FEEDBACK = true;
	public static final int SPLASH_SCREEN_MUSIC = R.raw.background;
	public static final int SUCCESS_MUSIC = R.raw.success;
	public static final int R_VOLUME = 500;
	public static final int L_VOLUME = 500;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;
	public static Context context;
	public static Thread musicThread;

	/*Kill game and exit*/
	
	public boolean onExit(View v) {
        try
        {
        	Intent bgmusic = new Intent(context, BZMusic.class);
        	context.stopService(bgmusic);
        	musicThread = null;
        	return true;
        }catch(Exception e){
        	return false;
        }
     		
	}
		 
}
