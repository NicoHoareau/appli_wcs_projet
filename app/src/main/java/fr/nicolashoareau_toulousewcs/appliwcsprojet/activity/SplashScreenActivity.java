package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView ivClearWindows = findViewById(R.id.iv_close);
        ivClearWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashScreenActivity.this, ConnectionActivity.class);
                startActivity(i);
            }
        });

        ConstraintLayout splashScreen = findViewById(R.id.splash_screen);
        splashScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this, ConnectionActivity.class));
            }
        });
    }
}
