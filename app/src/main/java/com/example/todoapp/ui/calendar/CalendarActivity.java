package com.example.todoapp.ui.calendar;

import android.os.Bundle;import android.view.GestureDetector;import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todoapp.R;

public class CalendarActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.calendarContainer, new CalendarFragment())
                    .commit();
        }

        //To inizialize detector swipe
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int MIN_DIST = 120; // pixel of swiping
            private static final int MAX_VEL = 150; //swiping velocity

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;

                float difX = e2.getX() - e1.getX();
                float difY = e2.getY() - e1.getY();

                // Verificar si el movimiento fue predominantemente horizontal
                if (Math.abs(difX) > Math.abs(difY)) {
                    // Deslizamiento de Izquierda a Derecha (Swipe Right) para ir atrás
                    if (difX > MIN_DIST && Math.abs(velocityX) > MAX_VEL) {
                        closeScreenWithAnimation();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    //Public method for detecting the ScrollView
    public void registrerTouch(MotionEvent event) {
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
    }

    private void closeScreenWithAnimation() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }



}

