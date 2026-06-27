/**
 @author Laura Mohni
 Matrikelnr. 20623
 Bearbeitungszeitraum: 30.03.-31.05.2026
 */



package com.example.todoapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.data.entity.TodoWithCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaDoc-Kommentare erstellt durch ein KI-System (GPT).
 * Inhalt wurde anschließend durch einen Menschen geprüft, jedoch nicht neu erstellt.
 *
 * RecyclerView-Adapter zur Darstellung von Todos inklusive zugehöriger Kategorien.
 * Nutzt {@link TodoWithCategory} als Datenquelle.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<TodoWithCategory> todoList = new ArrayList<>();
    private OnTodoClickListener listener;
    private float fontSize = 16f;

    /**
     * Setzt die Liste der Todos und aktualisiert die Anzeige.
     *
     * @param todos Liste von Todos mit Kategorieinformationen
     */
    public void setTodos(List<TodoWithCategory> todos) {
        this.todoList = todos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoWithCategory todo = todoList.get(position);
        holder.title.setText(todo.title);
        holder.title.setTextSize(fontSize);

        if (todo.iconName != null && !todo.iconName.isEmpty()) {
            Context context = holder.itemView.getContext();
            String firstIcon = todo.iconName.split(",")[0].trim();
            int resId = context.getResources().getIdentifier(
                    firstIcon, "drawable", context.getPackageName());

            if (resId != 0) {
                holder.icon.setImageResource(resId);
            } else {
                holder.icon.setImageResource(android.R.drawable.ic_menu_agenda);
            }
        } else {
            holder.icon.setImageResource(android.R.drawable.ic_menu_agenda);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTodoClick(todoList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    /**
     * ViewHolder für einzelne Todo-Einträge.
     */
    static class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView icon;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTodoTitle);
            icon = itemView.findViewById(R.id.ivCategoryIcon);
        }
    }

    /**
     * Callback-Interface für Klickereignisse auf Todos.
     */
    public interface OnTodoClickListener {
        void onTodoClick(TodoWithCategory todo);
    }

    /**
     * Setzt die Schriftgröße für die Todo-Titel.
     *
     * @param size Schriftgröße in SP
     */
    public void setFontSize(float size) {
        this.fontSize = size;
        notifyDataSetChanged();
    }

    /**
     * Gibt das Todo an einer bestimmten Position zurück.
     *
     * @param position Index in der Liste
     * @return TodoWithCategory-Objekt
     */
    public TodoWithCategory getTodoAt(int position) {
        return todoList.get(position);
    }

    /**
     * Setzt den Click-Listener für Todo-Elemente.
     *
     * @param listener Listener für Klickereignisse
     */
    public void setOnTodoClickListener(OnTodoClickListener listener) {
        this.listener = listener;
    }
}