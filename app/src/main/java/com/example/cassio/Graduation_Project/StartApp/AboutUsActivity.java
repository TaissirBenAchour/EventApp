package com.example.cassio.Graduation_Project.StartApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.cassio.Graduation_Project.R;

public class AboutUsActivity extends AppCompatActivity {
        ImageView btnIgs,btngit;

        public AboutUsActivity() {
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.about_us_layout);
            btnIgs= (ImageView) findViewById(R.id.btnigs_id);
            btngit=(ImageView) findViewById(R.id.btngit_id);
            btnIgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://instagram.com/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            btngit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://github.com/TaissirBenAchour/EventMobileApp?files=1");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }