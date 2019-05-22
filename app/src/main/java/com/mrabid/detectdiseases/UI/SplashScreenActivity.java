package com.mrabid.detectdiseases.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrabid.detectdiseases.R;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.pb_progress_splash_screen_activity);
        textView = findViewById(R.id.tv_progress_splash_screen_activity);

        load();

    }
    public void load(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                i++;
                progressBar.setProgress(i);
                textView.setText(i+"%");
                if(i==99){
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
                }else{
                    load();
                }
            }
        },10);
    }
}
