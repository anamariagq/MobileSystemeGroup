/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */



package com.example.todoapp.data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Repräsentiert die Many-to-Many-Verknüpfung zwischen {@link com.example.todoapp.data.entity.Todo}
 * und {@link com.example.todoapp.data.entity.Category}.
 *
 * Wird als Junction-Tabelle "todo_category" in Room gespeichert.
 */
@Entity(
        tableName = "todo_category",
        primaryKeys = {"todoId", "categoryId"}
)
public class TodoCategoryCrossRef {

    private long todoId;
    private long categoryId;

    /**
     * Leerer Konstruktor, von Room ignoriert.
     */
    @Ignore
    public TodoCategoryCrossRef() {}

    /**
     * Erstellt eine Verknüpfung zwischen einem Todo und einer Kategorie.
     *
     * @param todoId ID des Todos
     * @param categoryId ID der Kategorie
     */
    public TodoCategoryCrossRef(long todoId, long categoryId) {
        this.todoId = todoId;
        this.categoryId = categoryId;
    }

    /**
     * Gibt die ID des Todos zurück.
     *
     * @return Todo-ID
     */
    public long getTodoId() {
        return todoId;
    }

    /**
     * Setzt die ID des Todos.
     *
     * @param todoId Todo-ID
     */
    public void setTodoId(long todoId) {
        this.todoId = todoId;
    }

    /**
     * Gibt die ID der Kategorie zurück.
     *
     * @return Kategorie-ID
     */
    public long getCategoryId() {
        return categoryId;
    }

    /**
     * Setzt die ID der Kategorie.
     *
     * @param categoryId Kategorie-ID
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}