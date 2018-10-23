package com.example.combo.piano;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * L'activite ListeScores permet d'afficher la liste de tous les scores enregistres dans la base de donnees.
 * Elle utilise la classe MyDBAdapter et ScoresAdapater pour pouvoir recuperer et afficher correctement les scores
 * Elle permet au joueur de supprimer les entrees de la liste grace a un menu contextuel et de revenir a l'ecran principal grace a un menu
 */

public class ListeScores extends AppCompatActivity {

    private ListView listescores;                                               // Listview pour afficher la liste
    private ScoresAdapter adapter;                                              // adapatateur de type ScoresAdapater
    private MyDBAdapater dbAdapater;                                            // la base de donnees

    private String mnom;                                                        // variables pour stocker la valeur des donnees envoyees
    private String mdate;
    private String mscore;

    private boolean enregistreounon = true;                                     // booleen pour savoir si on a deja enregistre ou non et pour eviter d'ajouter une nouvelle entree inutile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        dbAdapater = new MyDBAdapater(this);                            // On ouvre la base de donnees
        dbAdapater.open();

        listescores = findViewById(R.id.list);                                 // On associe la listview a la variable
        registerForContextMenu(findViewById(R.id.list));                       // On fait en sorte qu'elle puisse utiliser le menu contextuel

        adapter = new ScoresAdapter(this, dbAdapater.getAllScores());   // on cree l'adaptateur de la base de donnees

        Intent data = getIntent();                                              // On recupere les valeurs envoyees a l'intent de cette classe
        mscore = data.getStringExtra("score");
        mdate = data.getStringExtra("date");
        mnom = data.getStringExtra("nom");

        if(savedInstanceState != null){}                                        // Si on a deja ajoute une entree, on ne fait rien
        else{                                                                   // Sinon,
            if(mscore != null && mdate != null && mnom != null ){               // Si les valeurs envoyees ne sont pas null,
                long id = dbAdapater.insertScore(mscore,mdate,mnom);            // on ajoute ces valeurs a la base de donnees
                adapter.add(dbAdapater.getScore(id));                           // on ajoute ce nouveau score a l'adaptateur
                adapter.clear();                                                // on rafraichit, pour mettre a jour le classement
                adapter.addAll(dbAdapater.getAllScores());
                adapter.notifyDataSetChanged();
            }
        }
        listescores.setAdapter(adapter);                                        // On associe a la liste l'adaptateur
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) { // on cree un menu contextuel pour pouvoir supprimer les entrees
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {                                                   // Lorsqu'on selectionne un element de la liste
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int index = info.position;
        Score s = adapter.getItem(index);

        switch (item.getItemId()){
            case R.id.action_supprimer:                                                                     // Si on a selectionne ensuite l'action Supprimer:
                adapter.remove(s);                                                                          // on supprime l'element de l'adaptateur
                dbAdapater.removeScore(s.getId());                                                          // on le supprime de la base de donnees
                adapter.clear();                                                                            // et on met a jour la liste pour le classement
                adapter.addAll(dbAdapater.getAllScores());
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                         // on cree un menu pour la barre du haut
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_menu:                                                          // Si on a selectionne l'option Retour au menu principal, on envoie le joueur sur la page d'accueil
                Intent intent = new Intent(ListeScores.this, Accueil.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {                                                            // On ferme la base de donnees lorsqu'on quitte l'activite
        dbAdapater.close();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("enregistreounon", enregistreounon);                            // On recupere l'etat de sauvegarde

        super.onSaveInstanceState(outState);
    }
}
