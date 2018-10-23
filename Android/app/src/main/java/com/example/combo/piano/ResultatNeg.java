package com.example.combo.piano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * L'activite ResultNeg est appelee lorsque le joueur perd la partie.
 * Elle affiche ce resultat ainsi que le meilleur score s'il y en a un
 */

public class ResultatNeg extends AppCompatActivity{

    private Button rejouer;                 // Les boutons pour recommencer une nouvelle partie et pour acceder au menu
    private Button menu;

    private TextView meilleur;              // Un textview pour afficher le meilleur score s'il y en a un
    private TextView affichage;

    private static MyDBAdapater dbAdapater; // La base de donnees pour acceder a la liste des scores et recuperer le meilleur score


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultatneg);

        rejouer = findViewById(R.id.brejouer);                                      // Association des composants du fichier xml aux variables
        menu = findViewById(R.id.bmenu);
        meilleur = findViewById(R.id.meilleurScoreTextView);
        affichage = findViewById(R.id.textviewAffichage);

        dbAdapater = new MyDBAdapater(this);                                 // On ouvre la base de donnees
        dbAdapater.open();

        ArrayList<Score> listsc = dbAdapater.getAllScores();                        // On recupere la liste des scores

        if(listsc.size() >= 1){ meilleur.setText("" + listsc.get(0).getScore()); }  // Si la liste contient au moins un element on affiche le premier score de cette liste
        else{                                                                       // Sinon on cache les deux textview
            affichage.setText("");
            meilleur.setText("");
        }

        rejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                            // bouton pour acceder a une nouvelle partie
                Intent intent = new Intent(ResultatNeg.this, Game.class);
                startActivity(intent);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                // bouton pour acceder a l'accueil
                Intent intent = new Intent(ResultatNeg.this, Accueil.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override                                                                             // Lorsque l'activite est terminee, on ferme la base de donnees
    protected void onDestroy() {
        dbAdapater.close();
        super.onDestroy();
    }
}
