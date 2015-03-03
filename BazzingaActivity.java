package com.entropy.bazzinga;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;


public class BazzingaActivity extends SimpleBaseGameActivity {
	
	public MediaPlayer mp = null;
	protected static String found_answer = null;
	protected static String[] found; 
	protected static int Num_ans_found =0;
	//camera
	private int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 480;
	private static String FILENAME = "saveGameData";
	
	//static variables
	protected static int panel_H = 600, panel_W = 75; /*Must match column.png*/
	protected static int panel_Y = 10, panel_X = 20;
	protected static int block_H = 75, block_W = 75; /*Must match A-Z.png*/
	protected static int numCol = 8, numRow = panel_H / block_H + 1; //numRow should be 8 

	//textures
	private ITextureRegion mBackgroundTextureRegion, mColumnTextureRegion, mVertWallTextureRegion, mHorWallTextureRegion;
	private TiledTextureRegion mA, mB, mC, mD, mE, mF, mG, mH, mI, mJ, mK, mL, mM, mN, mO, mP, mQ, mR, mS, mT, mU, mV, mW, mX, mY,mZ;
	private BitmapTextureAtlas bA, bB, bC, bD, bE, bF, bG, bH, bI, bJ, bK, bL, bM, bN, bO, bP, bQ, bR, bS, bT, bU, bV, bW, bX, bY,bZ;
	
	//sprites
	private Sprite[] colArr;
	private Sprite mBlock1;
	protected Sprite leftWall, rightWall, topWall, botWall;
	protected Scene scene;
	
	//stores
	Player falling;
	protected char[][] blockStore;
	protected Player[] blocks;
	protected int whichBlock;
	private String[] wordList = {"ALE", "TALE", "ATE"};

	private static char[] test1 = {'A', 'L', 'M', 'E', 'L', 'A', 'T', 'Z', 'Y', 'K'};
	//private static char[] test1 = {'P', 'L', 'M', 'V', 'L', 'D', 'T', 'Z', 'Y', 'K', 'H'};
	private int test2;
	private boolean first;
	public static Randomizer r;
	
	//testing stuffffff
	protected Text[] texty; protected Text texty1, texty2, texty3, q1, q2,q3;
	protected Font mFont;
	
	@SuppressLint("NewApi")
	@Override
	public EngineOptions onCreateEngineOptions() {
		//Display display = getWindowManager().getDefaultDisplay(); 
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		CAMERA_WIDTH = width;  // deprecated
		CAMERA_HEIGHT = height;  
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
		    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}
	 
