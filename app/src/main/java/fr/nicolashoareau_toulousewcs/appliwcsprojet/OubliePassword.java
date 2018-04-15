package fr.nicolashoareau_toulousewcs.appliwcsprojet;

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

/**
 * Created by perrine on 15/04/18.
 */

public class OubliePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oublie_password);

        final EditText inputEmail = findViewById(R.id.et_email_oublie);
        Button nouveauMdp = findViewById(R.id.btn_nouveau_mdp);
        Button retour = findViewById(R.id.btn_retour);
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OubliePassword.this, ConnexionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nouveauMdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(OubliePassword.this, R.string.entrer_email, Toast.LENGTH_SHORT).show();
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(OubliePassword.this, R.string.mdp_envoye, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OubliePassword.this, R.string.email_inconnu, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
