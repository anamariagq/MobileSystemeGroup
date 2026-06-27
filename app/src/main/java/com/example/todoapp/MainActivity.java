/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */



/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Haupt-Activity der Todo-Anwendung.
 * Verantwortlich für:
 * - Anzeige einer Liste von Todos in einem RecyclerView
 * - Navigation zu Detail-, Einstellungs- und Verwaltungsbildschirmen
 * - Löschen von Todos per Swipe-Geste
 * - Sortierung der Todo-Liste nach Titel, Datum oder Priorität
 * - Laden und Anwenden von Benutzereinstellungen (z. B. Schriftgröße)
 *
 * Verwendet:
 * - {@link RecyclerView} zur Darstellung der Liste
 * - {@link TodoAdapter} als Adapter für Todo-Daten
 * - {@link AppDatabase} als lokale Datenquelle
 * - {@link TodoWithCategory} als kombinierte Datenstruktur
 */
package com.example.todoapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.data.database.AppDatabase;
import com.example.todoapp.data.entity.TodoWithCategory;
import com.example.todoapp.ui.adapter.TodoAdapter;
import com.example.todoapp.data.entity.Todo;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.widget.ImageButton;

import androidx.preference.PreferenceManager;

import com.example.todoapp.ui.calendar.CalendarActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.ItemTouchHelper;


import java.util.List;



public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTodos;
    private TodoAdapter adapter;
    private FloatingActionButton fabAddTodo;

    private ImageButton btnCalendar;
    private AppDatabase database;
    private float fontSize = 16f;
    private String currentSort = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fabAddTodo = findViewById(R.id.fabAddTodo);

        fabAddTodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            startActivity(intent);
        });

        btnCalendar = findViewById(R.id.btnCalendar);

        btnCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        });

        recyclerViewTodos = findViewById(R.id.recyclerViewTools);

        adapter = new TodoAdapter();
        database = AppDatabase.getInstance(this);

        adapter.setOnTodoClickListener(todo -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    DetailActivity.class
            );

            intent.putExtra(
                    DetailActivity.EXTRA_TODO_ID,
                    todo.id
            );

            startActivity(intent);
        });


        recyclerViewTodos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewTodos.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        int position;
                        position = viewHolder.getAdapterPosition();
                        TodoWithCategory todo = adapter.getTodoAt(position);

                        new Thread(() -> {

                            Todo todoEntity = database.todoDao().getById(todo.id);
                            if (todoEntity != null) {
                                database.todoDao().delete(todoEntity);
                            }

                            runOnUiThread(() -> {
                                loadTodos();
                            });
                        }).start();

                    }
                };
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerViewTodos);




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void loadTodos() {
        new Thread(() -> {
            List<TodoWithCategory> todoList;
            switch (currentSort) {
                case "date":
                    todoList = database.todoDao().getTodosWithCategorySortedByDate();
                    break;
                case "priority":
                    todoList = database.todoDao().getTodosWithCategorySortedByPriority();
                    break;
                default:
                    todoList = database.todoDao().getTodosWithCategory();
                    break;
            }
            runOnUiThread(() -> adapter.setTodos(todoList));
        }).start();
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        if (item.getItemId() == R.id.menu_categories) {
            Intent intent = new Intent(this, CategoryManagementActivity.class);
            startActivity(intent);

            return true;
        }

        if (item.getItemId() == R.id.menu_priorities) {
            Intent intent = new Intent(this, PriorityManagementActivity.class);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.sort_title) {
            currentSort = "title";
            loadTodos();
            return true;
        }
        if (item.getItemId() == R.id.sort_date) {
            currentSort = "date";
            loadTodos();
            return true;
        }
        if (item.getItemId() == R.id.sort_priority) {
            currentSort = "priority";
            loadTodos();
            return true;
        }

        if (item.getItemId() == R.id.menu_todos) {
            Intent intent = new Intent(this, TodoManagementActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadSettings() {

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        fontSize = preferences.getInt("font_size", 16);

        if (adapter != null) {
            adapter.setFontSize(fontSize);
        }
    }






    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
        loadTodos();
    }


}