/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */




/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * ContentProvider zur read-only Bereitstellung von Todo- und Priority-Daten.
 * Ermöglicht den Zugriff auf die lokale Room-Datenbank über URIs.
 *
 * Unterstützte Ressourcen:
 * - todos (Liste und einzelne Einträge)
 * - priorities (Liste und einzelne Einträge)
 *
 * Implementierte Operationen:
 * - query() für Lesezugriffe
 *
 * Nicht unterstützt:
 * - insert()
 * - update()
 * - delete()
 *
 * Datenquelle:
 * - {@link AppDatabase} über SQLite OpenHelper
 * - Zugriff erfolgt direkt über SQL-Queries
 */
package com.example.todoapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todoapp.data.database.AppDatabase;

public class TodoContentProvider extends ContentProvider {

    private static final int TODOS = 100;
    private static final int TODO_ID = 101;
    private static final int PRIORITIES = 200;
    private static final int PRIORITY_ID = 201;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(TodoContract.AUTHORITY, "todos", TODOS);
        uriMatcher.addURI(TodoContract.AUTHORITY, "todos/#", TODO_ID);
        uriMatcher.addURI(TodoContract.AUTHORITY, "priorities", PRIORITIES);
        uriMatcher.addURI(TodoContract.AUTHORITY, "priorities/#", PRIORITY_ID);
    }

    private SupportSQLiteDatabase db;

    @Override
    public boolean onCreate() {
        db = AppDatabase.getInstance(getContext()).getOpenHelper().getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case TODOS:
                return db.query("SELECT * FROM todos");
            case TODO_ID:
                return db.query("SELECT * FROM todos WHERE id = ?",
                        new String[]{uri.getLastPathSegment()});
            case PRIORITIES:
                return db.query("SELECT * FROM priorities");
            case PRIORITY_ID:
                return db.query("SELECT * FROM priorities WHERE id = ?",
                        new String[]{uri.getLastPathSegment()});
            default:
                throw new IllegalArgumentException("Unbekannte URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Insert nicht unterstützt");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete nicht unterstützt");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update nicht unterstützt");
    }
}