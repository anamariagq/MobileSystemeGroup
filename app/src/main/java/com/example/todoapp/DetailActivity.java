/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */




package com.example.todoapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.media.MediaPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.data.database.AppDatabase;
import com.example.todoapp.data.entity.Todo;
import com.example.todoapp.data.entity.Priority;
import com.example.todoapp.data.entity.Category;
import com.example.todoapp.data.entity.TodoCategoryCrossRef;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * Detailansicht für das Erstellen, Bearbeiten und Löschen eines Todos.
 * Enthält Logik für Prioritäten, Kategorien, Fälligkeitsdatum und Abschlussstatus.
 * Persistenz erfolgt über Room via {@link AppDatabase}.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_TODO_ID = "todo_id";
    private long todoId = -1;
    private EditText etTitle, etDescription;
    private Button btnSave, btnDelete, btnSelectDate;
    private AppDatabase database;
    private Todo currentTodo;
    private CheckBox cbCompleted;
    private TextView tvDueDate;
    private Spinner spPriority;
    private long selectedDueDate = System.currentTimeMillis();
    private ListView lvCategories;
    private ArrayList<Long> selectedCategoryIds = new ArrayList<>();
    private List<Priority> priorityList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    /**
     * Initialisiert UI, lädt Daten aus der Datenbank und setzt Event-Handler.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        database = AppDatabase.getInstance(this);

        // todoId zuerst auslesen
        if (getIntent().hasExtra(EXTRA_TODO_ID)) {
            todoId = getIntent().getLongExtra(EXTRA_TODO_ID, -1);
        }

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        cbCompleted = findViewById(R.id.cbCompleted);
        tvDueDate = findViewById(R.id.tvDueDate);
        spPriority = findViewById(R.id.spPriority);
        lvCategories = findViewById(R.id.lvCategories);
        btnSelectDate = findViewById(R.id.btnSelectDate);

        updateDueDateText();

        // Kategorien laden
        new Thread(() -> {
            categoryList = database.categoryDao().getAll();
            List<String> names = new ArrayList<>();
            for (Category c : categoryList) {
                names.add(c.getName());
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_multiple_choice,
                        names
                );
                lvCategories.setAdapter(adapter);
                lvCategories.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            });
        }).start();

        // Prioritäten laden
        new Thread(() -> {
            priorityList = database.priorityDao().getAll();
            List<String> names = new ArrayList<>();
            for (Priority p : priorityList) {
                names.add(p.getName());
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        names
                );
                adapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item
                );
                spPriority.setAdapter(adapter);
            });
        }).start();

        // Speichern
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String description = etDescription.getText().toString();
            boolean isCompleted = cbCompleted.isChecked();

            new Thread(() -> {
                Todo todo = new Todo();
                todo.setTitle(title);
                todo.setDescription(description);
                todo.setCompleted(isCompleted);
                todo.setDueDate(selectedDueDate);

                if (priorityList.isEmpty()) {
                    return;
                }

                Priority selectedPriority =
                        priorityList.get(spPriority.getSelectedItemPosition());
                todo.setPriorityId(selectedPriority.getId());

                long savedTodoId;

                if (todoId == -1) {
                    savedTodoId = database.todoDao().insert(todo);
                } else {
                    todo.setId(todoId);
                    database.todoDao().update(todo);
                    savedTodoId = todoId;
                }

                currentTodo = todo;
                currentTodo.setId(savedTodoId);

                // Alte Kategorie-Verknüpfungen löschen
                database.todoDao().deleteCategoryRefsForTodo(savedTodoId);

                selectedCategoryIds.clear();

                for (int i = 0; i < lvCategories.getCount(); i++) {
                    if (lvCategories.isItemChecked(i)) {
                        selectedCategoryIds.add(categoryList.get(i).getId());
                    }
                }

                for (Long categoryId : selectedCategoryIds) {
                    TodoCategoryCrossRef crossRef =
                            new TodoCategoryCrossRef(savedTodoId, categoryId);
                    database.todoDao().insertTodoCategoryRef(crossRef);
                }

                runOnUiThread(() -> {
                    if (isCompleted) {
                        playJingle();
                    }
                    finish();
                });

            }).start();
        });

        // Löschen
        btnDelete.setOnClickListener(v -> {
            if (todoId == -1) {
                finish();
                return;
            }
            new Thread(() -> {
                if (currentTodo != null) {
                    database.todoDao().delete(currentTodo);
                }
                runOnUiThread(this::finish);
            }).start();
        });

        // Datum auswählen
        btnSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedDueDate);

            DatePickerDialog dialog = new DatePickerDialog(
                    DetailActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar selected = Calendar.getInstance();
                        selected.set(year, month, dayOfMonth);
                        selectedDueDate = selected.getTimeInMillis();
                        updateDueDateText();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        if (todoId != -1) {
            new Thread(() -> {
                Todo todo = database.todoDao().getById(todoId);
                currentTodo = todo;
                List<Long> assignedCategoryIds =
                        database.todoDao().getCategoryIdsForTodo(todoId);

                runOnUiThread(() -> {
                    selectedDueDate = todo.getDueDate();
                    etTitle.setText(todo.getTitle());
                    etDescription.setText(todo.getDescription());
                    cbCompleted.setChecked(todo.isCompleted());
                    updateDueDateText();

                    for (int i = 0; i < priorityList.size(); i++) {
                        if (priorityList.get(i).getId() == todo.getPriorityId()) {
                            spPriority.setSelection(i);
                            break;
                        }
                    }

                    // Bereits zugewiesene Kategorien vorauswählen
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (assignedCategoryIds.contains(categoryList.get(i).getId())) {
                            lvCategories.setItemChecked(i, true);
                        }
                    }
                });
            }).start();
        }
    }

    /**
     * Spielt einen Abschluss-Sound ab, wenn ein Todo als erledigt markiert wird.
     */
    private void playJingle() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.jingle);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }

    /**
     * Aktualisiert die Anzeige des Fälligkeitsdatums im UI.
     */
    private void updateDueDateText() {
        SimpleDateFormat formatter =
                new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        tvDueDate.setText(
                formatter.format(new Date(selectedDueDate))
        );
    }
}