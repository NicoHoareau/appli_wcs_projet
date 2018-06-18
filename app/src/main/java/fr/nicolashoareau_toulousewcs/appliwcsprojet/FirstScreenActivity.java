package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FirstScreenActivity extends AppCompatActivity {

    //private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(FirstScreenActivity.this, ConnexionActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);*/

        ImageView ivClearWindows = findViewById(R.id.iv_clear);
        ivClearWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstScreenActivity.this, ConnexionActivity.class);
                startActivity(i);
            }
        });
    }
}
