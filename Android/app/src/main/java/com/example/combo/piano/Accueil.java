package com.example.combo.piano;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * L'activite Accueil permet a l'utilisateur de commencer une nouvelle partie ou d'acceder à la liste des scores.
 * On utilise donc deux boutons, auxquels on associe des actions lorsqu'on appuie sur eux.
 */

public class Accueil extends AppCompatActivity {

    private Button bpartie;
    private Button bscore;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        bpartie = findViewById(R.id.bnouvelle_partie);                                       // Le bouton pour une nouvelle partie
        bscore = findViewById(R.id.bscore);                                                  // Le bouton pour acceder à la liste des scores

        bpartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accueil.this, Game.class);           // On cree une instance de la classe Intent pour passer de la classe Accueil à la classe Game
                startActivity(intent);
                finish();
            }
        });

        bscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accueil.this, ListeScores.class);    // On cree une instance de la classe Intent pour passer de la classe Accueil à la classe ListeScores
                startActivity(intent);
                finish();
            }
        });

    }//fin_onCreate

    public void stap() {                                                                       // Mettre en pause lecteur
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }
}
