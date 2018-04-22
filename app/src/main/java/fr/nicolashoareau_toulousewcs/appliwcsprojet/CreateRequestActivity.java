package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);


    }

    public void validateRequest(View view) {
        Intent intent = new Intent(CreateRequestActivity.this, MenuActivity.class);
        startActivity(intent);
    }

}
