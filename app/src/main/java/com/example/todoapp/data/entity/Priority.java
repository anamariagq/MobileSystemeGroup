package com.example.todoapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Repräsentiert eine Priorität innerhalb der Todo-Anwendung.
 * Wird als Room-Entity in der Tabelle "priorities" gespeichert.
 */
@Entity(tableName = "priorities")
public class Priority {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    /**
     * Gibt die eindeutige ID der Priorität zurück.
     *
     * @return Prioritäts-ID
     */
    public long getId() {
        return id;
    }

    /**
     * Setzt die eindeutige ID der Priorität.
     *
     * @param id Prioritäts-ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen der Priorität zurück.
     *
     * @return Prioritätsname
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Priorität.
     *
     * @param name Prioritätsname
     */
    public void setName(String name) {
        this.name = name;
    }
}