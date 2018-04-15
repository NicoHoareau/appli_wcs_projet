package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button connexion = findViewById(R.id.btn_inscription);
        connexion.setText(R.string.connexion);

        Button inscription = findViewById(R.id.btn_deja_inscrit);
        inscription.setText(R.string.inscription);

        Button oublieMdp = findViewById(R.id.btn_mdp_oublie);
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
                Intent intent = new Intent(ConnexionActivity.this, OubliePassword.class);
                startActivity(intent);
            }
        });
    }
}
