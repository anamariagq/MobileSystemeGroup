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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;

public class CalendarFragment extends Fragment {
    //private CompactCalendarView compactCalendarView;
    private CalendarView calendarView;
    private CalendarViewModel calendarViewModel;
    private TextView textMonth;
    private LinearLayout todoContainer;
    //Global Variable for selectedDates by User
    private LocalDate selectedDate = null;

    //Local list to paint the calendar points with no blocking the DB
    private List<TodoWithCategory> currentMonthTodosList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // UI References
        textMonth = view.findViewById(R.id.textMonth);
        todoContainer = view.findViewById(R.id.todoContainer);
        calendarView = view.findViewById(R.id.calendarView);
        LinearLayout titlesContainer = view.findViewById(R.id.titlesContainer);

        // ViewModel Initialization
        calendarViewModel = new ViewModelProvider(requireActivity()).get(CalendarViewModel.class);

        // Charge current Month on Open
        YearMonth currentMonth = YearMonth.now();
        calendarViewModel.loadTodosForMonth(currentMonth);
        textMonth.setText(formatMonth(currentMonth));

        //Day Binder to draw the points and gestion of clicks
        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                LocalDate date = day.getDate();
                container.calendarDayText.setText(String.valueOf(date.getDayOfMonth()));

                // Comprobation of points using the memorylist
                boolean hasTodos = false;
                long startOfDayMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long endOfDayMillis = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;

                for (TodoWithCategory todo : currentMonthTodosList) {
                    if (todo.dueDate >= startOfDayMillis && todo.dueDate <= endOfDayMillis) {
                        hasTodos = true;
                        break;
                    }
                }

                if (hasTodos) {
                    container.calendarDotIndicator.setVisibility(View.VISIBLE);
                    container.calendarDotIndicator.bringToFront();
                } else {
                    container.calendarDotIndicator.setVisibility(View.INVISIBLE);
                }

                // Light click to not break the swipe
                container.getView().setOnClickListener(v -> {
                    YearMonth yearMonth = YearMonth.from(date);
                    textMonth.setText(formatMonth(yearMonth));

                    selectedDate = date;
                    calendarViewModel.loadTodosForDate(date);

                    //To force and redray the underlist
                    renderDailyTodosUnderCalendar();
                });
            }
        });

        //Month Scroll Listener
        calendarView.setMonthScrollListener(month -> {
            YearMonth yearMonth = month.getYearMonth();
            textMonth.setText(formatMonth(yearMonth));
            calendarViewModel.loadTodosForMonth(yearMonth);
            return Unit.INSTANCE;
        });

        //Configure CalendarView Range
        calendarView.setup(
                currentMonth.minusMonths(12),
                currentMonth.plusMonths(12),
                DayOfWeek.MONDAY
        );
        calendarView.scrollToMonth(currentMonth);

        // Observer of Monthly Events for points and Lists
        calendarViewModel.getTodosForMonth().observe(getViewLifecycleOwner(), todos -> {
            if (todos != null) {
                // Save List on for daybinder
                currentMonthTodosList = todos;

                // for notification to draw the points
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    calendarView.notifyCalendarChanged();
                });

                // to update when user already selcted another day
                renderDailyTodosUnderCalendar();
            }
        });

        //Inflate days
        DayOfWeek[] daysOfWeeks = DayOfWeek.values();
        titlesContainer.removeAllViews(); //  Preventiva Cleaning
        for (DayOfWeek dayOfWeek : daysOfWeeks) {
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
            );
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(Color.DKGRAY);

            String dayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
            textView.setText(dayName);
            titlesContainer.addView(textView);
        }

        // 6.Configurate Scroll Intercept for accesability and touch
        View scrollView = (View) view.findViewById(R.id.todoContainer).getParent();
        if (scrollView != null) {
            scrollView.setOnTouchListener((v, event) -> {
                if (getActivity() instanceof CalendarActivity) {
                    ((CalendarActivity) getActivity()).registrerTouch(event);
                }
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    v.performClick();
                }
                return false;
            });
        }
    }

    // Process and print Todos of selected day under the calendar
    private void renderDailyTodosUnderCalendar() {
        todoContainer.removeAllViews();

        if (selectedDate != null && !currentMonthTodosList.isEmpty()) {
            long startOfDay = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endOfDay = selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;

            for (TodoWithCategory todo : currentMonthTodosList) {
                if (todo.dueDate >= startOfDay && todo.dueDate <= endOfDay) {
                    //inflate row desing
                    View item = getLayoutInflater().inflate(R.layout.item_todo, todoContainer, false);
                    //Only Text Title Todo
                    TextView title = item.findViewById(R.id.tvTodoTitle);
                    title.setText(todo.title);

                    //android.widget.ImageView categoryIcon = item.findViewById(R.id.ivCategoryIcon);
                    //category.setText(todo.categoryName);

                    todoContainer.addView(item);
                }
            }
            todoContainer.requestLayout();
        }
    }

    private String formatMonth(YearMonth yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
        return yearMonth.format(formatter);
    }

    // ViewHolder optimized
    public static class DayViewContainer extends ViewContainer {
        public final TextView calendarDayText;
        public final View calendarDotIndicator;

        public DayViewContainer(View view) {
            super(view);
            this.calendarDayText = view.findViewById(R.id.calendarDayText);
            this.calendarDotIndicator = view.findViewById(R.id.calendarDotIndicator);
        }
    }
}




