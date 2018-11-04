package com.example.combo.piano;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * L'activite Game decrit les actions effectuees pendant toute la duree d'une partie.
 * Elle contient 16 boutons pour simuler 16 touches de piano.
 * Seuls les boutons de la derniere ligne (celle qui est tout en bas de l'écran) sont cliquables.
 * Un chronometre est demarre a chaque debut de partie.
 * Il existe un togglebutton pour changer le mode de jeu (normal ou zarbi)
 */

public class Game extends AppCompatActivity{

    private MediaPlayer gameOverSong;
    private MediaPlayer gameEndSong;


    private final int NB_TUILES_TOTAL = 20;                         // Le nombre de touches au total dans la partie

    private Button case01;                                          // Liste des touches
    private Button case02;
    private Button case03;
    private Button case04;
    private Button case11;
    private Button case12;
    private Button case13;
    private Button case14;
    private Button case21;
    private Button case22;
    private Button case23;
    private Button case24;
    private Button case31;
    private Button case32;
    private Button case33;
    private Button case34;

    private ToggleButton onOff;                                     // Le bouton pour changer de mode
    private TextView mode;                                          // La legende du bouton onOff, qui disparait des que la partie est commencee

    private com.example.combo.piano.Chronometer chrono;

    private int random0;                                            // 4 valeurs aléatoires (initiales) pour chaque ligne de boutons, afin d'en colorier un en noir aleatoirement
    private int random1;
    private int random2;
    private int random3;

    private int ligne0;                                             // 4 valeurs pour chaque ligne pour savoir quel bouton de la ligne du dessus est noir afin d'effectuer les changements de ligne
    private int ligne1;
    private int ligne2;
    private int ligne3;

    private int nbtuiles;                                           // Le nombre de touches courant

    private boolean start = false;                                  // Une valeur booleene pour savoir si la partie a commence ou non
    private long valeur_chrono;                                     // Une variable pour stocker la valeur (la base pour setBase()) du chronometre lorsqu'on passe du mode paysage au mode portrait et inversement

    private String score;                                           // Une variable contenant la valeur affichee par le chronometre

    private boolean zarbimode;                                      // Une valeur booleene pour savoir dans quel mode on se trouve


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        case01 = findViewById(R.id.case01);                         // Association des composants du fichier xml aux variables
        case02 = findViewById(R.id.case02);
        case03 = findViewById(R.id.case03);
        case04 = findViewById(R.id.case04);

        case11 = findViewById(R.id.case11);
        case12 = findViewById(R.id.case12);
        case13 = findViewById(R.id.case13);
        case14 = findViewById(R.id.case14);

        case21 = findViewById(R.id.case21);
        case22 = findViewById(R.id.case22);
        case23 = findViewById(R.id.case23);
        case24 = findViewById(R.id.case24);

        case31 = findViewById(R.id.case31);
        case32 = findViewById(R.id.case32);
        case33 = findViewById(R.id.case33);
        case34 = findViewById(R.id.case34);

        onOff = findViewById(R.id.onOff);
        mode = findViewById(R.id.mode);

        chrono = findViewById(R.id.chrono);

        if(savedInstanceState != null){                                         // Si on a change de mode (paysage ou portrait), on recupere toutes les valeurs neccesaires pour continuer la partie la ou elle etait
            random0 = savedInstanceState.getInt("random0");
            random1 = savedInstanceState.getInt("random1");
            random2 = savedInstanceState.getInt("random2");
            random3 = savedInstanceState.getInt("random3");
            ligne0 = savedInstanceState.getInt("etat_ligne0");
            ligne1 = savedInstanceState.getInt("etat_ligne1");
            ligne2 = savedInstanceState.getInt("etat_ligne2");
            ligne3 = savedInstanceState.getInt("etat_ligne3");
            valeur_chrono = savedInstanceState.getLong("etat_chrono");
            nbtuiles = savedInstanceState.getInt("nb_tuiles");                                                          // utilise pour connaitre l'avancement de la partie
            zarbimode = savedInstanceState.getBoolean("etat_mode");
        }
        else {                                                                                                              // Si on vient de lancer une nouvelle partie, on genere nous-memes des valeurs aleatoires pour les touches noires
            random0 = (int) ((Math.random() * 4) + 1);
            random1 = (int) ((Math.random() * 4) + 1);
            random2 = (int) ((Math.random() * 4) + 1);
            random3 = (int) ((Math.random() * 4) + 1);
            nbtuiles = NB_TUILES_TOTAL;                                                                                     // On affecte a nbtuiles le nombre initial de touches
        }

        if(nbtuiles == NB_TUILES_TOTAL){                                                                                    // Si on n'a pas encore commence la partie, on utilise les valeurs des random pour l'initialisation
            if(onOff.isChecked()){ initialisation(random0, random1, random2, random3, Color.WHITE, Color.BLACK); }          // initialisation en fonction du mode
            else{ initialisation(random0, random1, random2, random3, Color.BLACK, Color.WHITE); }
        }
        else{                                                                                                               // Si on a deja commence la partie,
            onOff.setVisibility(View.INVISIBLE);                                                                            // on empeche le joueur de modifier la valeur du mode en cachant le bouton
            mode.setVisibility(View.INVISIBLE);                                                                             // et sa legende
            chrono.setBase(valeur_chrono);                                                                                  // on redemarre le chronometre sur la derniere valeur affichee avant de changer de mode (portrait ou paysage)
            chrono.start();
            start = true;                                                                                                   // on signale que la partie est commencee
            if(zarbimode){ initialisation(ligne0, ligne1, ligne2,ligne3, Color.WHITE, Color.BLACK); }                       // et on fait l'initialisation en fonction des valeurs des lignes, et en fonction du mode (normal ou zarbi)
            else{ initialisation(ligne0, ligne1, ligne2,ligne3, Color.BLACK, Color.WHITE); }
            if(nbtuiles <= 4){                                                                                              // Si on est a la fin de la partie, on lance la fonction finDefilement en fonction du mode
                if(zarbimode){ finDefilement(nbtuiles + 1, Color.BLACK); }
                else { finDefilement(nbtuiles + 1, Color.WHITE); }
            }
        }

        onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {                                                            // Lorsqu'on appuie sur le bouton onOff, on verifie que la partie n'est pas encore commencee
                if(b && !start){ initialisation(random0, random1, random2, random3, Color.WHITE, Color.BLACK); }            // Si elle a commence, on ne fait rien (de toute facon, le bouton est invisible dans ce cas)
                else if(!b && !start){ initialisation(random0, random1, random2, random3, Color.BLACK, Color.WHITE); }      // Sinon, on inverse les couleurs.
                else{ }
            }
        });

        case01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                                                // Pour chaque bouton de la ligne 0, on va associer la meme serie d'actions
                if(!start){                                                                                                 // Si la partie n'a pas commence, et qu'on appuie sur cette case
                    start = true;                                                                                           // On signale que la partie a commence
                    chrono.setBase(SystemClock.elapsedRealtime());                                                          // On demarre le chronometre
                    chrono.start();
                    onOff.setVisibility(View.INVISIBLE);                                                                    // et on le rend invisible
                    mode.setVisibility(View.INVISIBLE);
                }
                if (ligne0 == 1) {                                                                                          // Si la touche est noire (mode normal), (ou blanche en mode zarbi)
                    if(onOff.isChecked()) {changementLigne(Color.WHITE, Color.BLACK); }                                     // On change de ligne, le jeu continue
                    else{ changementLigne(Color.BLACK, Color.WHITE); }
                    if(nbtuiles <= 4){                                                                                      // si on arrive a la fin du jeu, on lance la fonction finDefilement
                        if(onOff.isChecked()){ finDefilement(nbtuiles, Color.BLACK); }
                        else{ finDefilement(nbtuiles, Color.WHITE); }
                    }
                    nbtuiles--;                                                                                             // et on decremente le nombre de touches
                }
                else{                                                                                                       // Si le joueur s'est trompe
                    case01.setBackgroundColor(Color.RED);                                                              // On le signale en coloriant la touche en rouge
                    playGameOver();
                    chrono.stop();                                                                                          // On arrete le chronometre
                    Intent intent = new Intent(Game.this, ResultatNeg.class);                                  // On cree une nouvelle instance de la classe Intent de la classe Game vers ResultatNeg
                    startActivity(intent);
                    finish();
                }
            }
        });

        case02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                                                // idem
                if(!start){
                    start = true;
                    chrono.setBase(SystemClock.elapsedRealtime());
                    chrono.start();
                    onOff.setVisibility(View.INVISIBLE);
                    mode.setVisibility(View.INVISIBLE);
                }
                if (ligne0 == 2) {
                    if(onOff.isChecked()) { changementLigne(Color.WHITE, Color.BLACK); }
                    else{ changementLigne(Color.BLACK, Color.WHITE); }
                    if(nbtuiles <= 4){
                        if(onOff.isChecked()){ finDefilement(nbtuiles, Color.BLACK); }
                        else{ finDefilement(nbtuiles, Color.WHITE); }
                    }
                    nbtuiles--;
                }
                else{
                    case02.setBackgroundColor(Color.RED);
                    playGameOver();
                    chrono.stop();
                    Intent intent = new Intent(Game.this, ResultatNeg.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        case03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                                                 // idem
                if(!start){
                    start = true;
                    chrono.setBase(SystemClock.elapsedRealtime());
                    chrono.start();
                    onOff.setVisibility(View.INVISIBLE);
                    mode.setVisibility(View.INVISIBLE);
                }
                if (ligne0 == 3) {
                    if(onOff.isChecked()) { changementLigne(Color.WHITE, Color.BLACK); }
                    else{ changementLigne(Color.BLACK, Color.WHITE); }
                    if(nbtuiles <= 4){
                        if(onOff.isChecked()){ finDefilement(nbtuiles, Color.BLACK); }
                        else{ finDefilement(nbtuiles, Color.WHITE); }
                    }
                    nbtuiles--;
                }
                else{
                    case03.setBackgroundColor(Color.RED);
                    playGameOver();
                    chrono.stop();
                    Intent intent = new Intent(Game.this, ResultatNeg.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        case04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                                                  // idem
                if(!start){
                    start = true;
                    chrono.setBase(SystemClock.elapsedRealtime());
                    chrono.start();
                    onOff.setVisibility(View.INVISIBLE);
                    mode.setVisibility(View.INVISIBLE);
                }
                if (ligne0 == 4) {
                    if(onOff.isChecked()) { changementLigne(Color.WHITE, Color.BLACK); }
                    else{ changementLigne(Color.BLACK, Color.WHITE); }
                    if(nbtuiles <= 4){
                        if(onOff.isChecked()){ finDefilement(nbtuiles, Color.BLACK); }
                        else{ finDefilement(nbtuiles, Color.WHITE); }
                    }
                    nbtuiles--;
                }
                else{
                    case04.setBackgroundColor(Color.RED);
                    playGameOver();
                    chrono.stop();
                    Intent intent = new Intent(Game.this, ResultatNeg.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /* Fonction initialisation pour initialiser les touches
    * Elle prend en parametres la valeur de chaque ligne (comprise entre 1 et 4 pour designer quel bouton est en noir ou blanc selon le mode),
    * Ainsi que deux couleurs (pour le mode)*/

    protected void initialisation(int l1, int l2, int l3, int l4, int c1, int c2){
        switch (l1){                                        // Cas sur la ligne 0
            case 1:                                         // Si l1 = 0
                case01.setBackgroundColor(c1);              // On colore la case01 en noir (mode normal) ou blanc et le reste en blanc (resp. noir)
                case02.setBackgroundColor(c2);              // et on met la valeur 1 dans la ligne0 pour savoir quel bouton est en noir (ou en blanc)
                case03.setBackgroundColor(c2);
                case04.setBackgroundColor(c2);
                ligne0 = 1;
                break;
            case 2:                                         // pareil mais pour la case02
                case02.setBackgroundColor(c1);
                case01.setBackgroundColor(c2);
                case03.setBackgroundColor(c2);
                case04.setBackgroundColor(c2);
                ligne0 = 2;
                break;
            case 3:                                         // pour la case03
                case03.setBackgroundColor(c1);
                case01.setBackgroundColor(c2);
                case02.setBackgroundColor(c2);
                case04.setBackgroundColor(c2);
                ligne0 = 3;
                break;
            case 4:                                         // pour la case04
                case04.setBackgroundColor(c1);
                case01.setBackgroundColor(c2);
                case02.setBackgroundColor(c2);
                case03.setBackgroundColor(c2);
                ligne0 = 4;
                break;
        }
        switch (l2){                                        // meme chose pour la ligne 1
            case 1:
                case11.setBackgroundColor(c1);
                case12.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                ligne1 = 1;
                break;
            case 2:
                case12.setBackgroundColor(c1);
                case11.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                ligne1 = 2;
                break;
            case 3:
                case13.setBackgroundColor(c1);
                case11.setBackgroundColor(c2);
                case12.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                ligne1 = 3;
                break;
            case 4:
                case14.setBackgroundColor(c1);
                case11.setBackgroundColor(c2);
                case12.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                ligne1 = 4;
                break;
        }
        switch (l3){                                        // idem ligne 2
            case 1:
                case21.setBackgroundColor(c1);
                case22.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                ligne2 = 1;
                break;
            case 2:
                case22.setBackgroundColor(c1);
                case21.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                ligne2 = 2;
                break;
            case 3:
                case23.setBackgroundColor(c1);
                case21.setBackgroundColor(c2);
                case22.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                ligne2 = 3;
                break;
            case 4:
                case24.setBackgroundColor(c1);
                case21.setBackgroundColor(c2);
                case22.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                ligne2 = 4;
                break;
        }
        switch (l4){                                    // idem ligne 3
            case 1:
                case31.setBackgroundColor(c1);
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                ligne3 = 1;
                break;
            case 2:
                case32.setBackgroundColor(c1);
                case31.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                ligne3 = 2;
                break;
            case 3:
                case33.setBackgroundColor(c1);
                case31.setBackgroundColor(c2);
                case32.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                ligne3 = 3;
                break;
            case 4:
                case34.setBackgroundColor(c1);
                case31.setBackgroundColor(c2);
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                ligne3 = 4;
                break;
        }
    }

    /* Fonction changementLigne pour faire defiler les touches, qui prend en parametre deux couleurs pour le mode
     * Meme fonctionnement que initialisation, sauf qu'on genere un nouveau nombre aleatoire pour la ligne du haut */

    protected void changementLigne(int c1, int c2){
        switch (ligne1){
            case 1:
                case01.setBackgroundColor(c1);
                case02.setBackgroundColor(c2);
                case03.setBackgroundColor(c2);
                case04.setBackgroundColor(c2);
                playDo();
                ligne0 = 1;
                break;
            case 2:
                case02.setBackgroundColor(c1);
                case01.setBackgroundColor(c2);
                case03.setBackgroundColor(c2);
                case04.setBackgroundColor(c2);
                playRe();
                ligne0 = 2;
                break;
            case 3:
                case03.setBackgroundColor(c1);
                case01.setBackgroundColor(c2);
                case02.setBackgroundColor(c2);
                case04.setBackgroundColor(c2);
                playMi();
                ligne0 = 3;
                break;
            case 4:
                case04.setBackgroundColor(c1);
                case01.setBackgroundColor(c2);
                case02.setBackgroundColor(c2);
                case03.setBackgroundColor(c2);
                playFa();
                ligne0 = 4;
                break;
        }
        switch (ligne2){
            case 1:
                case11.setBackgroundColor(c1);
                case12.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                playSol();
                ligne1 = 1;
                break;
            case 2:
                case12.setBackgroundColor(c1);
                case11.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                playLa();
                ligne1 = 2;
                break;
            case 3:
                case13.setBackgroundColor(c1);
                case11.setBackgroundColor(c2);
                case12.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                playSi();
                ligne1 = 3;
                break;
            case 4:
                case14.setBackgroundColor(c1);
                case11.setBackgroundColor(c2);
                case12.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                playDo();
                ligne1 = 4;
                break;
        }
        switch (ligne3){
            case 1:
                case21.setBackgroundColor(c1);
                case22.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                playMi();
                ligne2 = 1;
                break;
            case 2:
                case22.setBackgroundColor(c1);
                case21.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                playFa();
                ligne2 = 2;
                break;
            case 3:
                case23.setBackgroundColor(c1);
                case21.setBackgroundColor(c2);
                case22.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                playSol();
                ligne2 = 3;
                break;
            case 4:
                case24.setBackgroundColor(c1);
                case21.setBackgroundColor(c2);
                case22.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                playLa();
                ligne2 = 4;
                break;
        }

        random3 = (int) ((Math.random() * 4) + 1);
        switch (random3){
            case 1:
                case31.setBackgroundColor(c1);
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                playSi();
                ligne3 = 1;
                break;
            case 2:
                case32.setBackgroundColor(c1);
                case31.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                playDo();
                ligne3 = 2;
                break;
            case 3:
                case33.setBackgroundColor(c1);
                case31.setBackgroundColor(c2);
                case32.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                playRe();
                ligne3 = 3;
                break;
            case 4:
                case34.setBackgroundColor(c1);
                case31.setBackgroundColor(c2);
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                playMi();
                ligne3 = 4;
                break;
        }
    }

    /* Fonction finDefilement pour colorier toutes les touches en blanc (mode normal) (ou noir mode zarbi)
    *  en fonction de l'avancement dans la partie */

    protected void finDefilement(int nblignesrestantes, int c2){
        switch (nblignesrestantes){
            case 4:                                         // S'il reste 4 touches lorsqu'on l'appelle, on va colorier la derniere ligne en haut en blanc (ou noir)
                case31.setBackgroundColor(c2);
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                playFa();
                break;
            case 3:
                case31.setBackgroundColor(c2);              // S'il en reste 3, les deux dernières lignes seront coloriees en blanc (ou noir)
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                case21.setBackgroundColor(c2);
                case22.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                playSol();
                break;
            case 2:
                case31.setBackgroundColor(c2);              // Et ainsi de suite ...
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                case21.setBackgroundColor(c2);
                case22.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                case11.setBackgroundColor(c2);
                case12.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                playLa();
                break;
            case 1:
                case31.setBackgroundColor(c2);
                case32.setBackgroundColor(c2);
                case33.setBackgroundColor(c2);
                case34.setBackgroundColor(c2);
                case21.setBackgroundColor(c2);
                case22.setBackgroundColor(c2);
                case23.setBackgroundColor(c2);
                case24.setBackgroundColor(c2);
                case11.setBackgroundColor(c2);
                case12.setBackgroundColor(c2);
                case13.setBackgroundColor(c2);
                case14.setBackgroundColor(c2);
                case01.setBackgroundColor(c2);
                case02.setBackgroundColor(c2);
                case03.setBackgroundColor(c2);
                case04.setBackgroundColor(c2);
                playSi();
                chrono.stop();                                                          // On arrete le chrono
                score = chrono.getText().toString();                                    // Et on recupere la valeur affichee par le chronometre
                Intent intent = new Intent(Game.this, ResultatPos.class);  // On passe ensuite ces donnees dans l'intent pour les recuperer sur l'activite ResultatPos
                intent.putExtra("score", score);
                DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                intent.putExtra("date", dtf.format(new Date()).toString());
                playGameEnd();                                                          // On joue la musique de fin de jeu
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {                                // On recupere ici les donnees pour les utiliser lorsqu'on change de mode (paysage ou portrait)
        outState.putInt("etat_ligne0", ligne0);
        outState.putInt("etat_ligne1", ligne1);
        outState.putInt("etat_ligne2", ligne2);
        outState.putInt("etat_ligne3", ligne3);
        outState.putInt("random0", random0);
        outState.putInt("random1", random1);
        outState.putInt("random2", random2);
        outState.putInt("random3", random3);
        outState.putInt("nb_tuiles", nbtuiles);
        outState.putLong("etat_chrono", chrono.getBase());
        outState.putBoolean("etat_mode", onOff.isChecked());

        super.onSaveInstanceState(outState);
    }
    // Music
    public void playDo(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.song_do);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playRe(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.re);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playMi(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.mi);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playFa(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.fa);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playSol(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.sol);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playLa(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.la);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playSi(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.si);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playGameOver(){
        stopGameSong();
        gameOverSong = MediaPlayer.create(this, R.raw.game_over);
        gameOverSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameOverSong.start();
    }

    public void playGameEnd(){
         stopGameSong();
        gameEndSong = MediaPlayer.create(this, R.raw.rooftop_run_modern);
        gameEndSong.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopGameSong();
                    }
                });
        gameEndSong.start();
    }

    public void stopGameSong(){
        if(gameOverSong != null){
            gameOverSong.release();
            gameOverSong = null;
        }
        else if(gameEndSong != null){
            gameEndSong.release();
            gameEndSong = null;
        }
    }
}
