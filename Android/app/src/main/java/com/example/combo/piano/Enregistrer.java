package com.example.combo.piano;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * L'activite Enregistrer permet de sauvegarder le score du joueur, et de recuperer son nom.
 * Elle va recuperer les donnees envoyees par la classe ResultatPos, si le joueur appuie sur le bouton valider, elle les transmettra ensuite a l'activite ListeScores
 */

public class Enregistrer extends AppCompatActivity{

    private Button valider;                 // boutons pour valider et annuler l'enregistrement
    private Button annuler;
    private Button photo;                   // bouton pour prendre en photo le joueur

    ImageView imgTakePic;
    private static final  int CAN_REQUIEST = 1313;

    private TextView score;                 // textviews pour afficher le score du joueur
    private TextView date;
    private EditText nom;                   // edittext pour permettre au joueur d'ecrire son nom

    private String mscore;                  // variables pour recuperer les donnees
    private String mdate;
    private String mnom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregistrement);

        valider = findViewById(R.id.bvalider);              // Association des composants du fichier xml aux variables
        annuler = findViewById(R.id.bannuler);

        score = findViewById(R.id.scoreTextView);
        date = findViewById(R.id.dateTextView);
        nom = findViewById(R.id.nomEdit);

        Intent data = getIntent();                          // Recuperation des donnees
        mdate = data.getStringExtra("date");
        mscore = data.getStringExtra("score");

        score.setText("Score : " + mscore);                 // Affichage du score et de la date
        date.setText("Date : " + mdate);


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                        // bouton pour valider l'enregistrement et pour passer les donnees a ListScores
                if((mnom = nom.getText().toString()) != null && 2 < mnom.length()){                  // Si le joueur a entre au moins 3 caracteres on envoie
                    Intent intent = new Intent(Enregistrer.this, ListeScores.class);
                    intent.putExtra("score", mscore);
                    intent.putExtra("date", mdate);
                    intent.putExtra("nom", mnom);
                    startActivity(intent);
                    finish();
                }
                else{                                                                                   // Sinon, on signale au joueur de le faire
                    Toast.makeText(getApplicationContext(), "Entrez un nom de plus de deux lettres svp", Toast.LENGTH_LONG).show();
                }
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                            // bouton pour annuler l'action, on envoie le joueur sur la page d'accueil
                Intent intent = new Intent(Enregistrer.this, Accueil.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
