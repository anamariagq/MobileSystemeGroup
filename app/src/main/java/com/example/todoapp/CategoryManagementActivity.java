/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.data.database.AppDatabase;
import com.example.todoapp.data.entity.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Activity zur Verwaltung von Kategorien.
 * Unterstützt Erstellen, Bearbeiten, Löschen sowie Icon-Auswahl für Kategorien.
 * Persistenz erfolgt über Room ({@link AppDatabase}).
 */
public class CategoryManagementActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private EditText etCategoryName;
    private Button btnAddCategory;
    private AppDatabase database;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter adapter;

    private final String[] iconOptions = {
            "ic_menu_agenda",
            "ic_menu_call",
            "ic_menu_camera",
            "ic_menu_compass",
            "ic_menu_edit",
            "ic_menu_info_details",
            "ic_menu_mapmode",
            "ic_menu_myplaces",
            "ic_menu_search",
            "ic_menu_today"
    };
    private String selectedIconName = "ic_menu_agenda";

    /**
     * Initialisiert UI, Adapter, Datenbankzugriff und Event-Handler.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        database = AppDatabase.getInstance(this);

        rvCategories = findViewById(R.id.rvCategories);
        etCategoryName = findViewById(R.id.etCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);

        adapter = new CategoryAdapter();
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(adapter);

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
                        Category toDelete = categoryList.get(viewHolder.getAdapterPosition());
                        new Thread(() -> {
                            database.categoryDao().delete(toDelete);
                            runOnUiThread(() -> loadCategories());
                        }).start();
                    }
                };
        new ItemTouchHelper(callback).attachToRecyclerView(rvCategories);

        btnAddCategory.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString().trim();
            if (name.isEmpty()) return;

            showIconPickerDialog(() -> {
                new Thread(() -> {
                    Category category = new Category();
                    category.setName(name);
                    category.setIconName(selectedIconName);
                    database.categoryDao().insert(category);
                    runOnUiThread(() -> {
                        etCategoryName.setText("");
                        loadCategories();
                    });
                }).start();
            });
        });

        loadCategories();
    }

    /**
     * Öffnet einen Dialog zur Auswahl eines Icons.
     *
     * @param onIconSelected Callback nach Auswahl
     */
    private void showIconPickerDialog(Runnable onIconSelected) {
        new AlertDialog.Builder(this)
                .setTitle("Icon auswählen")
                .setItems(iconOptions, (dialog, which) -> {
                    selectedIconName = iconOptions[which];
                    onIconSelected.run();
                })
                .show();
    }

    /**
     * Lädt alle Kategorien aus der Datenbank und aktualisiert die UI.
     */
    private void loadCategories() {
        new Thread(() -> {
            categoryList = database.categoryDao().getAll();
            runOnUiThread(() -> adapter.setCategories(categoryList));
        }).start();
    }

    /**
     * Öffnet einen Dialog zur Bearbeitung einer Kategorie.
     *
     * @param category zu bearbeitende Kategorie
     */
    private void showEditDialog(Category category) {
        EditText input = new EditText(this);
        input.setText(category.getName());

        new AlertDialog.Builder(this)
                .setTitle("Kategorie bearbeiten")
                .setView(input)
                .setPositiveButton("Speichern", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) return;

                    showIconPickerDialog(() -> {
                        new Thread(() -> {
                            category.setName(newName);
                            category.setIconName(selectedIconName);
                            database.categoryDao().update(category);
                            runOnUiThread(() -> loadCategories());
                        }).start();
                    });
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    /**
     * RecyclerView Adapter für die Darstellung von Kategorien.
     */
    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.text.setText(categoryList.get(position).getName());
            holder.itemView.setOnClickListener(v -> showEditDialog(categoryList.get(position)));
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        /**
         * Setzt die Datenliste der Kategorien.
         *
         * @param list neue Kategorienliste
         */
        void setCategories(List<Category> list) {
            categoryList = list;
            notifyDataSetChanged();
        }

        /**
         * ViewHolder für Kategorie-Einträge.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}