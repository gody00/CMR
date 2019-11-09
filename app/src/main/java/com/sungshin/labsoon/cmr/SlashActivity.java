package com.sungshin.labsoon.cmr;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sungshin.labsoon.cmr.Main.MainActivity;

public class SlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);

        final Intent mainIntent = new Intent(this, MainActivity.class);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(mainIntent);
                finish();

            }
        }, 3000);

    }
}
