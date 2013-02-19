package edu.neu.madcourse.michaelallen.boggle;

import edu.neu.madcourse.michaelallen.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class BoggleMain extends Activity implements OnClickListener{

	View continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.boggle_main);
        
        View exitButton = findViewById(R.id.boggle_exit_button);
        exitButton.setOnClickListener(this);
        View newGameButton = findViewById(R.id.boggle_new_game_button);
        newGameButton.setOnClickListener(this);
        
        View acknowledgementsButton = findViewById(R.id.boggle_acknowledgements_button);
        acknowledgementsButton.setOnClickListener(this);
        
        continueButton = findViewById(R.id.boggle_continue_button);
        continueButton.setOnClickListener(this);
        
        View highScoresButton = findViewById(R.id.boggle_high_scores);
        highScoresButton.setOnClickListener(this);
       
        View boggleRulesButton = findViewById(R.id.boggle_rules_button);
        boggleRulesButton.setOnClickListener(this);
        
    }
    
    @Override
    protected void onResume() {
       super.onResume();
       
       if (Globals.getGlobals().newGameStarted()){
    	   continueButton.setVisibility(View.VISIBLE);
       }
       else{
    	   continueButton.setVisibility(View.INVISIBLE);
       }
    }

    @Override
    protected void onPause() {
       super.onPause();
       //BoggleMusic.stop(this);
    }

    

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 case R.id.boggle_exit_button:
			 finish();
			 break;
		 case R.id.boggle_new_game_button:
			 Intent boggleGame = new Intent(this, BoggleGame.class);
			 startActivity(boggleGame);
			 Globals.getGlobals().setNewGame(true);
			 break;
		 case R.id.boggle_acknowledgements_button:
			 Intent boggleack = new Intent(this, BoggleAcknowledgements.class);
			 startActivity(boggleack);
			 break;
		 case R.id.boggle_continue_button:
			 if(Globals.getGlobals().newGameStarted()){
				 Intent continueGame = new Intent(this, BoggleGame.class);
				 continueGame.putExtra("edu.neu.madcourse.michaelallen.boggle.resume", 1);
				 startActivity(continueGame);
			 }
			 break;
		 case R.id.boggle_high_scores:
			 Intent boggleHS = new Intent(this, BoggleHS.class);
			 startActivity(boggleHS);
			 break;
		 case R.id.boggle_rules_button:
			 Intent boggleRules = new Intent(this, BoggleRules.class);
			 startActivity(boggleRules);
			 break;
		 }
		
	}
    
}