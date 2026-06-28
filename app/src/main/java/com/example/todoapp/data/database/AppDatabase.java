/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */


package com.example.todoapp.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.room.Room;

import com.example.todoapp.data.dao.PriorityDao;
import com.example.todoapp.data.dao.CategoryDao;
import com.example.todoapp.data.dao.TodoDao;
import com.example.todoapp.data.entity.Category;
import com.example.todoapp.data.entity.Todo;
import com.example.todoapp.data.entity.Priority;
import com.example.todoapp.data.entity.TodoCategoryCrossRef;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Zentrale Room-Datenbankdefinition der Anwendung.
 * Enthält die Entitäten und stellt den Zugriff auf die DAO-Schnittstellen bereit.
 *
 * Implementiert ein Singleton-Muster zur globalen Nutzung der Datenbankinstanz.
 */
@Database(
        entities = {
                Todo.class,
                Category.class,
                Priority.class,
                TodoCategoryCrossRef.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Zugriff auf Priority-Datenbankoperationen.
     *
     * @return PriorityDao-Instanz
     */
    public abstract PriorityDao priorityDao();

    /**
     * Zugriff auf Category-Datenbankoperationen.
     *
     * @return CategoryDao-Instanz
     */
    public abstract CategoryDao categoryDao();

    /**
     * Zugriff auf Todo-Datenbankoperationen.
     *
     * @return TodoDao-Instanz
     */
    public abstract TodoDao todoDao();

    private static AppDatabase instance;

    /**
     * Liefert die Singleton-Instanz der Datenbank.
     * Wird bei Bedarf initialisiert.
     *
     * @param context Applikationskontext
     * @return globale AppDatabase-Instanz
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "todo_database"
                    ).fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}