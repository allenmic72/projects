package edu.neu.madcourse.michaelallen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.*;
import edu.neu.madcourse.michaelallen.boggle.BoggleMain;
import edu.neu.madcourse.michaelallen.boggle.Globals;
import edu.neu.madcourse.michaelallen.sudoku.Sudoku;
import edu.neu.mobileClass.*;


public class MainActivity extends Activity implements OnClickListener{

	Globals globals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        globals = Globals.getGlobals();
        
        //TODO: uncomment this
        PhoneCheckAPI.doAuthorization(this);

        this.setTitle("Michael Allen");
        
        View teamButton = findViewById(R.id.team_button);
        teamButton.setOnClickListener(this);
        
        View errorButton = findViewById(R.id.create_error_button);
        errorButton.setOnClickListener(this);
        
        View sudokuButton = findViewById(R.id.sudoku_button);
        sudokuButton.setOnClickListener(this);
        
        View mainExitButton = findViewById(R.id.main_exit_button);
        mainExitButton.setOnClickListener(this);
        
        View boggleButton = findViewById(R.id.boggle_main_button);
        boggleButton.setOnClickListener(this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 case R.id.team_button:
			 Intent teamIntent = new Intent(this, Team.class);
			 startActivity(teamIntent);
			 break;
		 case R.id.create_error_button:
			 int error = 5/0;
			 error = error + error;
			 break;
		 case R.id.sudoku_button:
			 Intent sudokuIntent = new Intent(this, Sudoku.class);
			 startActivity(sudokuIntent);
			 break;
		 case R.id.main_exit_button:
			 finish();
			 break;
		 case R.id.boggle_main_button:
			 Intent boggleIntent = new Intent(this, BoggleMain.class);
			 startActivity(boggleIntent);
			 break;
		 }
		
	}
	
    
}
