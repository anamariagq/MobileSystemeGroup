/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */



/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Activity zur Verwaltung und Anzeige aller Todos.
 * Bietet eine Listenansicht mit Bearbeitungs- und Löschfunktionen.
 *
 * Funktionen:
 * - Anzeige aller Todos inklusive Kategorie-Informationen
 * - Navigation zur Detailansicht beim Klick auf ein Todo
 * - Swipe-Geste zum Löschen von Einträgen
 * - Laden der Daten aus der lokalen Datenbank
 *
 * Datenzugriff:
 * - {@link AppDatabase}
 * - {@link TodoWithCategory}
 * - {@link Todo}
 */
package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.data.database.AppDatabase;
import com.example.todoapp.data.entity.Todo;
import com.example.todoapp.data.entity.TodoWithCategory;

import java.util.ArrayList;
import java.util.List;

public class TodoManagementActivity extends AppCompatActivity {

    private RecyclerView rvTodos;
    private AppDatabase database;
    private List<TodoWithCategory> todoList = new ArrayList<>();
    private TodoMgmtAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_management);

        database = AppDatabase.getInstance(this);

        rvTodos = findViewById(R.id.rvTodos);
        adapter = new TodoMgmtAdapter();
        rvTodos.setLayoutManager(new LinearLayoutManager(this));
        rvTodos.setAdapter(adapter);

        // Klick öffnet DetailActivity zum Bearbeiten
        adapter.setOnClickListener(todo -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_TODO_ID, todo.id);
            startActivity(intent);
        });

        // Swipe-to-delete
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
                        TodoWithCategory item = todoList.get(viewHolder.getAdapterPosition());
                        new Thread(() -> {
                            Todo todo = database.todoDao().getById(item.id);
                            if (todo != null) {
                                database.todoDao().delete(todo);
                            }
                            runOnUiThread(() -> loadTodos());
                        }).start();
                    }
                };
        new ItemTouchHelper(callback).attachToRecyclerView(rvTodos);

        loadTodos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodos();
    }

    private void loadTodos() {
        new Thread(() -> {
            todoList = database.todoDao().getTodosWithCategory();
            runOnUiThread(() -> adapter.setTodos(todoList));
        }).start();
    }

    interface OnClickListener {
        void onClick(TodoWithCategory todo);
    }

    // Inline Adapter
    class TodoMgmtAdapter extends RecyclerView.Adapter<TodoMgmtAdapter.ViewHolder> {

        private OnClickListener listener;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TodoWithCategory todo = todoList.get(position);
            holder.text.setText(todo.title);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(todo);
            });
        }

        @Override
        public int getItemCount() {
            return todoList.size();
        }

        void setTodos(List<TodoWithCategory> list) {
            todoList = list;
            notifyDataSetChanged();
        }

        void setOnClickListener(OnClickListener l) {
            this.listener = l;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}