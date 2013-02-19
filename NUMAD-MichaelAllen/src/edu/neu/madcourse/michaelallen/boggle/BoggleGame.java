package edu.neu.madcourse.michaelallen.boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.madcourse.michaelallen.R;

public class BoggleGame extends Activity implements OnClickListener{
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.boggle_game);
		
		View quitGame = findViewById(R.id.boggle_game_quit);
		quitGame.setOnClickListener(this);
		
		Button pausedResume = (Button) findViewById(R.id.boggle_game_pause);
		pausedResume.setOnClickListener(this);
		
		final String RESUME_GAME = "edu.neu.madcourse.michaelallen.boggle.resume";
		int resumed = getIntent().getIntExtra(RESUME_GAME, 0);
		
		TextView SelectedLetters = (TextView) findViewById(R.id.boggle_game_selected);
		TextView CurrentScore = (TextView) findViewById(R.id.boggle_game_currentscore);
		
		if (resumed == 1){
			putSharedPreferences();
			if (Globals.getGlobals().getIsPaused()){
				pausedResume.setText(R.string.boggle_resume_text);
			}
			else{
				pausedResume.setText(R.string.boggle_pause_text);
			}
			TextView timerTextView = (TextView) findViewById(R.id.boggle_game_timer);
			int min = (int) (Globals.getGlobals().getTimerVal() / 60);
	    	int sec = (int) (Globals.getGlobals().getTimerVal() % 60);
			timerTextView.setText("" + min + ":" + sec);
		}
		else{
			Globals.getGlobals().resetAllVariables();
			populateBoardWithLetters();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.boggle_game_quit:
			finish();
			break;
		case R.id.boggle_game_pause:
			switchPausedOrResumed();
		}
		
	}

    @Override
    protected void onResume() {
       super.onResume();
       Globals.getGlobals().initSoundPool(this);
       if(!Globals.getGlobals().getIsPaused()){
    	   Globals.getGlobals().setTimer(makeTimer(Globals.getGlobals().getTimerVal()));
       }
    }

    @Override
    protected void onPause() {
       super.onPause();
       Globals.getGlobals().getSP().release();
       if(!Globals.getGlobals().getIsPaused()){
    	   Globals.getGlobals().clearTimer();
       }
       
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	saveSharedPreferences();
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    }
    
    
    


	public void setBoardLetter(int i, int j, String x){
		Globals.getGlobals().setBoardLetters(i, j,  x);
	}
	public String getBoardLetter(int i, int j){
		if(i > 3){
			i = 3;
		}
		if (j > 3){
			j = 3;
		}
		return Globals.getGlobals().getBoardLetters(i, j);
	}
	public void addToSelectedLetterTextView(String letter){
		TextView SelectedLetters = (TextView) findViewById(R.id.boggle_game_selected);
		String currentText = SelectedLetters.getText().toString();
		currentText = currentText + letter;
		SelectedLetters.setText(currentText);
	}
	public void clearSelectedLetterTextView(){
		TextView SelectedLetters = (TextView) findViewById(R.id.boggle_game_selected);
		SelectedLetters.setText("");
	}
	
	public boolean checkWord(ArrayList<String> selectedLetters){
		if (selectedLetters.size() < 3){
			return false;
		}
		
		String selectedWord = "";
		for (int i = 0; i < selectedLetters.size(); i++){
			selectedWord += selectedLetters.get(i);
		}
		
		selectedWord = selectedWord.toLowerCase();
		if (selectedWordAlreadyChosenPrior(selectedWord)){
			return false;
		}
		
		String loc = getDictionaryLocationForWord(selectedWord);
		boolean match;
		match = matchForWordInGivenFile(selectedWord, loc);

		if (match){
			successfulWord(selectedWord);
			int dink = Globals.getGlobals().getDink();
			Globals.getGlobals().getSP().play(dink, 1, 1, 1, 0, 1);
			return true;
		}
			
		
		return false;
		
		
		
	}
	
	private void switchPausedOrResumed(){
		Button pausedResume = (Button) findViewById(R.id.boggle_game_pause);
		
		Globals.getGlobals().switchIsPaused();
		
		if (Globals.getGlobals().getIsPaused()){
			pausedResume.setText(R.string.boggle_resume_text);
			Globals.getGlobals().clearTimer();
		}
		else{
			pausedResume.setText(R.string.boggle_pause_text);
			Globals.getGlobals().setTimer(makeTimer(Globals.getGlobals().getTimerVal()));
		}
	}
	
	private void saveSharedPreferences(){
    	SharedPreferences sPref = getSharedPreferences(Globals.getGlobals().getSharedPrefName(), 0);
    	Editor e = sPref.edit();
    	
    	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    	
    	SharedPrefHolder sph = new SharedPrefHolder();
    	sph.boardLetters = Globals.getGlobals().getBoard();
    	sph.isPaused = Globals.getGlobals().getIsPaused();
    	sph.newGame = Globals.getGlobals().newGameStarted();
    	sph.priorChosenWords = Globals.getGlobals().getPriorWords();
    	sph.score = Globals.getGlobals().getScore();
    	sph.timerVal = Globals.getGlobals().getTimerVal();
    	
    	String sphJson = gson.toJson(sph);
    	
    	
    	e.putString("boggle", sphJson);
    	e.commit();
    	
	}
	
	private void setCurrentScore(int n){
		TextView CurrentScore = (TextView) findViewById(R.id.boggle_game_currentscore);
		CurrentScore.setText("Score: " + n);
	}
	
	private void putSharedPreferences(){
		SharedPreferences sPref = getSharedPreferences("BoggleSharedPref", 0);
    	
    	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    	
    	String json = sPref.getString("boggle", null);
    	if (json != null){
    		SharedPrefHolder sph = gson.fromJson(json, SharedPrefHolder.class);
    		
    		Globals.getGlobals().setBoard(sph.boardLetters);
    		Globals.getGlobals().setisPaused(sph.isPaused);
    		Globals.getGlobals().setNewGame(sph.newGame);
    		Globals.getGlobals().putPriorWords(sph.priorChosenWords);
    		Globals.getGlobals().setScore(sph.score);
    		Globals.getGlobals().setTimerVal(sph.timerVal);
    		
    		
    		setCurrentScore(Globals.getGlobals().getScore());
    	}
    	
    	
    	
    	
	}
	
	private CountDownTimer makeTimer(long sec){
		CountDownTimer t =  new CountDownTimer(sec * 1000, 1000) {

			TextView timerTextView = (TextView) findViewById(R.id.boggle_game_timer);
			int beep = Globals.getGlobals().getBeep();
			
		     public void onTick(long millisUntilFinished) {
		    	 int SecondsUntilFinished = (int) millisUntilFinished / 1000;
		    	 int min = SecondsUntilFinished / 60;
		    	 int sec = SecondsUntilFinished % 60;
		    	 
		    	 if (sec < 10) {
		    		 timerTextView.setText(min + ":" + "0" + sec);
		    		 if (min < 1){
		    			 Globals.getGlobals().getSP().play(beep, 1, 1, 1, 0, 1);
		    		 }
		    	 }
		    	 
		    	 else {
		    		 timerTextView.setText(min + ":" + sec);
		    	 }
		    	 Globals.getGlobals().setTimerVal(millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		    	 timerTextView.setText("0:00");
		         timerHitZero();
		     }
		  };
		  return t;
	}
	
	/**
	 * Since my wordlist is split into files starting with "_" 
	 * 		+ the first three letters of the words within the file,
	 * 		this will be the file location to check in the assets folder
	 */
	private String getDictionaryLocationForWord(String selectedWord){
		String loc = "words/" + "_" + selectedWord.substring(0, 3) + ".txt";
		return loc;
	}
	
	private boolean selectedWordAlreadyChosenPrior(String selectedWord){
		
		ArrayList<String> priorWords = Globals.getGlobals().getPriorWords();
		for (int j = 0; j < priorWords.size(); j++){
			if (priorWords.get(j).equals(selectedWord)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Matches a substring of the selectedWord, which has the first three characters removed,
	 *		to the strings wihin the fileLoc dictionary file
	 * Since all strings in fileLoc are in the same format with the first three characters
	 * being contained within the fileLoc name
	 */
	private boolean matchForWordInGivenFile(String selectedWord, String fileLoc) {
		
		InputStream stream;
		try {
			stream = getAssets().open(fileLoc);
			String suffix = selectedWord.substring(3);
			
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader buf = new BufferedReader(reader);
			
			String nextLine = buf.readLine();
			while (nextLine != null){
				if (suffix.equals(nextLine)){
					stream.close();
					return true;
				}
				nextLine = buf.readLine();
			} 
			
			
			stream.close();
			return false;
		
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * increases the points the user has scored for the game
	 * based on the size of word:
	 * 		word length = 3 or 4 ---> 1 point
	 * 		word length > 4 ---> 1 point + length of word over 4 characters
	 * adds the successful word to the priorWords global array
	 * clears the selectedLetters text view's text
	 */
	private void successfulWord(String word){
		int wordSize = word.length();
		int points = 1;
		wordSize -= 4;
		if (wordSize > 0){
			points = points + wordSize;
		}
		
		
		
		Globals.getGlobals().increaseScore(points);
		int totalScore = Globals.getGlobals().getScore();
		setCurrentScore(totalScore);
		
		Globals.getGlobals().addChosenWord(word);
		
		
		clearSelectedLetterTextView();
		
		
	}
	
	private void populateBoardWithLetters(){
		ArrayList<String> pickedLetters = new ArrayList<String>();
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				String rndLetter = getWeightedRandomLetter();
				while (!isLetterOkayToUseOnBoard(pickedLetters, rndLetter)){
					rndLetter = getWeightedRandomLetter();
				}
				
				
				setBoardLetter(i, j, rndLetter);
				pickedLetters.add(rndLetter);
			}
		}
	}
	
	private void timerHitZero(){
		openScoreScreenDialog();
	}
	
	private void openScoreScreenDialog(){
		Globals.getGlobals().setNewGame(false);
		AlertDialog.Builder scoreDialog = new AlertDialog.Builder(this);
		scoreDialog.create();
		scoreDialog.setMessage("Time's up!");
		scoreDialog.setPositiveButton("View Score", new DialogInterface.OnClickListener(){
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				openScoreScreen();
			}
			
		});
		scoreDialog.setIcon(R.drawable.ic_launcher);
		scoreDialog.setInverseBackgroundForced(true);
		scoreDialog.show();
	}
	
	private void openScoreScreen(){
		Intent scoreScreen = new Intent(this, BoggleScoreScreen.class);
		startActivity(scoreScreen);
		finish();
	}
	private boolean isLetterOkayToUseOnBoard(ArrayList<String> letters, String x){
		int count = 0;
		
		for (int i = 0; i < letters.size(); i++){
			if (letters.get(i) == x){
				count++;
			}
		}
		
		if (count < 3){
			return true;
		}
		else {
			return false;
		}
	}
	
	private String getWeightedRandomLetter(){
		String letter = "A";
		
		Random rng = new Random();
		int randInt = rng.nextInt(65);
		
		if (randInt < 3)
			return "A";
		if (2 < randInt && randInt < 5)
			return "B";
		if (4 < randInt && randInt < 7)
			return "C";
		if (6 < randInt && randInt < 9)
			return "D";
		if (8 < randInt && randInt < 12)
			return "E";
		if (11 < randInt && randInt < 14)
			return "F";
		if (13 < randInt && randInt < 16)
			return "G";
		if (15 < randInt && randInt < 18)
			return "H";
		if (17 < randInt && randInt < 21)
			return "I";
		if (20 < randInt && randInt < 22)
			return "J";
		if (21 < randInt && randInt < 23)
			return "K";
		if (22 < randInt && randInt < 25)
			return "L";
		if (24 < randInt && randInt < 27)
			return "M";
		if (26 < randInt && randInt < 29)
			return "N";
		if (28 < randInt && randInt < 34)
			return "O";
		if (33 < randInt && randInt < 34)
			return "P";
		if (33 < randInt && randInt < 35)
			return "Q";
		if (34 < randInt && randInt < 37)
			return "R";
		if (36 < randInt && randInt < 39)
			return "S";
		if (38 < randInt && randInt < 41)
			return "T";
		if (40 < randInt && randInt < 45)
			return "U";
		if (44 < randInt && randInt < 46)
			return "V";
		if (45 < randInt && randInt < 47)
			return "W";
		if (46 < randInt && randInt < 48)
			return "X";
		if (47 < randInt && randInt < 49)
			return "Y";
		if (48 < randInt && randInt < 50)
			return "Z";
		if (49 < randInt && randInt < 54)
			return "E";
		if (53 < randInt && randInt < 57)
			return "I";
		if (56 < randInt && randInt < 60)
			return "A";
		if (59 < randInt && randInt < 63)
			return "U";
		else{
			return letter;
		}
	}
	

}