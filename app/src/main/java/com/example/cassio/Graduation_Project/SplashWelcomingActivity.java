package com.example.cassio.Graduation_Project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class SplashWelcomingActivity extends AppCompatActivity {

    LinearLayout l1;
    Animation uptodown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_welcoming);
        l1 = (LinearLayout) findViewById(R.id.l1);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        l1.setAnimation(uptodown);
        Thread timer1 = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(1800);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    Intent intent = new Intent(SplashWelcomingActivity.this, loginActivity.class);
                    startActivity(intent);
                }


            }
        };
        timer1.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
