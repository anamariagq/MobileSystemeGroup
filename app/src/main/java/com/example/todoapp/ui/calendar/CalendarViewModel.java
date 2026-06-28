package com.example.todoapp.ui.calendar;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.todoapp.data.dao.TodoDao;
import com.example.todoapp.data.database.AppDatabase;
import com.example.todoapp.data.entity.Todo;
import com.example.todoapp.data.entity.TodoWithCategory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.Executors;


public class CalendarViewModel extends AndroidViewModel {


    //constructor
    private final MutableLiveData<List<TodoWithCategory>> todosForSelectedDate =
            new MutableLiveData<>();
    private final MutableLiveData<List<TodoWithCategory>> todosForMonth =
            new MutableLiveData<>();
    public CalendarViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<TodoWithCategory>> getTodosForSelectedDate() {
        return todosForSelectedDate;
    }
    public LiveData<List<TodoWithCategory>> getTodosForMonth() {
        return todosForMonth;
    }

    //Load Todos for a Day
    public void loadTodosForDate(LocalDate date) {
        // Ejecutar en background
        Executors.newSingleThreadExecutor().execute(() -> {
            long millis = date.atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            TodoDao dao = AppDatabase.getInstance(getApplication()).todoDao();
            List<TodoWithCategory> todos = dao.getTodosWithCategoryByDate(millis);

            todosForSelectedDate.postValue(todos);
        });
    }
    public void loadTodosForMonth(YearMonth yearMonth){
        Executors.newSingleThreadExecutor().execute(() -> {
            long startMillis = yearMonth.atDay(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
            long endMillis = yearMonth.atEndOfMonth()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            TodoDao dao = AppDatabase.getInstance(getApplication()).todoDao();
            List<TodoWithCategory> todos = dao.getTodosBetweenDates(startMillis, endMillis);

            todosForMonth.postValue(todos);
        });

    }


}
