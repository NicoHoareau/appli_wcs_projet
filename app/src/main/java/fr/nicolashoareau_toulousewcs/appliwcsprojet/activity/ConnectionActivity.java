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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button logIn = findViewById(R.id.btn_sign_in);
        Button signIn = findViewById(R.id.btn_already_registered);
        Button forgotPass = findViewById(R.id.btn_forgot_pass);

        final EditText etEmail = findViewById(R.id.et_email);
        final EditText etPassword = findViewById(R.id.et_password);
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        logIn.setText(R.string.log_in);
        signIn.setText(R.string.sign_in);
        forgotPass.setVisibility(View.VISIBLE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectionActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectionActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(ConnectionActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ConnectionActivity.this, R.string.entrer_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(ConnectionActivity.this, R.string.entrer_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ConnectionActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ConnectionActivity.this, R.string.unkown_user, Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(ConnectionActivity.this, MenuActivity.class);
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
