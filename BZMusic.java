
package com.entropy.bazzinga;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class BZMusic extends Service{
	   public static boolean isRunning = false;
	   static MediaPlayer player;
	   //private static Context context; 
	   @Override
	   public IBinder onBind(Intent arg0) {          
		   return null;     
		   }     
	   @Override     
	   public void onCreate() {         
		   super.onCreate();   
		   player = MediaPlayer.create(this, R.raw.background);
	        player.setLooping(true); // Set looping
	        player.setVolume(500,500);
	       
		//  setMusicOptions(this,BZEngine.LOOP_BACKGROUND_MUSIC,BZEngine.R_VOLUME,BZEngine.L_VOLUME,BZEngine.SPLASH_SCREEN_MUSIC);
		   }
	   public void setMusicOptions(Context context, boolean isLooped, int rVolume, int lVolume, int soundFile){
		   player = MediaPlayer.create(context, soundFile);         
		   player.setLooping(isLooped);          
		   player.setVolume(rVolume,lVolume);
	   }
	   public int onStartCommand(Intent intent, int flags, int startId) {           
		   try
		   {
			   Log.i("ON START COMMAND", "Huston, we have a lift off!");
			   player.start(); 
			   isRunning = true;
		   }catch(Exception e){
			   isRunning = false;
			   player.stop();
		   }

		   return 1;     
//		   player.start();
//	        return 1;
	   }      
	   
	 
	   public void onStart(Intent intent, int startId) {         
		   // TODO   
		   
		   }    
	   public IBinder onUnBind(Intent arg0) {         
				return null;     
		}      
		public void onStop() {
				isRunning = false;
		}    
		
		public void onPause() { //ContinueMusic 
		}   
		
	     
		public void onDestroy() {          
				player.stop();         
				player.release();
		}      
		@Override     
		public void onLowMemory() {
				player.stop();
		}
		public static void onMute(){
				isRunning = false;
		}
		
		public void onSuccess(){//Dunno whether or not create new thread- You could fire it up as did in MainMenu
				super.onCreate();           
				setMusicOptions(this,false,BZEngine.R_VOLUME,BZEngine.L_VOLUME,BZEngine.SUCCESS_MUSIC);
		}
		
}

