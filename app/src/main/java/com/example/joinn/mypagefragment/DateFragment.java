package com.example.joinn.mypagefragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.joinn.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateFragment extends Fragment{


    private TextView monthYearText;

    RecyclerView recyclerView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_date, container, false);

        monthYearText = view.findViewById(R.id.monthYearText);
        ImageButton prevBtn = view.findViewById(R.id.pre_btn);
        ImageButton nextBtn = view.findViewById(R.id.next_btn);



        recyclerView = view.findViewById(R.id.recyclerView);

        CalendarUtil.selectedDate = Calendar.getInstance();


        setMonthView();

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtil.selectedDate.add(Calendar.MONTH, -1);
                setMonthView();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtil.selectedDate.add(Calendar.MONTH, 1);
                setMonthView();
            }
        });

        return view;
    }

    private String monthYearFromDate(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;

        String monthYear = month + "월 " + year;

        return monthYear;
    }


    private void setMonthView(){
        monthYearText.setText(monthYearFromDate(CalendarUtil.selectedDate));

        ArrayList<Date> dayList = daysInMonthArray();

        CalendarAdapter adapter = new CalendarAdapter(dayList);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext().getApplicationContext(),7);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Date> daysInMonthArray(){
        ArrayList<Date> dayList = new ArrayList<>();

        Calendar monthCalendar = (Calendar) CalendarUtil.selectedDate.clone();

        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        while (dayList.size() < 42){
            dayList.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }


        return  dayList;
    }


}