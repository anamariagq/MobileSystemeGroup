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
 * Repräsentiert eine Kategorie innerhalb der Todo-Anwendung.
 * Wird als Room-Entity in der Tabelle "categories" gespeichert.
 */
@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private String iconName;

    /**
     * Gibt die eindeutige ID der Kategorie zurück.
     *
     * @return Kategorien-ID
     */
    public long getId() {
        return id;
    }

    /**
     * Setzt die eindeutige ID der Kategorie.
     *
     * @param id Kategorien-ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen der Kategorie zurück.
     *
     * @return Kategoriename
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Kategorie.
     *
     * @param name Kategoriename
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Namen des Icons der Kategorie zurück.
     *
     * @return Icon-Name
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * Setzt den Namen des Icons der Kategorie.
     *
     * @param iconName Icon-Name
     */
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}