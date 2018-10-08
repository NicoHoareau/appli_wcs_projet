package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        final EditText inputEmail = findViewById(R.id.et_email);
        final EditText inputPassword = findViewById(R.id.et_password);
        final TextView msgError = findViewById(R.id.msg_error);
        Button btnInscription = findViewById(R.id.btn_inscription);
        Button btnDejaInscrit = findViewById(R.id.btn_deja_inscrit);
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        btnDejaInscrit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ConnectionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    msgError.setText(R.string.entrer_email);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    msgError.setText(R.string.entrer_password);
                    return;
                }

                if (password.length() < 6) {
                    msgError.setText(R.string.password_too_short);
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, R.string.failure_registration, Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(SignInActivity.this, CreateProfilActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
