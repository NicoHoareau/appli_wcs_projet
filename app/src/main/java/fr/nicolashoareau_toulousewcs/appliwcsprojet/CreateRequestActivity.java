package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class CreateRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        TextView tvCodeRequest = findViewById(R.id.tv_code_request);
        //Génération d'un id unique
        String key1 = "123456789";
        String key2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String codeRequest = String.format("#%s%s%s", "REQ", generateString(3, key1), generateString(3, key2));
        tvCodeRequest.setText(codeRequest);

    }

    public void validateRequest(View view) {
        Intent intent = new Intent(CreateRequestActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    //Génération de code unique personnalisé :
    private String generateString(int length, String key) {
        char[] char1 = key.toCharArray();
        StringBuilder stringBuilder1 = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = char1[random.nextInt(char1.length)];
            stringBuilder1.append(c);
        }
        return stringBuilder1.toString();
    }

}
