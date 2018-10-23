package com.example.combo.piano;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Classe ScoresAdapter qui herite de la classe ArrayAdapater<Score>
 * Elle s'appuie sur le fichier xml cell_layout, et affiche un score en fonction de ce fichier
 */

public class ScoresAdapter extends ArrayAdapter<Score> {

    private final Context context;

    public ScoresAdapter(Context context, ArrayList<Score> scores){
        super(context, R.layout.cell_layout, scores);
        this.context = context;
    }

    public View getView(int position,View convertView, ViewGroup parent) {
        View cellView = convertView;

        if (cellView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cellView = inflater.inflate(R.layout.cell_layout, parent, false);
        }

        TextView dateTv = cellView.findViewById(R.id.dateCell);
        TextView nomTv = cellView.findViewById(R.id.nomCell);
        TextView scoreTv = cellView.findViewById(R.id.scoreCell);
        TextView classementTv = cellView.findViewById(R.id.classementCell);

        Score scoreS = getItem(position);
        String s = scoreS.getScore();
        int classement = scoreS.getClassement();
        String nom = scoreS.getNom();
        String d = scoreS.getDate();

        dateTv.setText("" + d);
        nomTv.setText("" + nom);
        scoreTv.setText("" + s);
        classementTv.setText("" + classement);

        return cellView;
    }

    @Override
    public void add(Score object) {
        super.add(object);
    }

    public void remove(Score object){
        super.remove(object);
    }

}
