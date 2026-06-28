/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */




/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Activity zur Darstellung der Anwendungseinstellungen.
 * Hostet ein {@link PreferenceFragmentCompat}, das die Einstellungen aus
 * einer XML-Resource lädt und anzeigt.
 *
 * Aufbau:
 * - {@link SettingsFragment} als Fragment zur Anzeige der Preferences
 * - Nutzung von {@code R.xml.preferences} als Konfigurationsquelle
 *
 * Zweck:
 * Zentrale Konfigurationsoberfläche für App-Einstellungen.
 */
package com.example.todoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
}