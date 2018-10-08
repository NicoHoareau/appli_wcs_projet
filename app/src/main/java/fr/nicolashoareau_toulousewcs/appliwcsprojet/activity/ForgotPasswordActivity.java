package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;

/**
 * Created by perrine on 15/04/18.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oublie_password);

        final EditText inputEmail = findViewById(R.id.et_email);
        Button newPass = findViewById(R.id.btn_new_password);
        Button back = findViewById(R.id.btn_back);
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, ConnectionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        newPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, R.string.entrer_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, R.string.pass_send, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, R.string.unknown_email, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
