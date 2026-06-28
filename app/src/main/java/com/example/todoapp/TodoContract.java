/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */




/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Contract-Klasse zur Definition der ContentProvider-Struktur.
 * Enthält Tabellen-, Spalten- und URI-Definitionen für den externen Datenzugriff.
 *
 * Struktur:
 * - {@link TodoEntry} beschreibt die Tabelle "todos"
 * - {@link PriorityEntry} beschreibt die Tabelle "priorities"
 *
 * Zweck:
 * Zentrale Definition von Schema- und Content-URIs für die Datenbankkommunikation.
 */
package com.example.todoapp;

import android.net.Uri;
import android.provider.BaseColumns;

public class TodoContract {

    public static final String AUTHORITY = "de.host.mobsys.todo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todos";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();


        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATETIME = "datetime";
        public static final String COLUMN_PRIORITY_ID = "priority_id";
    }

    public static class PriorityEntry implements BaseColumns {
        public static final String TABLE_NAME = "priorities";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();


        public static final String COLUMN_NAME = "name";
    }
}