	@Override
	protected void onCreateResources() {
		try {
			
		    // 1 - Set up bitmap textures
		    ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/background.png");
		        }
		    });
		    ITexture columnTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/column.png");
		        }
		    });
		    ITexture vertWallTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/vertWall.png");
		        }
		    });
		    ITexture horWallTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/horWall.png");
		        }
		    });
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    columnTexture.load();
		    vertWallTexture.load();
		    horWallTexture.load();
		    // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    this.mColumnTextureRegion = TextureRegionFactory.extractFromTexture(columnTexture);
		    this.mVertWallTextureRegion = TextureRegionFactory.extractFromTexture(vertWallTexture);
		    this.mHorWallTextureRegion = TextureRegionFactory.extractFromTexture(horWallTexture);
		    //setup blocks
		    //A
		    this.bA = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mA = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bA, this, "gfx/A.png", 0, 0, 1, 1);
		    this.bA.load();
		    //B
		    this.bB = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mB = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bB, this, "gfx/B.png", 0, 0, 1, 1);
		    this.bB.load();
		  	//C
		    this.bC = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mC = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bC, this, "gfx/C.png", 0, 0, 1, 1);
		    this.bC.load();
		    //D
		    this.bD = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mD = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bD, this, "gfx/D.png", 0, 0, 1, 1);
		    this.bD.load();
		  //E
		    this.bE = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mE = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bE, this, "gfx/E.png", 0, 0, 1, 1);
		    this.bE.load();
		  //A
		    this.bF = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mF = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bF, this, "gfx/F.png", 0, 0, 1, 1);
		    this.bF.load();
		  //G
		    this.bG = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mG = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bG, this, "gfx/G.png", 0, 0, 1, 1);
		    this.bG.load();
		  //H
		    this.bH = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mH = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bH, this, "gfx/H.png", 0, 0, 1, 1);
		    this.bH.load();
		  //I
		    this.bI = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mI = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bI, this, "gfx/I.png", 0, 0, 1, 1);
		    this.bI.load();
		  //J
		    this.bJ = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mJ = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bJ, this, "gfx/J.png", 0, 0, 1, 1);
		    this.bJ.load();
		  //K
		    this.bK = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mK = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bK, this, "gfx/K.png", 0, 0, 1, 1);
		    this.bK.load();
		  //L
		    this.bL = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mL = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bL, this, "gfx/L.png", 0, 0, 1, 1);
		    this.bL.load();
		  //M
		    this.bM = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mM = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bM, this, "gfx/M.png", 0, 0, 1, 1);
		    this.bM.load();
		  //N
		    this.bN = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mN = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bN, this, "gfx/N.png", 0, 0, 1, 1);
		    this.bN.load();
		  //O
		    this.bO = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mO = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bO, this, "gfx/O.png", 0, 0, 1, 1);
		    this.bO.load();
		  //P
		    this.bP = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mP = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bP, this, "gfx/P.png", 0, 0, 1, 1);
		    this.bP.load();
		  //Q
		    this.bQ = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mQ = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bQ, this, "gfx/Q.png", 0, 0, 1, 1);
		    this.bQ.load();
		  //R
		    this.bR = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bR, this, "gfx/R.png", 0, 0, 1, 1);
		    this.bR.load();
		  //S
		    this.bS = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mS = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bS, this, "gfx/S.png", 0, 0, 1, 1);
		    this.bS.load();
		  //T
		    this.bT = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mT = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bT, this, "gfx/T.png", 0, 0, 1, 1);
		    this.bT.load();
		  //U
		    this.bU = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mU = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bU, this, "gfx/U.png", 0, 0, 1, 1);
		    this.bU.load();
		  //V
		    this.bV = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mV = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bV, this, "gfx/V.png", 0, 0, 1, 1);
		    this.bV.load();
		  //W
		    this.bW = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mW = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bW, this, "gfx/W.png", 0, 0, 1, 1);
		    this.bW.load();
		  //X
		    this.bX = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mX = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bX, this, "gfx/X.png", 0, 0, 1, 1);
		    this.bX.load();
		  //Y
		    this.bY = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mY = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bY, this, "gfx/Y.png", 0, 0, 1, 1);
		    this.bY.load();
		  //Z
		    this.bZ = new BitmapTextureAtlas(this.getTextureManager(), 75, 75);
		    this.mZ = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.bZ, this, "gfx/Z.png", 0, 0, 1, 1);
		    this.bZ.load();
		} catch (IOException e) {
		    Debug.e(e);
		}
	}
	@TargetApi(11)
	@Override
	protected Scene onCreateScene() {
		//File file = new File(FILENAME);
	    //we load it then, only if it exists
	  //  if (file.exists()) {
	   // 	loadGame();
	  //  }
	  //  else{
		//Create new scene
		scene = new Scene();
		//Initialize variables
		init();
		//Draw Background
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(backgroundSprite);
		//Add gamePanel Columns
		colArr = new Sprite[numCol];
		for(int i=0 ; i<colArr.length ; i++) {
			colArr[i] = new Sprite((i*75)+panel_X, panel_Y, this.mColumnTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(colArr[i]);
		}
		//Add walls
		leftWall = new Sprite(panel_X-5, panel_Y, this.mVertWallTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(leftWall);
		rightWall = new Sprite(panel_X + (panel_W*numCol), panel_Y, this.mVertWallTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(rightWall);
		topWall = new Sprite(panel_X, panel_Y-5, this.mHorWallTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(topWall);
		botWall = new Sprite(panel_X, panel_Y + panel_H, this.mHorWallTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(botWall);
		
		mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20);
		
		mFont.load();
		
		texty = new Text[8];
		
		for(int i=0 ; i<8 ; i++) {
			texty[i] = new Text(30, 30*i+30, mFont, "JinjoJinjoJinjoJinjoJinjoJinjoJinjo", new TextOptions(), this.getVertexBufferObjectManager());
			scene.attachChild(texty[i]);
		}
		
		texty1 = new Text(30, 30*2+30, mFont, "Halley", new TextOptions(), this.getVertexBufferObjectManager());
		scene.attachChild(texty1);
		texty2 = new Text(200, 30*10+30, mFont, "Halley", new TextOptions(), this.getVertexBufferObjectManager());
		scene.attachChild(texty2);
		texty3 = new Text(30, 30*11+30, mFont, "Halley", new TextOptions(), this.getVertexBufferObjectManager());
		scene.attachChild(texty3);
		
		
		//newBlock(col, pos_x, pos_H)
		
		//Get Preset Blocks
		r = new Randomizer();
		r.clearPriorityList();
//		Collection<String> questions;
//		questions = r.questions.values();
//		Object[] questions1 = questions.toArray();
//		q1.setText((CharSequence) questions1[0]);
//		q2.setText((CharSequence) questions1[1]);
//		q3.setText((CharSequence) questions1[2]);
		
		Set set = r.questions.entrySet(); 
		// Get an iterator 
		Iterator k = set.iterator(); 
		// Display elements 
		while(k.hasNext()) { 
		Map.Entry me = (Map.Entry)k.next(); 
		System.out.print(me.getKey() + ": "); 
		System.out.println(me.getValue()); 
		} 
		
		//Selected 10 questions - from database
		String[] questions = r.selectQs();
		
		System.out.println(questions[0]);
		System.out.println(questions[1]);
		System.out.println(questions[2]);
		
//		q1.setText(questions[0]);
//		q2.setText(questions[1]);
//		q3.setText(questions[2]);
		
		q1 = new Text(200, 50, mFont, questions[0], new TextOptions(), this.getVertexBufferObjectManager());
		q1.setColor(Color.WHITE);
		scene.attachChild(q1);
		q2 = new Text(200, 100, mFont, questions[1], new TextOptions(), this.getVertexBufferObjectManager());
		q2.setColor(Color.WHITE);
		scene.attachChild(q2);
		q3 = new Text(200, 150, mFont, questions[2], new TextOptions(), this.getVertexBufferObjectManager());
		q3.setColor(Color.WHITE);
		scene.attachChild(q3);
		// Note - 2 blocks and 2 towers.
		//Pre exising towers that are going to be there
		r.towerAnswers();
		
		// For the random blocks that are present initially (not from answers)
		r.getPreX();
		
		// Fills in the towers and random blocks
		r.fillGrid();
		
		// extracts letters from answers and does the weight distribution
		r.getLetters();
		
		//int col1 = 3;
		int col1 = r.towers[0].get_position();
		String tower1 = r.towers[0].get_displayLetters();
		for(int i=0 ; i<tower1.length() ; i++) {
			char x = tower1.charAt(i);
			newBlock(col1, 20+block_W*col1, panel_X+(7-i)*block_H-10, x);
			blockStore[7-i][col1] = x;
			blocks[whichBlock].set_endRow(7-i);
		}
		int col2 = r.towers[1].get_position();
		String tower2 = r.towers[1].get_displayLetters();
		for(int i=0 ; i<tower2.length() ; i++) {
			char x = tower2.charAt(i);
			newBlock(col2, 20+block_W*col2, panel_X+(7-i)*block_H-10, x);
			blockStore[7-i][col2] = x;
			blocks[whichBlock].set_endRow(7-i);
		}
		
		newBlock(r.preX[0].get_col(), 20+block_W*r.preX[0].get_col(), panel_X+(r.preX[0].get_row())*block_H-10, r.preX[0].get_letter());
		blockStore[r.preX[0].get_row()][r.preX[0].get_col()] = r.preX[0].get_letter();
		blocks[whichBlock].set_endRow(r.preX[0].get_row());
		newBlock(r.preX[1].get_col(), 20+block_W*r.preX[1].get_col(), panel_X+(r.preX[1].get_row())*block_H-10, r.preX[1].get_letter());
		blockStore[r.preX[1].get_row()][r.preX[1].get_col()] = r.preX[1].get_letter();
		blocks[whichBlock].set_endRow(r.preX[0].get_row());
		draw();
		
		
		//New Block
		newBlock();
		
		return scene;
	    }
	 //   return null;
	//}
	
//	private void newGame() {
//		init();
//		newBlock();
//		}
//	
//		private void loadGame() {
//		FileInputStream fIn = null;
//		InputStreamReader isr = null;
//		String data = null;
//		try{
//		char[] inputBuffer = new char[1024];
//		fIn = openFileInput(FILENAME);
//		isr = new InputStreamReader(fIn);
//		isr.read(inputBuffer);
//		data = new String(inputBuffer);
//		isr.close();
//		fIn.close();
//		}
//		catch(IOException e){
//		e.printStackTrace(System.err);
//		}
//		//set blockStore
//		int count = 0;
//		char[] arr = data.toCharArray();
//		for(int i=0 ; i<8 ; i++) { //row
//		for(int j=0 ; j<8 ; j++) { //col
//		blockStore[i][j] = arr[count];
//		count++;
//		}
//		}
//		draw();
//		for(int i=0 ; i<8 ; i++) { //row
//		for(int j=0 ; j<8 ; j++) { //col
//		newBlock(j, 20+block_W*j, panel_X+(i)*block_H-10, blockStore[i][j]);
//		blocks[whichBlock].set_endRow(i);
//		}
//		}
//		char c = arr[65];
//		newBlock(c);
//
//		}
//		
//		private void saveGame() {
//			//create string for blockStore
//			String bs = "";
//			for(int i=0 ; i<8 ; i++) { //row
//				for(int j=0 ; j<8 ; j++) { //col
//					if(blockStore[i][j] == ' ') {
//						bs += '*';
//					}
//					else {
//						bs += blockStore[i][j];
//					}
//				}
//			}
//			bs += "/"; 
//			//create string for current block
//			String b = "";
//			b += blocks[whichBlock].value; 
//			//combine strings
//			String w = bs + b;
//			FileOutputStream fOut = null;
//			OutputStreamWriter osw = null;
//			try{
//				fOut = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//				osw = new OutputStreamWriter(fOut);
//				osw.write(w);
//				osw.close();
//				fOut.close();
//			}
//			catch(Exception e){
//				e.printStackTrace(System.err);
//			}
//			}
		
		
		public void newBlock(char c) {
			whichBlock++;
			texty1.setText(Integer.toString(whichBlock));
			texty2.setText("new");
			if(whichBlock != 0) {
				scene.unregisterTouchArea(blocks[whichBlock-1]);
				blocks[whichBlock-1].moving = false; //MAKE IT STOP
			}
			
			if(!first) {
				if(blocks[whichBlock-1].endRow == 1) {
					texty1.setText("End!");
					mEngine.stop();
				}
				
			blockStore[blocks[whichBlock-1].endRow][blocks[whichBlock-1].col] = blocks[whichBlock-1].value;
			find();
			}

			char value = c;
			TiledTextureRegion mBlock = getTexture(value);
			blocks[whichBlock] = new Player(245, 20, mBlock, this.getVertexBufferObjectManager(), this, value);
			scene.attachChild(blocks[whichBlock]);
			scene.registerTouchArea(blocks[whichBlock]);
			first = false;
			//saveGame();
			
		}
		
    private char seedBlocks() {
    	
    	int ratioValue = r.getRatioValue();
		char theLetter = r.generateLetter(ratioValue);
		System.out.println("THE LETTER : " + theLetter);
		return theLetter;
		
    	//return test1[test2++];
	}
    public void newBlock() {
		whichBlock++;
		texty1.setText(Integer.toString(whichBlock));
		texty2.setText("new");
		if(whichBlock != 0) {
			scene.unregisterTouchArea(blocks[whichBlock-1]);
			blocks[whichBlock-1].moving = false; //MAKE IT STOP
		}
		if(!first) {
			if(blocks[whichBlock-1].endRow == 1) {
				texty1.setText("End!");
				mEngine.stop();
			}
			blockStore[blocks[whichBlock-1].endRow][blocks[whichBlock-1].col] = blocks[whichBlock-1].value;
			find();
		//	saveGame();
		}
		
		char value = seedBlocks();
		TiledTextureRegion mBlock = getTexture(value);
		blocks[whichBlock] = new Player(245, 20, mBlock, this.getVertexBufferObjectManager(), this, value);
		scene.attachChild(blocks[whichBlock]);
		scene.registerTouchArea(blocks[whichBlock]);
		first = false;
	}
	public void newBlock(int r, int x, int y, char c) { //to force in place
		whichBlock++;
		if(whichBlock != 0) {
			texty1.setText(Integer.toString(whichBlock));
			scene.unregisterTouchArea(blocks[whichBlock-1]);
			blocks[whichBlock-1].moving = false; //MAKE IT STOP
		}
		char value = c;
		TiledTextureRegion mBlock = getTexture(value);
		blocks[whichBlock] = new Player(r, x, y, mBlock, this.getVertexBufferObjectManager(), this, value);
		scene.attachChild(blocks[whichBlock]);
		scene.registerTouchArea(blocks[whichBlock]);
		
	}
	/*public void newBlock() {
		whichBlock++;
		if(whichBlock != 0) {
			scene.unregisterTouchArea(blocks[whichBlock-1]);
			blocks[whichBlock-1].moving = false; //MAKE IT STOP
		}
		if(!first) {
			if(blocks[whichBlock-1].endRow == 1) {
				texty1.setText("End!");
				mEngine.stop();
			}
			blockStore[blocks[whichBlock-1].endRow][blocks[whichBlock-1].col] = blocks[whichBlock-1].value;
			r.clearPriorityList();
			find();
		}
		
		char value = seedBlocks();
		TiledTextureRegion mBlock = getTexture(value);
		blocks[whichBlock] = new Player(245, 20, mBlock, this.getVertexBufferObjectManager(), this, value);
		scene.attachChild(blocks[whichBlock]);
		scene.registerTouchArea(blocks[whichBlock]);
		first = false;
	}
	public void newBlock(int r, int x, int y, char c) { //to force in place		
		whichBlock++;
		if(whichBlock != 0) {
			//texty1.setText(Integer.toString(whichBlock));
			scene.unregisterTouchArea(blocks[whichBlock-1]);
			blocks[whichBlock-1].moving = false; //MAKE IT STOP
		}
		char value = c;
		TiledTextureRegion mBlock = getTexture(value);
		blocks[whichBlock] = new Player(r, x, y, mBlock, this.getVertexBufferObjectManager(), this, value);
		scene.attachChild(blocks[whichBlock]);
		scene.registerTouchArea(blocks[whichBlock]);
		
	}*/
	private TiledTextureRegion getTexture(char val) {
		switch(val) {
			case 'A': return mA;
			case 'B': return mB;
			case 'C': return mC;
			case 'D': return mD;
			case 'E': return mE;
			case 'F': return mF;
			case 'G': return mG;
			case 'H': return mH;
			case 'I': return mI;
			case 'J': return mJ;
			case 'K': return mK;
			case 'L': return mL;
			case 'M': return mM;
			case 'N': return mN;
			case 'O': return mO;
			case 'P': return mP;
			case 'Q': return mQ;
			case 'R': return mR;
			case 'S': return mS;
			case 'T': return mT;
			case 'U': return mU;
			case 'V': return mV;
			case 'W': return mW;
			case 'X': return mX;
			case 'Y': return mY;
			case 'Z': return mZ;
			default: return mD;
		}
	}
	private void init() {
		first = true;
    	//blockStore
    	blockStore = new char[BazzingaActivity.numRow][BazzingaActivity.numCol];	
    	for(int i=0 ; i<BazzingaActivity.numRow ; i++) {
			for(int j=0 ; j<BazzingaActivity.numCol ; j++) {
				blockStore[i][j] = ' ';
				if(1 == BazzingaActivity.numRow-1) {
					blockStore[i][j] = '*';
				}
			}
		}
    	//blockHold
    	blocks = new Player[64];
    	whichBlock = -1;
    }
	/*---- CHIRAG FIND FUNCTIONS ----- */
	public int find() {
		texty2.setText("Look");
		draw();
		
        int matches = 0;

        for( int r = 0; r < numRow; r++ )
            for( int c = 0; c < numCol; c++ )
                for( int rd = -1; rd <= 1; rd++ )//Generates all 8 direction cosines.
                    for( int cd = -1; cd <= 1; cd++ )//Generates all 8 direction cosines.
                        if( rd != 0 || cd != 0 )//The origin is skipped{
                            matches += solveDirection( r, c, rd, cd );
                            
                        
        return matches;
    }
	
	private int solveDirection( int baseRow, int baseCol, int rowDirection, int colDirection )
    {
        String charSequence = "";
        int numMatches = 0;
        int searchResult;

        charSequence += blockStore[ baseRow ][ baseCol ];//Starts from one position on blockStore

        for( int i = baseRow + rowDirection, j = baseCol + colDirection;
                 i >= 0 && j >= 0 && i < 8 && j < 8;//Boundary conditions so that search doesn't go out of bounds
                 i += rowDirection, j += colDirection )
        {
            charSequence += blockStore[ i ][ j ];
            searchResult = prefixSearch( Randomizer.wordList, charSequence );

            if( searchResult == Randomizer.wordList.length )//If the last word in wordList array doesn't contain charSequence then break out of loop
                break;

            if( !((String)Randomizer.wordList[ searchResult ]).startsWith( charSequence ) ) //If word in theWord doesn't start with charSequence then no use looping further break.
            {
                if (charSequence.length() >= 2 && charSequence.charAt(0) != ' ') {
                    //System.out.println("'" + charSequence + "'");
                    int starPosn = charSequence.indexOf(' ');
                    if (charSequence.charAt(charSequence.length()-1) != ' ')
                    	starPosn = charSequence.length();
                    char prLetter = prefixOfWordList(charSequence, starPosn);
                    addToPriorityList(prLetter);
                }
            }
			/* OLD CODE HERE */
            if( Randomizer.wordList[ searchResult ].equals( charSequence ) ) {
                numMatches++;
                int numChar = charSequence.length();
                texty1.setText("Found!");
                r.resetRandomizerArrays(charSequence);
                generateWordIndices(baseRow,rowDirection,baseCol,colDirection,i,j,numChar);//Delegate the blanking to a function
            }
        }
       
        return numMatches;
    }
	
	public void generateWordIndices(int baseRow,int rowDelta,int baseCol,int colDelta,int i,int j,int len)
	{
		for( int k = i, l = j;len>0;k -= rowDelta, l -= colDelta )//Reverses the original indice generation
		{
			len--;
			blockStore[k][l] = ' ';
			draw();
			vanish();
		}
	}
	
	private static int prefixSearch( Object [ ] a, String x ) {
        int idx = Arrays.binarySearch( a, x );
        
        if( idx < 0 )
            return -idx - 1;//make insertion point usable by turning it positive.
        else
            return idx;
    }
	
	public void addToPriorityList(char letter)  {
        //System.out.println("priority letter = " + letter);
		if (letter == '*') return;            
        
        for (int i = 0; i < Randomizer.priorityList.length; i++) {
            if (Randomizer.priorityList[i] == letter)	{ 
            	//System.out.println("i: " + i+ "priorityList[i]" + Randomizer.priorityList[i]);
            	break;
            }
            else {
                if (Randomizer.priorityList[i] == '*'){
                    Randomizer.priorityList[i] = letter;
                    System.out.println("Added letter to priority list");
                    break;
                }
            }
        }
    }
    
    public char prefixOfWordList(String seq, int starPosn)  {
    	//System.out.println("starPosn" + starPosn);
        String subseq = seq.substring(0, starPosn);
        //System.out.println("subseq = '" + subseq + "'");
        //int low = 0, high = Randomizer.wordList.length;
        //int mid = (low + high)/2;
        
        /*while (low <= high) {
            if (Randomizer.wordList[mid].length() > subseq.length() && Randomizer.wordList[mid].startsWith(subseq))	{
                System.out.println("Priority match : " + subseq + " " + wordList[mid]);
            	return Randomizer.wordList[mid].charAt(starPosn);
            }
            else if (Randomizer.wordList[mid].length() > subseq.length() && Randomizer.wordList[mid].compareToIgnoreCase(seq) > 0){
            	System.out.println("In high = mid-1");
            	high = mid-1;
            }
            else if (Randomizer.wordList[mid].length() > subseq.length() && Randomizer.wordList[mid].compareToIgnoreCase(seq) < 0)	{
            	System.out.println("In low = mid+1");
            	low = mid+1;
            }
        }*/
        for (int i = 0; i < Randomizer.wordList.length; i++) {
            if (Randomizer.wordList[i].length() > subseq.length() && Randomizer.wordList[i].startsWith(subseq)) 
                return Randomizer.wordList[i].charAt(starPosn);
        }        
        return '*';
    }
    
	/* OLD CODE END */
	
	
	/*
	private void drop(int row, int col) { //drop block and all above
		blockStore[7][col] = 'S';
		//for every row of empty space below
		for(int i=(row+1) ; i<=7 ; i++) { //from row below block to bottom
		
			String x = Integer.toString(i);
			texty1.setText(x);
			String y = Integer.toString(col);
			texty2.setText(y);
			blockStore[6][col] = 'X';
			blockStore[7][col] = 'Y';
			blockStore[7][col+1] = 'W';
			blockStore[1][0] = 'Z';
			
			if(blockStore[i][col] != ' ') {
				//for all blocks in column
				for(int j=row ; j>0 ; j--) { //from row to top
					blockStore[j+1][col] = blockStore[j][col]; 
					blockStore[j][col] = ' ';
				}
			}
		}
		draw();
	}
	private void gravity(int row, int col) { //check for space directly below block
		if(blockStore[row+1][col] != ' ') {
			texty2.setText(Character.toString(blockStore[row][col]));
			this.drop(row, col);
		}
	}
	private void gravityCalls() {
		for(int i=0 ; i<whichBlock ; i++) { //call gravity on all blocks
			//texty1.setText("i: " + Integer.toString(i));
			gravity(blocks[i].endRow, blocks[i].col); 
		}
	}
	OLD CODE END */
	
	private void vanish() { //remove blocks from scene
		boolean[] noMore = new boolean[whichBlock+1];
		
//		MainActivity.mp.pause();
//  	    mp = MediaPlayer.create(getApplicationContext(), R.raw.success);
//        mp.start();
//        while(mp.isPlaying()){
//         //Do nothing
//        }
//        MainActivity.mp.start();
		for(int i=0 ; i < noMore.length ; i++) {noMore[i] = false;}
		
		for(int i=0 ; i<whichBlock ; i++) {
			if(blockStore[blocks[i].endRow][blocks[i].col] == ' ') {
				noMore[i] = true;
			}
		}
		
		for(int i=0 ; i<noMore.length ; i++) {
			if(noMore[i] == true) {
				scene.detachChild(blocks[i]);
				found_answer+=blocks[i].value;
				for(int j=i ; j<=whichBlock ; j++) {
					blocks[j] = blocks[j+1];
				}
				blocks[whichBlock] = null;
				whichBlock--;
			}
		}
		found[Num_ans_found]= found_answer;
		Num_ans_found++;
		System.out.println(found_answer);
		System.out.println(Num_ans_found);
		
		gravity();
		if(Num_ans_found == 2){
			Intent endGame = new Intent(this, EndGame.class);
			startActivity(endGame);
			
		}
		draw();		
	}
	/*private void gravity() {
		Boolean fell = false;
		do {
			fell = false;
			for(int i=0 ; i<whichBlock ; i++) {
				//if the block is floating
				if(blockStore[blocks[i].endRow+1][blocks[i].col] == ' ' && blocks[i].endRow != 7) {
					//drop one row
					blockStore[blocks[i].endRow+1][blocks[i].col] = blockStore[blocks[i].endRow][blocks[i].col];
					blockStore[blocks[i].endRow][blocks[i].col] = ' ';
					int r = blocks[i].endRow+1, c = blocks[i].col;
					char c2 = blocks[i].value;
					blocks[i].move(20+block_W*c, panel_X+(r)*block_H-10);
					blocks[i].set_endRow(r);
					fell = true;
				}
			}
		} while(fell);
		//go thru every block
	}*/
	private void gravity() {
		Boolean fell = false;
		do {
			fell = false;
			for(int i=0 ; i<whichBlock ; i++) {
				//if the block is floating
				if(blockStore[blocks[i].endRow+1][blocks[i].col] == ' ' && blocks[i].endRow != 7) {
					int r = blocks[i].endRow, c = blocks[i].col;
					char c2 = blocks[i].value;
					//delete the block
					blockStore[blocks[i].endRow][blocks[i].col] = ' ';
					scene.detachChild(blocks[i]);
					for(int j=i ; j<=whichBlock ; j++) {
						blocks[j] = blocks[j+1];
					}
					blocks[whichBlock] = null;
					int y = whichBlock;
					for(int x=0 ; x<=whichBlock ; x++) {
						if(blocks[x] == null) {
							y=x;
							break;
						}
					}
					whichBlock = y-1; 
					texty1.setText(Integer.toString(whichBlock));
					texty2.setText("grav");
					//create new block in correct place
					newBlock(c, 20+block_W*c, panel_X+(r+1)*block_H-10, c2);
					blocks[whichBlock].moving = false; 
					blockStore[r+1][c] = c2;
					blocks[whichBlock].set_endRow(r+1);
					whichBlock++;
					//blocks[i].move(20+block_W*c, panel_X+(r)*block_H-10);
					//blocks[i].set_endRow(r);
					fell = true;
				}
			}
		} while(fell);
		//go thru every block
	}
	private void draw() {
		for(int i=0 ; i<8 ; i++) {
			String s = i + ": ";
			for(int j=0 ; j<numCol ; j++) {
				if(blockStore[i][j] == ' ') {
					s += "* ";
				}
				else {
					String k = blockStore[i][j] + " ";
					s += k;
				}
			}
			texty[i].setText(s);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        //Log.d(this.getClass().getName(), "back button pressed");
	    	if (mEngine.isRunning()) {
	    		finish();
	    		Intent endGame = new Intent(this, EndGame.class);
				startActivity(endGame);
//	    		saveGame();
//	    		Toast.makeText(this, "Menu button to resume",Toast.LENGTH_SHORT).show();
//	    	} else {
//	    		// unPauseGame();
//	    		
	    	}
	    	return true;

    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
}