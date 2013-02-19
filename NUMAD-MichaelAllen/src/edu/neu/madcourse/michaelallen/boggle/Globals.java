package edu.neu.madcourse.michaelallen.boggle;

import java.util.ArrayList;

import edu.neu.madcourse.michaelallen.R;
import edu.neu.madcourse.michaelallen.sudoku.Game;

import android.content.Context;
import android.media.SoundPool;
import android.os.CountDownTimer;

public class Globals{
	private Globals(){}
	
	private String[][] boardLetters = new String[4][4];
	
	private int score = 0;
	
	private ArrayList<String> priorChosenWords = new ArrayList<String>();
	
	private boolean newGame = false;
	
	private long timerVal = 120;
	
	private CountDownTimer gameTimer;
	
	private boolean isPaused = false;
	
	private SoundPool sp;
	private int dink;
	private int typewriter;
	private int beep;
	
	private final String SHARED_PREF_NAME = "BoggleSharedPref";
	private final String HIGH_SCORE_PREF = "HighScore";
	
	private static class GlobalHolder{
		private static final Globals INSTANCE = new Globals();
	}
	
	public void setTimer(CountDownTimer t){
		gameTimer = t;
		gameTimer.start();
	}
	
	public void clearTimer(){
		gameTimer.cancel();
	}
	
	public void initSoundPool(Context c){
		sp = new SoundPool(3, 3, 0);
		dink = sp.load(c, R.raw.dink, 1);
		typewriter = sp.load(c, R.raw.typewriter, 3);
		beep = sp.load(c, R.raw.bleep, 2);
		
	}
	
	public int getDink(){
		return dink;
	}
	
	public int getTypewriter(){
		return typewriter;
	}
	
	public int getBeep(){
		return beep;
	}
	
	public SoundPool getSP(){
		return sp;
	}
	
	public String getSharedPrefName(){
		return SHARED_PREF_NAME;
	}
	
	public String getHighScorePrefName(){
		return HIGH_SCORE_PREF;
	}
	
	public boolean getIsPaused(){
		return isPaused;
	}
	
	public void switchIsPaused(){
		isPaused = !isPaused;
	}
	
	public void setisPaused(boolean b){
		isPaused = b;
	}
	
	public long getTimerVal(){
		return timerVal;
	}
	
	public void setTimerVal(long val){
		timerVal = val;
	}
	
	public boolean newGameStarted(){
		return newGame;
	}
	
	public void setNewGame(boolean b){
		newGame = b;
	}
	
	public String getBoardLetters(int i, int j){
		return boardLetters[i][j];
	}
	
	public void setBoardLetters(int i, int j, String letter){
		boardLetters[i][j] = letter;
	}
	
	public String[][] getBoard(){
		final String[][] board = boardLetters;
		return board;
	}

	public void setBoard(String[][] board){
		boardLetters = board;
	}
	
	public int getScore(){
		return score;
	}
	
	public void increaseScore(int n){
		score += n;
	}
	
	public void setScore(int n){
		score = n;
	}
	
	public ArrayList<String> getPriorWords(){
		final ArrayList<String> words = priorChosenWords;
		return words;
	}
	
	public void putPriorWords(ArrayList<String> priors){
		priorChosenWords = priors;
	}
	
	public void addChosenWord(String word){
		priorChosenWords.add(word);
	}
	
	public void resetAllVariables(){
		boardLetters = new String[4][4];
		score = 0;
		priorChosenWords = new ArrayList<String>();
		timerVal = 120; 
		gameTimer = null;
		isPaused = false;
	}
	
	public static Globals getGlobals(){
		return GlobalHolder.INSTANCE;
	}
	
	
}