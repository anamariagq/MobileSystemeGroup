/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */



package com.example.todoapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Repräsentiert ein Todo-Element innerhalb der Anwendung.
 * Wird als Room-Entity in der Tabelle "todos" gespeichert.
 */
@Entity(tableName = "todos")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;

    private String description;

    private long dueDate;

    private boolean completed;

    private long priorityId;

    /**
     * Gibt die eindeutige ID des Todos zurück.
     *
     * @return Todo-ID
     */
    public long getId() {
        return id;
    }

    /**
     * Setzt die eindeutige ID des Todos.
     *
     * @param id Todo-ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt den Titel des Todos zurück.
     *
     * @return Titel
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Todos.
     *
     * @param title Titel
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gibt die Beschreibung des Todos zurück.
     *
     * @return Beschreibung
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung des Todos.
     *
     * @param description Beschreibung
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gibt das Fälligkeitsdatum des Todos zurück.
     *
     * @return Fälligkeitsdatum als Zeitstempel
     */
    public long getDueDate() {
        return dueDate;
    }

    /**
     * Setzt das Fälligkeitsdatum des Todos.
     *
     * @param dueDate Zeitstempel des Fälligkeitsdatums
     */
    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gibt zurück, ob das Todo abgeschlossen ist.
     *
     * @return {@code true}, wenn abgeschlossen
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Setzt den Abschlussstatus des Todos.
     *
     * @param completed Abschlussstatus
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Gibt die Prioritäts-ID des Todos zurück.
     *
     * @return Prioritäts-ID
     */
    public long getPriorityId() {
        return priorityId;
    }

    /**
     * Setzt die Prioritäts-ID des Todos.
     *
     * @param priorityId Prioritäts-ID
     */
    public void setPriorityId(long priorityId) {
        this.priorityId = priorityId;
    }
}