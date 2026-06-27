/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */


package com.example.todoapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.data.entity.Category;

import java.util.List;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Data Access Object (DAO) für die Entität {@link Category}.
 * Definiert Datenbankoperationen für den Zugriff auf die Tabelle "categories"
 * innerhalb der Room-Persistenzschicht.
 */
@Dao
public interface CategoryDao {

    /**
     * Fügt eine neue Kategorie in die Datenbank ein.
     *
     * @param category die einzufügende Kategorie
     * @return die automatisch generierte ID des neu eingefügten Datensatzes
     */
    @Insert
    long insert(Category category);

    /**
     * Aktualisiert eine bestehende Kategorie in der Datenbank.
     *
     * @param category die zu aktualisierende Kategorie
     */
    @Update
    void update(Category category);

    /**
     * Löscht eine Kategorie aus der Datenbank.
     *
     * @param category die zu löschende Kategorie
     */
    @Delete
    void delete(Category category);

    /**
     * Gibt alle Kategorien aus der Datenbank zurück, sortiert nach Name.
     *
     * @return Liste aller Kategorien
     */
    @Query("SELECT * FROM categories ORDER BY name")
    List<Category> getAll();

    /**
     * Gibt eine Kategorie anhand ihrer ID zurück.
     *
     * @param id eindeutige ID der Kategorie
     * @return Kategorie-Objekt oder {@code null}, falls kein Eintrag existiert
     */
    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Category getById(long id);
}