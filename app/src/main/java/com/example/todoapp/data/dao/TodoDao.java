/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */



package com.example.todoapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Update;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todoapp.data.entity.Todo;
import com.example.todoapp.data.entity.TodoWithCategory;
import com.example.todoapp.data.entity.TodoCategoryCrossRef;

import java.util.List;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Data Access Object (DAO) für die Entität {@link Todo}.
 * Enthält CRUD-Operationen sowie Abfragen zur Verwaltung von Todos
 * inklusive Verknüpfungen zu Kategorien über die Join-Tabelle "todo_category".
 *
 * Zusätzlich werden aggregierte Abfragen für die Darstellung von
 * {@link TodoWithCategory} bereitgestellt.
 */
@Dao
public interface TodoDao {

    /**
     * Fügt ein neues Todo in die Datenbank ein.
     *
     * @param todo das einzufügende Todo
     * @return die generierte ID des neu angelegten Datensatzes
     */
    @Insert
    long insert(Todo todo);

    /**
     * Fügt eine Verknüpfung zwischen Todo und Kategorie in die Join-Tabelle ein.
     *
     * @param crossRef die Beziehung zwischen Todo und Kategorie
     */
    @Insert
    void insertTodoCategoryRef(TodoCategoryCrossRef crossRef);

    /**
     * Löscht ein Todo aus der Datenbank.
     *
     * @param todo das zu löschende Todo
     */
    @Delete
    void delete(Todo todo);

    /**
     * Aktualisiert ein bestehendes Todo.
     *
     * @param todo das zu aktualisierende Todo
     */
    @Update
    void update(Todo todo);

    /**
     * Gibt alle Todos sortiert nach Titel zurück.
     *
     * @return Liste aller Todos
     */
    @Query("SELECT * FROM todos ORDER BY title")
    List<Todo> getAll();

    /**
     * Gibt ein einzelnes Todo anhand der ID zurück.
     *
     * @param id eindeutige ID des Todos
     * @return Todo oder {@code null}, falls kein Eintrag existiert
     */
    @Query("SELECT * FROM todos WHERE id = :id")
    Todo getById(long id);

    /**
     * Gibt alle Todos inklusive zugehöriger Kategorien zurück.
     * Kategorien werden aggregiert (kommagetrennt) dargestellt.
     *
     * @return Liste von {@link TodoWithCategory}
     */
    @Query(
            "SELECT todos.id, todos.title, todos.dueDate, " +
                    "GROUP_CONCAT(categories.name, ', ') AS categoryName, " +
                    "GROUP_CONCAT(categories.iconName, ', ') AS iconName " +
                    "FROM todos " +
                    "LEFT JOIN todo_category ON todos.id = todo_category.todoId " +
                    "LEFT JOIN categories ON categories.id = todo_category.categoryId " +
                    "GROUP BY todos.id " +
                    "ORDER BY todos.title"
    )
    List<TodoWithCategory> getTodosWithCategory();

    /**
     * Gibt alle Todos inklusive Kategorien zurück, sortiert nach Fälligkeitsdatum.
     *
     * @return Liste von {@link TodoWithCategory}
     */
    @Query(
            "SELECT todos.id, todos.title, todos.dueDate, " +
                    "GROUP_CONCAT(categories.name, ', ') AS categoryName, " +
                    "GROUP_CONCAT(categories.iconName, ', ') AS iconName " +
                    "FROM todos " +
                    "LEFT JOIN todo_category ON todos.id = todo_category.todoId " +
                    "LEFT JOIN categories ON categories.id = todo_category.categoryId " +
                    "GROUP BY todos.id " +
                    "ORDER BY todos.dueDate"
    )
    List<TodoWithCategory> getTodosWithCategorySortedByDate();

    /**
     * Gibt alle Todos inklusive Kategorien zurück, sortiert nach Priorität.
     *
     * @return Liste von {@link TodoWithCategory}
     */
    @Query(
            "SELECT todos.id, todos.title, todos.dueDate, " +
                    "GROUP_CONCAT(categories.name, ', ') AS categoryName, " +
                    "GROUP_CONCAT(categories.iconName, ', ') AS iconName " +
                    "FROM todos " +
                    "LEFT JOIN todo_category ON todos.id = todo_category.todoId " +
                    "LEFT JOIN categories ON categories.id = todo_category.categoryId " +
                    "GROUP BY todos.id " +
                    "ORDER BY todos.priorityId"
    )
    List<TodoWithCategory> getTodosWithCategorySortedByPriority();

    /**
     * Löscht alle Kategorie-Verknüpfungen eines bestimmten Todos.
     *
     * @param todoId ID des Todos
     */
    @Query("DELETE FROM todo_category WHERE todoId = :todoId")
    void deleteCategoryRefsForTodo(long todoId);

    /**
     * Gibt alle Kategorie-IDs zurück, die einem Todo zugeordnet sind.
     *
     * @param todoId ID des Todos
     * @return Liste der Kategorie-IDs
     */
    @Query("SELECT categoryId FROM todo_category WHERE todoId = :todoId")
    List<Long> getCategoryIdsForTodo(long todoId);

    @Query(
            "SELECT t.id, t.title, t.dueDate, " +
                    "GROUP_CONCAT(c.name, ', ') AS categoryName, " +
                    "GROUP_CONCAT(c.iconName, ', ') AS iconName " +
                    "FROM todos t " +
                    "LEFT JOIN todo_category tc ON t.id = tc.todoId " +
                    "LEFT JOIN categories c ON c.id = tc.categoryId " +
                    "WHERE t.dueDate = :date " +
                    "GROUP BY t.id " +
                    "ORDER BY t.title"
    )
    List<TodoWithCategory> getTodosWithCategoryByDate(long date);

    @Query(
            "SELECT t.id, t.title, t.dueDate, " +
                    "GROUP_CONCAT(c.name, ',') AS categoryName, " +
                    "GROUP_CONCAT(c.iconName, ',') AS iconName " +
                    "FROM todos t " +
                    "LEFT JOIN todo_category tc ON t.id = tc.todoId " +
                    "LEFT JOIN categories c ON c.id = tc.categoryId " +
                    "WHERE t.dueDate BETWEEN :start AND :end " +
                    "GROUP BY t.id"
    )
    List<TodoWithCategory> getTodosBetweenDates(long start, long end);



}