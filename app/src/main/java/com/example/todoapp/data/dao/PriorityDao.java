/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */


package com.example.todoapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.data.entity.Priority;

import java.util.List;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Data Access Object (DAO) für die Entität {@link Priority}.
 * Stellt CRUD-Operationen sowie Abfragen für die Tabelle "priorities" bereit.
 */
@Dao
public interface PriorityDao {

    /**
     * Fügt eine neue Priorität in die Datenbank ein.
     *
     * @param priority die einzufügende Priorität
     * @return die generierte ID des neuen Datensatzes
     */
    @Insert
    long insert(Priority priority);

    /**
     * Aktualisiert eine bestehende Priorität in der Datenbank.
     *
     * @param priority die zu aktualisierende Priorität
     */
    @Update
    void update(Priority priority);

    /**
     * Löscht eine Priorität aus der Datenbank.
     *
     * @param priority die zu löschende Priorität
     */
    @Delete
    void delete(Priority priority);

    /**
     * Gibt alle Prioritäten aus der Datenbank zurück, sortiert nach Name.
     *
     * @return Liste aller Prioritäten
     */
    @Query("SELECT * FROM priorities ORDER BY name")
    List<Priority> getAll();
}