package edu.neu.madcourse.michaelallen.boggle;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.neu.madcourse.michaelallen.R;
import edu.neu.madcourse.michaelallen.sudoku.Music;

public class BoggleGameView extends View {

	private float viewWidth;
	private float viewHeight;
	private int blockWidth;
	private ArrayList<Rect> selectedBlocks;
	private ArrayList<String> selectedLetters;
	
	private ArrayList<Rect> goodSelectionBlocks;
	private ArrayList<Rect> badSelectionBlocks;
	
	private final BoggleGame game;
	
	
	private static final String TAG = "BoggleGameView";
	
	
	public BoggleGameView(Context context, AttributeSet attrs) {
		super(context);
		this.game = (BoggleGame) context;
		
		selectedBlocks = new ArrayList<Rect>();
		selectedLetters = new ArrayList<String>();
		goodSelectionBlocks = new ArrayList<Rect>();
		badSelectionBlocks = new ArrayList<Rect>();
		
		this.setBackgroundResource(R.drawable.bogglebck);
		
	}
	
	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
	    int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
	    setMeasuredDimension(width,width);
	}
	
	@Override
	   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		viewHeight = h;
		viewWidth = w;
	    blockWidth = (int) (viewWidth / 4f);
	    super.onSizeChanged(w, h, oldw, oldh);
	   }	
	
	@Override
	protected void onDraw(Canvas canvas){
		int greenBlueTranslucent = Color.argb(95, 0, 100, 210);
		
		Paint boardPaint;
		boardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		boardPaint.setColor(greenBlueTranslucent);
		boardPaint.setStyle(Paint.Style.STROKE);
		boardPaint.setStrokeWidth(8);
		
		for (int vert = 0; vert < 5; vert++){
			float lineXVal = vert * blockWidth;
			canvas.drawLine(lineXVal, 0, lineXVal, viewHeight, boardPaint);
		}
		
		for (int horiz = 0; horiz < 5; horiz++){
			float lineYVal = horiz * blockWidth;
			canvas.drawLine(0, lineYVal, viewWidth, lineYVal, boardPaint);
		}		
		
		Paint selected = new Paint();
	    selected.setColor(getResources().getColor(R.color.puzzle_selected));
	    
		for (int block = 0; block < selectedBlocks.size(); block++){
			if (selectedBlocks.get(block) != null){				
			    canvas.drawRect(selectedBlocks.get(block), selected);
			}
		}
		
		Paint goodWordSelection = new Paint();
		goodWordSelection.setColor(getResources().getColor(R.color.boggle_correctWord));
		for (int block = 0; block < goodSelectionBlocks.size(); block++){
			if (goodSelectionBlocks.get(block) != null){				
			    canvas.drawRect(goodSelectionBlocks.get(block), goodWordSelection);
			}
		}
		
		Paint badWordSelection = new Paint();
		badWordSelection.setColor(getResources().getColor(R.color.boggle_incorrectWord));
		for (int block = 0; block < badSelectionBlocks.size(); block++){
			if (badSelectionBlocks.get(block) != null){				
			    canvas.drawRect(badSelectionBlocks.get(block), badWordSelection);
			}
		}
		
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
	      foreground.setColor(getResources().getColor(
	            R.color.puzzle_foreground));
	      foreground.setStyle(Style.FILL);
	      foreground.setTextSize(blockWidth * 0.75f);
	      foreground.setTextAlign(Paint.Align.CENTER);
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				canvas.drawText(this.game.getBoardLetter(i, j),
		                  i * blockWidth + blockWidth/2,
		                  j * blockWidth +  7 * blockWidth / 10,
		                  foreground);
			}
		}
		
		
		
		
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent event){
		if (Globals.getGlobals().getIsPaused()){
			return false;
		}
		
		float x = event.getX();
		float y = event.getY();
		
		
		
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			invalidateBlocks(goodSelectionBlocks);
			invalidateBlocks(badSelectionBlocks);
			
			Rect block = createTouchedBlock(x, y);
			if (block != null){
				int typewriter = Globals.getGlobals().getTypewriter();
				Globals.getGlobals().getSP().play(typewriter , 1, 1, 2, 0, 1);
				
				selectedBlocks.add(block);
				String touchedLetter = getTouchedLetter(x, y);
				selectedLetters.add(touchedLetter);
				this.game.addToSelectedLetterTextView(touchedLetter);
				invalidate(block);
			}
			break;
		case MotionEvent.ACTION_UP:
			checkSelectedLetters();
			return false;
		case MotionEvent.ACTION_MOVE:
			float xBlock = x / blockWidth;
			float yBlock = y / blockWidth;
			if(moveEventWithinTolerableRange(xBlock, yBlock) 
					&& blockIsAdjacentToLastTouchedBlock(xBlock, yBlock)
					){
				Rect block1 = createTouchedBlock(x, y);
				if (block1 != null){
					selectedBlocks.add(block1);
					String touchedLetter = getTouchedLetter(x, y);
					selectedLetters.add(touchedLetter);
					this.game.addToSelectedLetterTextView(touchedLetter);
					invalidate(block1);
				}
			}
			break;
		}
		
		return true;
		
	}
	
	/**
	 * 
	 * @param x left x position of the block touched by user
	 * @param y top y position of the block touched by user
	 * @return a new Rect to be drawn on the touched block iff the block
	 * 			has not already been touched since last word completion
	 * 			else null 
	 */
	private Rect createTouchedBlock(float x, float y){
		
		int touchedBlockX =(int) (x / blockWidth);
		int touchedBlockY =(int) (y / blockWidth);
		if(blockNotAlreadyTouched(touchedBlockX, touchedBlockY)){
			
			
			int touchedBlockRightX = touchedBlockX + 1;
			int touchedBlockBottomY = touchedBlockY + 1;
			
			Rect block = new Rect(
					touchedBlockX * blockWidth,
					touchedBlockY * blockWidth,
					touchedBlockRightX * blockWidth,
					touchedBlockBottomY * blockWidth);
			return block;
		}
		else{
			return null;
		}
		
	}
	
	private boolean blockNotAlreadyTouched(int x, int y){
		boolean notTouched = true;
		for (int blocks = 0; blocks < selectedBlocks.size(); blocks++){
			Rect block = selectedBlocks.get(blocks);
			int blockXFixed = block.left / blockWidth;
			int blockYFixed = block.top / blockWidth;
			if (blockXFixed == x && blockYFixed == y){
				notTouched = false;
				break;
			}
		}
		return notTouched;
	}
	
	private void invalidateAndClearSelectedBlocks(){
		for (int block = 0; block < selectedBlocks.size(); block++){
			Rect rect = selectedBlocks.get(block);
			if (rect != null){
				invalidate(rect);
			}
		}
		selectedBlocks.clear();
	}
	
	/**
	 * Checks if the decimal value of both x and y is > 0.15 and < 0.85
	 * 
	 * @param x block normalized x value of touch event
	 * @param y block normalized y value of touch event
	 */
	private boolean moveEventWithinTolerableRange(float x, float y){
		boolean withinTolerableRange = false;
		
		while(x > 1.0 || y > 1.0){
			if (x > 1.0){
				x -= 1;
			}
			if (y > 1.0){
				y -= 1.0;
			}
		}
		
		if((x > .15 && x < .85) &&
			(y > .15 && y < .85)){
			withinTolerableRange = true;
		}
		return withinTolerableRange;
	}
	
	private String getTouchedLetter(float x, float y){
		int blockXLocation = (int) x / blockWidth;
		int blockYLocation = (int) y / blockWidth;
		
		String letter = this.game.getBoardLetter(blockXLocation, blockYLocation);
		return letter;
	}
	
	/**
	 * checks the word that the user has formed against the dictionary
	 * if it is valid:
	 * 	 user gets points
	 * 	 adds valid word to list of words user has chosen
	 * 
	 * clearAllSelections will be called
	 * @throws IOException 
	 */
	private void checkSelectedLetters(){
		boolean goodWord = this.game.checkWord(selectedLetters);
		
		ArrayList<Rect> selectionAnimationBlocks;
		if (goodWord){
			selectionAnimationBlocks = goodSelectionBlocks;
		}
		else{
			selectionAnimationBlocks = badSelectionBlocks;
		}
		clearAllSelections(selectionAnimationBlocks);
	}
	
	/**
	 * invalidates all selected blocks,
	 * clears the selected blocks, 
	 * removes the text from Selected letters textview
	 * 
	 * adds the blocks in selectedBlocks to the passed Array
	 * this will color the blocks either red or green
	 * sleeps for 500 ms then invalidates the blocks again to remove color
	 * 
	 */
	private void clearAllSelections(ArrayList<Rect> goodOrBadSelection){
		this.game.clearSelectedLetterTextView();
		
		for (int i = 0; i < selectedBlocks.size(); i++){
			Rect block = selectedBlocks.get(i);
			goodOrBadSelection.add(block);
			invalidate(block);
		}
		
		selectedLetters.clear();
		selectedBlocks.clear();
		
		createTimerToRemoveAnimationOnBlocks();		
		
	}
	
	private void createTimerToRemoveAnimationOnBlocks(){
		CountDownTimer blockTimer = new CountDownTimer(500, 500){

			@Override
			public void onFinish() {
				invalidateBlocks(goodSelectionBlocks);
				invalidateBlocks(badSelectionBlocks);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
		};
		blockTimer.start();
	}
	
	private void invalidateBlocks(ArrayList<Rect> rects){
		
		for (int i = 0; i < rects.size(); i++){
			Rect block = rects.get(i);
			invalidate(block);
		}
		
		rects.clear();
		
			
	}
	
	private boolean blockIsAdjacentToLastTouchedBlock(float xBlock, float yBlock){
		int lastTouchedBlockIndex = selectedBlocks.size() - 1;
		Rect lastTouchedBlock = selectedBlocks.get(lastTouchedBlockIndex);
		int lastTouchedXBlock = lastTouchedBlock.left / blockWidth;
		int lastTouchedYBlock = lastTouchedBlock.top / blockWidth;
		
		int distxToX = Math.abs((int)xBlock - lastTouchedXBlock);
		int distyToY = Math.abs((int)yBlock - lastTouchedYBlock);
		
		if(distxToX > 1 || distyToY > 1){
			return false;
		}
		else{
			return true;
		}
		
	}
}