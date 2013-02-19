package edu.neu.madcourse.michaelallen.boggle;

import com.google.gson.annotations.Expose;



public class BoggleHighScores{
	
	BoggleHighScores(int[] highscores){
		this.highscores = highscores;
	}
	@Expose
	public int[] highscores;
	
}