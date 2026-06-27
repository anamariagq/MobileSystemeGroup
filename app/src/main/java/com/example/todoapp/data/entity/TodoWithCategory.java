/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */



package com.example.todoapp.data.entity;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Projektion für aggregierte Todo-Daten mit zugehörigen Kategorien.
 * Wird als Read-Model für JOIN-Abfragen aus der Datenbank verwendet.
 *
 * Enthält keine Room-Entity-Annotation, da es sich um eine reine
 * Abfrage-Datenstruktur handelt.
 */
public class TodoWithCategory {

    /** ID des Todos */
    public long id;

    /** Titel des Todos */
    public String title;

    /** Due Date for Calendar*/
    public long dueDate;

    /** Aggregierte Kategorienamen (kommagetrennt) */
    public String categoryName;

    /** Aggregierte Icon-Namen der Kategorien (kommagetrennt) */
    public String iconName;
}