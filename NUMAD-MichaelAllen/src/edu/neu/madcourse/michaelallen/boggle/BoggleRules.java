package edu.neu.madcourse.michaelallen.boggle;

import edu.neu.madcourse.michaelallen.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BoggleRules extends Activity implements OnClickListener{
	 @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.boggle_rules);
	      
	      View quit = findViewById(R.id.boggle_rules_quit);
	      quit.setOnClickListener(this);
	   }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.boggle_rules_quit:
			finish();
			break;
		}
		
	}
}