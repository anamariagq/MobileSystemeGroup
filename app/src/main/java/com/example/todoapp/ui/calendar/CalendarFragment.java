package com.example.todoapp.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.R;
import com.example.todoapp.data.entity.TodoWithCategory;


import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder; // Reemplaza al antiguo DayBinder
import com.kizitonwose.calendar.view.ViewContainer;
//import com.kizitonwose.calendar.view.CalendarView;
//import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
//import com.kizitonwose.calendar.view.DayBinder;
//import com.kizitonwose.calendar.view.ui.ViewContainer;




//import com.github.sundeepk.compactcalendarview.CompactCalendarView;
//import com.github.sundeepk.compactcalendarview.domain.Event;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import org.intellij.lang.annotations.JdkConstants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
//import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
//import java.util.Date;
import java.util.Locale;

import kotlin.Unit;

public class CalendarFragment extends Fragment {
    //private CompactCalendarView compactCalendarView;
    private CalendarView calendarView;
    private CalendarViewModel calendarViewModel;

    private TextView textMonth;
    private LinearLayout todoContainer;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        //UI Calendar References
        textMonth = view.findViewById(R.id.textMonth);
        todoContainer = view.findViewById(R.id.todoContainer);
        calendarView = view.findViewById(R.id.calendarView);

        //Viewmodel Initialize
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        //Charge current Month on Fragment Open
        YearMonth currentMonth = YearMonth.now();
        calendarViewModel.loadTodosForMonth(currentMonth);
        textMonth.setText(formatMonth(currentMonth));

        //1.Day binder(click day)
        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>(){
            @Override
            public DayViewContainer create(View view){
                return new DayViewContainer(view);
            }
            @Override
            public void bind(DayViewContainer container, CalendarDay day){
                container.textView.setText(String.valueOf(day.getDate().getDayOfMonth()));

                container.getView().setOnClickListener(v -> {
                    LocalDate date = day.getDate();
                    YearMonth yearMonth = YearMonth.from(date);
                    textMonth.setText(formatMonth(yearMonth));
                    calendarViewModel.loadTodosForDate(date);
                });
            }
        });

        //2.Month scroll listener
        calendarView.setMonthScrollListener(month-> {
            YearMonth yearMonth = month.getYearMonth();
            textMonth.setText(formatMonth(yearMonth));
            calendarViewModel.loadTodosForMonth(yearMonth);

            return Unit.INSTANCE;
        });
        //3.Configure CalendarView
        calendarView.setup(
                currentMonth.minusMonths(12),
                currentMonth.plusMonths(12),
                DayOfWeek.MONDAY
        );
        calendarView.scrollToMonth(currentMonth);

        //Observer monthly events
        calendarViewModel.getTodosForMonth().observe(getViewLifecycleOwner(), todos -> {
            //Para trabajar despues
        });

        //Observer daily todos
        calendarViewModel.getTodosForSelectedDate().observe(getViewLifecycleOwner(), todos ->
        {
            todoContainer.removeAllViews();

            for (TodoWithCategory todo : todos) {
                View item = getLayoutInflater().inflate(R.layout.item_todo, todoContainer, false);

                TextView title = item.findViewById(R.id.tvTodoTitle);
                title.setText(todo.title);

                TextView category = item.findViewById(R.id.ivCategoryIcon);
                category.setText(todo.categoryName);

                todoContainer.addView(item);
            }
        });

        return view;
    }

    private String formatMonth(YearMonth yearMonth){
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
        return yearMonth.format(formatter);
    }

    //Container for each day
    public static class DayViewContainer extends ViewContainer {
        TextView textView;

        public DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.calendarDayText);
        }
    }
}