package com.example.combo.piano;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * L'activite ResultPos est appelee lorsque le joueur gagne la partie.
 * Elle affiche ce resultat ainsi que le meilleur score s'il y en a un
 * Si le score du joueur est meilleur que celui de la base de donnees, on affiche le nouveau record
 */

public class ResultatPos extends AppCompatActivity{


    private Button rejouer;                                 // Les boutons pour recommencer une nouvelle partie, pour enregistrer son score et pour acceder au menu
    private Button enregistrer;
    private Button menu;

    private TextView score;                                 // Les textviews pour afficher le meilleur score et l'actuel
    private TextView meilleur;
    private TextView affichage;

    private String mdate;                                   // Des variables pour stocker les valeurs envoyees par l'activite Game
    private String mscore;

    private static MyDBAdapater dbAdapater;                 // La base de donnees pour acceder au premier element de la liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultatpos);

        Intent data = getIntent();                          // On recupere l'intent de cette classe

        rejouer = findViewById(R.id.brejouer);              // Association des composants du fichier xml aux variables
        enregistrer = findViewById(R.id.benregistrer);
        menu = findViewById(R.id.bmenu);

        score = findViewById(R.id.score_actuel);
        meilleur = findViewById(R.id.meilleurScoreTextView);
        affichage = findViewById(R.id.textviewAffichage);

        mscore = data.getStringExtra("score");         // On recupere les valeurs passees par Game
        mdate = data.getStringExtra("date");

        score.setText("" + mscore);                         // On affiche le score du joueur

        dbAdapater = new MyDBAdapater(this);         // On accede a la base de donnees
        dbAdapater.open();

        ArrayList<Score> listsc = dbAdapater.getAllScores(); // On recupere la liste des scores

        if(listsc.size() >= 1){                                             // Si la liste contient au moins un element,
            int comparaison = mscore.compareTo(listsc.get(0).getScore());   // On compare le premier element de la liste au score du joueur
            if(comparaison < 0){
                affichage.setText("Nouveau record : ");                     // Si le score du joueur est meilleur, on l'affiche
                meilleur.setText("" + mscore);
            }
            else if (comparaison >= 0){
                affichage.setText("Meilleur score :");                      // Sinon, on affiche le meilleur score dans la base de donnees
                meilleur.setText("" + listsc.get(0).getScore());
            }
        }
        else{
            affichage.setText("");                                          // Si la liste est vide on fait disparaitre ces champs
            meilleur.setText("");
        }

        rejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                            // bouton pour acceder a une nouvelle partie
                Intent intent = new Intent(ResultatPos.this, Game.class);
                startActivity(intent);
                finish();
            }
        });

        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                             // bouton pour enregistrer le score, on envoie egalement les donnees sur l'intent de la classe Enregistrer
                Intent intent = new Intent(ResultatPos.this, Enregistrer.class);
                intent.putExtra("score", mscore);
                intent.putExtra("date", mdate);
                startActivity(intent);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                    // bouton pour acceder a l'ecran d'accueil
                Intent intent = new Intent(ResultatPos.this, Accueil.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {                                                            // On ferme la base de donnees lorsqu'on quitte l'activite
        dbAdapater.close();
        super.onDestroy();
    }
}
