package fr.nicolashoareau_toulousewcs.appliwcsprojet.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button connexion = findViewById(R.id.btn_inscription);
        Button inscription = findViewById(R.id.btn_deja_inscrit);
        Button oublieMdp = findViewById(R.id.btn_mdp_oublie);

        final EditText etEmail = findViewById(R.id.et_email);
        final EditText etPassword = findViewById(R.id.et_password);
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        connexion.setText(R.string.connexion);
        inscription.setText(R.string.inscription);
        oublieMdp.setVisibility(View.VISIBLE);

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        oublieMdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this, OubliePasswordActivity.class);
                startActivity(intent);
            }
        });

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(ConnexionActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ConnexionActivity.this, R.string.entrer_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(ConnexionActivity.this, R.string.entrer_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ConnexionActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ConnexionActivity.this, R.string.utilisateur_inconnu, Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(ConnexionActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
