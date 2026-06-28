/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */


/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Activity zur Verwaltung von Prioritäten.
 * Ermöglicht das Anzeigen, Hinzufügen, Bearbeiten und Löschen von Priority-Entitäten
 * aus der lokalen Datenbank.
 *
 * Funktionale Bestandteile:
 * - RecyclerView zur Anzeige aller Prioritäten
 * - Eingabefeld zum Erstellen neuer Prioritäten
 * - Swipe-Gesten zum Löschen von Einträgen
 * - Dialog zur Bearbeitung bestehender Prioritäten
 *
 * Datenquelle:
 * - {@link AppDatabase} über {@code priorityDao()}
 * - Entität: {@link Priority}
 */
package com.example.todoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import com.example.todoapp.data.database.AppDatabase;
import com.example.todoapp.data.entity.Priority;

import java.util.ArrayList;
import java.util.List;

public class PriorityManagementActivity extends AppCompatActivity {

    private RecyclerView rvPriorities;
    private EditText etPriorityName;
    private Button btnAddPriority;
    private AppDatabase database;
    private List<Priority> priorityList = new ArrayList<>();
    private PriorityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_management);

        database = AppDatabase.getInstance(this);

        rvPriorities = findViewById(R.id.rvPriorities);
        etPriorityName = findViewById(R.id.etPriorityName);
        btnAddPriority = findViewById(R.id.btnAddPriority);

        adapter = new PriorityAdapter();
        rvPriorities.setLayoutManager(new LinearLayoutManager(this));
        rvPriorities.setAdapter(adapter);

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
                        Priority toDelete = priorityList.get(viewHolder.getAdapterPosition());
                        new Thread(() -> {
                            database.priorityDao().delete(toDelete);
                            runOnUiThread(() -> loadPriorities());
                        }).start();
                    }
                };
        new ItemTouchHelper(callback).attachToRecyclerView(rvPriorities);

        btnAddPriority.setOnClickListener(v -> {
            String name = etPriorityName.getText().toString().trim();
            if (name.isEmpty()) return;
            new Thread(() -> {
                Priority priority = new Priority();
                priority.setName(name);
                database.priorityDao().insert(priority);
                runOnUiThread(() -> {
                    etPriorityName.setText("");
                    loadPriorities();
                });
            }).start();
        });

        loadPriorities();
    }

    private void loadPriorities() {
        new Thread(() -> {
            priorityList = database.priorityDao().getAll();
            runOnUiThread(() -> adapter.setPriorities(priorityList));
        }).start();
    }

    private void showEditDialog(Priority priority) {
        EditText input = new EditText(this);
        input.setText(priority.getName());

        new AlertDialog.Builder(this)
                .setTitle("Priorität bearbeiten")
                .setView(input)
                .setPositiveButton("Speichern", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) return;
                    new Thread(() -> {
                        priority.setName(newName);
                        database.priorityDao().update(priority);
                        runOnUiThread(() -> loadPriorities());
                    }).start();
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    // Inline Adapter
    class PriorityAdapter extends RecyclerView.Adapter<PriorityAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.text.setText(priorityList.get(position).getName());
            holder.itemView.setOnClickListener(v -> showEditDialog(priorityList.get(position)));
        }

        @Override
        public int getItemCount() {
            return priorityList.size();
        }

        void setPriorities(List<Priority> list) {
            priorityList = list;
            notifyDataSetChanged();
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