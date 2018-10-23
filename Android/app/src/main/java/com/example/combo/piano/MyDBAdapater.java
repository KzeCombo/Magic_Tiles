package com.example.combo.piano;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Classe MyDBAdapater qui cree la base de donnees scores_dataBase, ainsi que la table_scores qui contient les scores enregistres du jeu
 * Cette table possede 5 colonnes, une photo du joueur, une pour l'identifiant, une pour le score, une autre pour la date et une pour le nom
 * On peut effectuer des insertions de scores, des suppressions, recuperer tous les scores, ou un seul.
 * Lorqu'on recupere la liste de tous les scores, on trie la liste en fonction du score, et on peut ainsi recuperer le rang.
 */

public class MyDBAdapater {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "scores_dataBase.db";

    private static final String TABLE_SCORES = "table_scores";
    public static final String COL_ID = "_id";
    public static final String COL_SCORE = "score";
    public static final String COL_DATE = "date";
    public static final String COL_NOM = "nom";

    private static final String CREATE_DB =
            "create table " + TABLE_SCORES + " ("
                    + COL_ID + " integer primary key autoincrement, "
                    + COL_SCORE + " text not null, "
                    + COL_NOM + " text not null, "
                    + COL_DATE + " text not null);";

    private SQLiteDatabase mDB;
    private MyOpenHelper myOpenHelper;

    public MyDBAdapater(Context context){
        myOpenHelper = new MyOpenHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void open() { mDB = myOpenHelper.getWritableDatabase();}

    public void close() {mDB.close();}

    public Score getScore(long id) throws SQLException{
        Score score = null;
        Cursor c = mDB.query(TABLE_SCORES,
                new String[] {COL_ID, COL_SCORE, COL_NOM, COL_DATE},
                COL_ID + " = " + id, null, null, null, null);

        if (c.getCount() > 0){
            c.moveToFirst();
            score = new Score(c.getInt(0), c.getPosition(), c.getString(1), c.getString(2), c.getString(3));
        }
        c.close();

        return score;
    }

    public ArrayList<Score> getAllScores(){
        ArrayList<Score> scores = new ArrayList<Score>();

        Cursor c = mDB.query(TABLE_SCORES,
                new String[] {COL_ID, COL_SCORE, COL_NOM, COL_DATE},
                null, null, null, null, COL_SCORE + " ASC");
        c.moveToFirst();
        while (!c.isAfterLast()){
            scores.add(new Score(c.getInt(0), c.getPosition() + 1, c.getString(1),c.getString(2), c.getString(3)));
            c.moveToNext();
        }
        c.close();

        return scores;
    }

    public long insertScore(String score, String date, String nom){
        ContentValues values = new ContentValues();
        values.put(COL_SCORE, score);
        values.put(COL_DATE, date);
        values.put(COL_NOM, nom);
        return mDB.insert(TABLE_SCORES, null, values);
    }


    public int removeScore(long aid){
        return mDB.delete(TABLE_SCORES, COL_ID + "=?", new String[] {Long.toString(aid)});
    }

    private class MyOpenHelper extends SQLiteOpenHelper{
        public MyOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory cursorFactory, int version){
            super(context, name, cursorFactory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table " + TABLE_SCORES + ";");
            onCreate(db);
        }
    }
}
