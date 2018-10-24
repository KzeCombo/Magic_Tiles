package com.example.combo.piano;


/**
 * Classe Score, qui permet de creer un nouveau score.
 */

public class Score {
    private long id;
    private int classement;
    private String score;
    private String nom;
    private String date;


    public Score(int id, int classement, String score, String nom, String date) {
        this.id = id;
        this.classement = classement;
        this.score = score;
        this.nom = nom;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public int getClassement() { 
        return classement; 
    }

    public String getScore() {
        return score;
    }

    public String getNom() {
        return nom;
    }

    public String getDate() {
        return date;
    }

    public void setClassement(int classement) { 
    }

    public void setId(long id) { 
        this.classement = classement; 
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
