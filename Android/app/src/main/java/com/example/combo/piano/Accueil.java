package com.example.combo.piano;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * L'activite Accueil permet a l'utilisateur de commencer une nouvelle partie ou d'acceder à la liste des scores.
 * On utilise donc deux boutons, auxquels on associe des actions lorsqu'on appuie sur eux.
 */

public class Accueil extends AppCompatActivity {

    private Button bpartie;
    private Button bmap;
    private Button bscore;

    // Interface_Capteur
    private TextView xTextView;
    private TextView yTextView;
    private TextView zTextView;
    private ToggleButton startstopButton;

    // Gestion des capteurs
    private Sensor mSensor;
    private SensorManager manager;
    private boolean sensorSupported;
    private int selectedSensor;
    private int selectedFrequency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        bpartie = findViewById(R.id.bnouvelle_partie);                                        // Le bouton pour une nouvelle partie
        bmap = findViewById(R.id.bmap);
        bscore = findViewById(R.id.bscore);                                                  // Le bouton pour acceder à la liste des scores

        bpartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accueil.this, Game.class);           // On cree une instance de la classe Intent pour passer de la classe Accueil à la classe Game
                startActivity(intent);
                finish();
            }
        });

        bmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accueil.this, MapsActivity.class);           // On cree une instance de la classe Intent pour passer de la classe Accueil à la classe MapsActivity
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

        // onCreate capteur

        manager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);

        xTextView = (TextView) findViewById(R.id.x_textview);
        yTextView = (TextView) findViewById(R.id.y_textview);
        zTextView = (TextView) findViewById(R.id.z_textview);

        startstopButton = (ToggleButton) findViewById(R.id.startstop_button);
        startstopButton.setChecked(false);

        Spinner sensorSpinner = (Spinner) findViewById(R.id.sensor_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> sensorAdapter = ArrayAdapter.createFromResource(this,
                R.array.sensors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sensorSpinner.setAdapter(sensorAdapter);
        sensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                        selectedSensor = Sensor.TYPE_ACCELEROMETER;
                        break;
                    case 1:
                        selectedSensor = Sensor.TYPE_GYROSCOPE;
                        break;
                    default:
                        selectedSensor = Sensor.TYPE_MAGNETIC_FIELD;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner freqencySpinner = (Spinner) findViewById(R.id.frequency_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.frequencies, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        freqencySpinner.setAdapter(frequencyAdapter);
        freqencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos) {
                    case 0:
                        selectedFrequency = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case 1:
                        selectedFrequency = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 2:
                        selectedFrequency = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    default:
                        selectedFrequency = SensorManager.SENSOR_DELAY_FASTEST;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }//fin_onCreate

    // onPause Capteur
    @Override
    public void onPause() {
        super.onPause();
        if (startstopButton.isChecked() && sensorSupported) {
            startstopButton.setChecked(false);
            manager.unregisterListener((SensorEventListener) this, mSensor);
        }
    }

    // Appui sur le bouton Start/Stop
    public void startStopCapture(View v) {
        if (startstopButton.isChecked()) {
            mSensor = manager.getDefaultSensor(selectedSensor);
            //sensorSupported = manager.registerListener((SensorEventListener) this, mSensor, selectedFrequency);
            xTextView.setText(R.string.x_eq);
            yTextView.setText(R.string.y_eq);
            zTextView.setText(R.string.z_eq);
            if (!sensorSupported)
                Toast.makeText(this, R.string.unsupported, Toast.LENGTH_LONG).show();
        } else if (sensorSupported) {
            manager.unregisterListener((SensorEventListener) this, mSensor);
        }
    }


    /*****************************************************
     * Implémentation de l'interface SensorEventListener *
     *****************************************************/


    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                xTextView.setText(String.format("x = %f m/s^2", event.values[0]));
                yTextView.setText(String.format("y = %f m/s^2", event.values[1]));
                zTextView.setText(String.format("z = %f m/s^2", event.values[2]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                xTextView.setText(String.format("x = %f rad/s", event.values[0]));
                yTextView.setText(String.format("y = %f rad/s", event.values[1]));
                zTextView.setText(String.format("z = %f rad/s", event.values[2]));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                xTextView.setText(String.format("x = %f µT", event.values[0]));
                yTextView.setText(String.format("y = %f µT", event.values[1]));
                zTextView.setText(String.format("z = %f µT", event.values[2]));
                break;
        }
    }


    //Fin_gestion Capteur

}//fin_class
