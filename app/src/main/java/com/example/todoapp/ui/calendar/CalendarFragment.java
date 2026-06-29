package com.example.todoapp.ui.calendar;

import android.graphics.Color;
import android.graphics.Typeface;import android.os.Bundle;
import android.view.Gravity;import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;

public class CalendarFragment extends Fragment {
    //private CompactCalendarView compactCalendarView;
    private CalendarView calendarView;
    private CalendarViewModel calendarViewModel;

    private TextView textMonth;
    private LinearLayout todoContainer;

    //Global Variable for selectedDates
    private LocalDate selectedDate = null;



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
        calendarViewModel = new ViewModelProvider(requireActivity()).get(CalendarViewModel.class);

        //Charge current Month on Fragment Open
        YearMonth currentMonth = YearMonth.now();
        calendarViewModel.loadTodosForMonth(currentMonth);
        textMonth.setText(formatMonth(currentMonth));

        //Here is important to keep with the order: 1, 2, 3!
        //1.Day binder(click day)
        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>(){
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view){
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day){

                LocalDate date = day.getDate();
                //Show number of day on TexView
                container.calendarDayText.setText(String.valueOf(day.getDate().getDayOfMonth()));

                //Point when there´s one or more todos on a day
                boolean hasTodos = false;

                //lo adapt dueDate to date --> not null
                long startOfDayMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long endOfDayMillis = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()-1;

                //to obtain todos List of the Month from ViewModel
                List<TodoWithCategory> monthsTodo = calendarViewModel.getTodosForMonth().getValue();

                if(monthsTodo != null){
                    for(TodoWithCategory todo : monthsTodo){
                        //Temporal Log to check for null
                        //android.util.Log.d("CALENDAR_TEST", "Day: " + date + " | Todo Due: " + todo.dueDate + " | Range: " + startOfDayMillis + " at " + endOfDayMillis);
                        if(todo.dueDate >= startOfDayMillis && todo.dueDate <= endOfDayMillis){
                            hasTodos = true;
                            break;
                        }
                    }
                }

                //paint or not a point depending on the result
                if(container.calendarDotIndicator != null) {
                    if (hasTodos) {
                        container.calendarDotIndicator.setVisibility(View.VISIBLE);
                        //to assure dot at the front
                        container.calendarDotIndicator.bringToFront();
                    } else {
                        container.calendarDotIndicator.setVisibility(View.INVISIBLE);
                    }
                }

                container.getView().setOnClickListener(v -> {
                    //Logik to update the month
                    YearMonth yearMonth = YearMonth.from(date);
                    textMonth.setText(formatMonth(yearMonth));
                    selectedDate = date;
                    calendarViewModel.loadTodosForDate(date);
                    //updateUnderCalendarList();


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
        calendarViewModel.getTodosForMonth().observe(getViewLifecycleOwner(), todos -> {//todos
            //Sync with Room Info
            if(todos != null) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    calendarView.notifyCalendarChanged();
                });
            }

        });


        //to find Xml dynamic container for the Calendar days
        LinearLayout titlesContainer = view.findViewById(R.id.titlesContainer);

        DayOfWeek[] daysOfWeeks = DayOfWeek.values();

        for (DayOfWeek dayOfWeek : daysOfWeeks){
            TextView textView = new TextView(getContext());
            //For Days to use same space
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            );
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(Color.DKGRAY);

            //TextStyleShort to extract official abbreviation from system
            String dayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
            textView.setText(dayName);

            //to Add TextView to the thing
            titlesContainer.addView(textView);
        }

        View scrollView = (View) view.findViewById(R.id.todoContainer).getParent();
        if (scrollView != null) {
            scrollView.setOnTouchListener((v, event) -> {
                // Touch to CalendarActivity
                if (getActivity() instanceof CalendarActivity) {
                    ((CalendarActivity) getActivity()).registrerTouch(event);
                }
                //False to not avoid scrolling
                return false;
            });
        }

        return view;
    }

    private String formatMonth(YearMonth yearMonth){
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
        return yearMonth.format(formatter);
    }

    //Container Class for each day and points
    public static class DayViewContainer extends ViewContainer {
        public final TextView calendarDayText;
        public final View calendarDotIndicator;

        public DayViewContainer(View view) {
            super(view);
            this.calendarDayText = view.findViewById(R.id.calendarDayText);
            this.calendarDotIndicator = view.findViewById(R.id.calendarDotIndicator);
        }
    }

    //to show the TodoList of the Day under Calendar
    /*
    private void updateUnderCalendarList(){
        todoContainer.removeAllViews();

        //Obtain the list of all month

        List<TodoWithCategory> monthlyTodo = calendarViewModel.getTodosForMonth().getValue();

        if (monthlyTodo != null && selectedDate != null){
            long startOfDay = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endOfDay = selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;

            for (TodoWithCategory todo : monthlyTodo) {
                // Verificar si la tarea cae dentro de este día
                if (todo.dueDate >= startOfDay && todo.dueDate <= endOfDay) {
                    // Inflar el diseño de la tarea
                    View item = getLayoutInflater().inflate(R.layout.item_todo, todoContainer, false);

                    TextView title = item.findViewById(R.id.tvTodoTitle);
                    title.setText(todo.title);

                    TextView category = item.findViewById(R.id.ivCategoryIcon);
                    category.setText(todo.categoryName);

                    // Insertar el elemento visual debajo del calendario
                    todoContainer.addView(item);
                }
            }

            todoContainer.requestLayout();

        }

    }*/

}