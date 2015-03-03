package com.entropy.bazzinga;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
 
public class Player extends GameObject {
 
    // ===========================================================
    // Constants
    // ===========================================================
	
	
    // ===========================================================
    // Fields
    // ===========================================================
	BazzingaActivity b;
	Boolean moving;
	protected int col;
	protected int endRow;
	protected char value;
    // ===========================================================
    // Constructors
    // ===========================================================
 
    public Player(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, BazzingaActivity b, char value) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.b = b;
        moving = true;
        col = 3;
        this.value = value;
    }
    public Player(int col, final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, BazzingaActivity b, char value) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.b = b;
        moving = true;
        this.col = col;
        this.value = value;
    }
 
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    public boolean onAreaTouched(final TouchEvent e, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		//move
    	float old = mX;
    	this.setPosition(e.getX() - this.getWidth() / 2, mY);
    	//check collision
    	Boolean goRight = where(old, mX);
    	if(b.whichBlock > 0) {
	    	for(int i=0 ; i<b.whichBlock ; i++) {
	    		if(this.collidesWith(b.blocks[i]) && ((goRight && this.col < b.blocks[i].col) || (!goRight && this.col > b.blocks[i].col))) {
	        		mX = old;
	        		break;
	        	}
	    	}	
    	}
    	
    	//snap
    	switch(e.getAction()) {
		case TouchEvent.ACTION_UP: 
			//which column
			int col = (int)(mX-20)/75;
			int overlap = (int)(mX-20)%75;
	    	if(overlap > 40) {
	    		col++;
	    	}
	    	mX = col*75 + 20;
	    	this.col = col;
			break;
    	}
		return true;
	}
    
    @Override
    public void move() {
    	if(moving == true) {
    		this.mPhysicsHandler.setVelocityY(75);
    		checkCollision();
    	}
    	else {
    		this.mPhysicsHandler.setVelocityY(0);
    	}
    }
 
    // ===========================================================
    // Methods
    // ===========================================================
    public void set_endRow(int endRow) {
    	this.endRow = endRow;
    }
    private void checkCollision() {
    	    	
    	//check if collide with bottom wall
    	if(this.collidesWith(b.botWall)) {
			endRow = 7; //HARDCODE
			b.newBlock();
    	}
    	
    	else if(b.whichBlock >= 0) {
	    	for(int i=0 ; i<b.whichBlock ; i++) {
	    		if(this.collidesWith(b.blocks[i]) && this.col == b.blocks[i].col) {
	    			//b.texty1.setText(Integer.toString(i));
	    			endRow = b.blocks[i].endRow - 1;
	        		b.newBlock();
	        		break;
	        	}
	    	}	
    	}
    	
    	//check if above another
    }
    private Boolean where(float oldX, float newX) {
    	if(oldX <= newX) {
    		return true;
    	}
    	return false;
    }
    public void move(int x, int y) {
    	this.mX = x;
    	this.mY = y;
    }
    public float getX() {
    	return mX;
    }
    public float getY() {
    	return mY;
    }
    
}