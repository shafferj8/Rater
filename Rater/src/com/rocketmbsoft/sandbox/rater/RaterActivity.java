package com.rocketmbsoft.sandbox.rater;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class RaterActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Rater r = new Rater(
        		this, 
        		PreferenceManager.getDefaultSharedPreferences(this), 
        		"My App", 
        		"com.rocketmbsoft.sandbox.rater");
        
        r.run();
    }
}