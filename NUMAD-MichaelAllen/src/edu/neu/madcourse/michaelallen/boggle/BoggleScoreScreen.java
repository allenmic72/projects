package edu.neu.madcourse.michaelallen.boggle;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.neu.madcourse.michaelallen.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BoggleScoreScreen extends Activity implements OnClickListener{
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.boggle_score_screen);
		
		TextView scoreText = (TextView) findViewById(R.id.boggle_score_textview);
		String text = "Your score was " + Globals.getGlobals().getScore();
		scoreText.setText(text);
		
		TextView wordsFound = (TextView) findViewById(R.id.boggle_score_screen_words);
		String wordsFoundText = "";
		ArrayList<String> foundWords = Globals.getGlobals().getPriorWords();
		for (int i = 0; i < foundWords.size(); i++){
			wordsFoundText = wordsFoundText + " " + foundWords.get(i);
		}
		wordsFound.setText("You found the following words: " + wordsFoundText);
		
		saveScoreIfHigh(Globals.getGlobals().getScore());
		
		View mainScreen = findViewById(R.id.boggle_main_screen_button);
		mainScreen.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.boggle_main_screen_button:
			finish();
		}
		
	}
	
	public void saveScoreIfHigh(int score){
		SharedPreferences spr = getSharedPreferences(Globals.getGlobals().getHighScorePrefName(), 0);
		Editor e = spr.edit();
		Gson gson = new Gson();

		String oldHS = spr.getString("highscores", null);
		Log.d("HS checkin", oldHS + " something");
		if (oldHS != null){
			
			BoggleHighScores oldHighScore = gson.fromJson(oldHS, BoggleHighScores.class);
			
			int[] newhs = checkOldHighScores(0, oldHighScore.highscores, score);
			Log.d("High score checkiing", gson.toJson(newhs));
			String jjson = gson.toJson(new BoggleHighScores(newhs));
			e.putString("highscores", jjson);
			e.commit();
		}
		else{
			int[] scores = new int[5];
			scores[0] = score;
			BoggleHighScores hs = new BoggleHighScores(scores);
			String json = gson.toJson(hs);
			Log.d("to json", json);
			e.putString("highscores", json);
			e.commit();
			showDialogToUser(0);
		}
		
		
		
		
	}
	int count = 0;
	private int[] checkOldHighScores(int i, int[] hs, int score){
		while (i < 5){
			Log.d("old hs", "score in array is " + hs[i] + " at index " + i);
			if (hs[i] < score){				
				int tmp = hs[i];
				Log.d("", "old score at index " + i + " was" + tmp);
				hs[i] = score;
				if (count == 0){
					showDialogToUser(i);
					count++;
				}
				return checkOldHighScores(i+1, hs, tmp); //shift old score
			}
			i++;
		}
		return hs;
	}
	
	private void showDialogToUser(int i){
		AlertDialog.Builder IdDialogBuilder = new AlertDialog.Builder(this);
		IdDialogBuilder.create();
		i++;
		IdDialogBuilder.setMessage("You got the #" + i + " position on the High Score list!");
		IdDialogBuilder.setPositiveButton("Cool", new DialogInterface.OnClickListener(){
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences spr = getSharedPreferences(Globals.getGlobals().getHighScorePrefName(), 0);
				Gson gson = new Gson();

				String oldHS = spr.getString("highscores", null);
				Log.d("tst", oldHS + " ");
			}
			
		});
		IdDialogBuilder.show();
	}
}