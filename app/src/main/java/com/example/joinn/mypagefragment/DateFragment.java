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

    Calendar calendar;
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;

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


//        calendarView = view.findViewById(R.id.calendarView);
//        diaryTextView =  view.findViewById(R.id.diaryTextView);
//        save_Btn = view.findViewById(R.id.save_Btn);
//        del_Btn =  view.findViewById(R.id.del_Btn);
//        cha_Btn =  view.findViewById(R.id.cha_Btn);
//        textView2 =  view.findViewById(R.id.textView2);
//        textView3 =  view.findViewById(R.id.textView3);
//        contextEditText = view.findViewById(R.id.contextEditText);
//
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year,
//                                            int month, int dayOfMonth) {
//
//                diaryTextView.setVisibility(View.VISIBLE);
//                save_Btn.setVisibility(View.VISIBLE);
//                contextEditText.setVisibility(View.VISIBLE);
//                textView2.setVisibility(View.INVISIBLE);
//                cha_Btn.setVisibility(View.INVISIBLE);
//                del_Btn.setVisibility(View.INVISIBLE);
//                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
//                contextEditText.setText("");
//                checkDay(year, month, dayOfMonth);
//
//            }
//        });
//
//        save_Btn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                saveDiary(readDay);
//                str = contextEditText.getText().toString();
//                textView2.setText(str);
//                save_Btn.setVisibility(View.INVISIBLE);
//                cha_Btn.setVisibility(View.VISIBLE);
//                del_Btn.setVisibility(View.VISIBLE);
//                contextEditText.setVisibility(View.INVISIBLE);
//                textView2.setVisibility(View.VISIBLE);
//
//            }
//        });




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


//    public void checkDay(int cYear, int cMonth, int cDay)
//    {
//        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
//
//
//        try
//        {
//            FileInputStream fis = getContext().openFileInput(readDay);
//
//            byte[] fileData = new byte[fis.available()];
//            fis.read(fileData);
//            fis.close();
//
//            str = new String(fileData);
//
//            contextEditText.setVisibility(View.INVISIBLE);
//            textView2.setVisibility(View.VISIBLE);
//            textView2.setText(str);
//
//            save_Btn.setVisibility(View.INVISIBLE);
//            cha_Btn.setVisibility(View.VISIBLE);
//            del_Btn.setVisibility(View.VISIBLE);
//
//            cha_Btn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    contextEditText.setVisibility(View.VISIBLE);
//                    textView2.setVisibility(View.INVISIBLE);
//                    contextEditText.setText(str);
//
//                    save_Btn.setVisibility(View.VISIBLE);
//                    cha_Btn.setVisibility(View.INVISIBLE);
//                    del_Btn.setVisibility(View.INVISIBLE);
//                    textView2.setText(contextEditText.getText());
//                }
//
//            });
//            del_Btn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    textView2.setVisibility(View.INVISIBLE);
//                    contextEditText.setText("");
//                    contextEditText.setVisibility(View.VISIBLE);
//                    save_Btn.setVisibility(View.VISIBLE);
//                    cha_Btn.setVisibility(View.INVISIBLE);
//                    del_Btn.setVisibility(View.INVISIBLE);
//                    removeDiary(readDay);
//                }
//            });
//            if (textView2.getText() == null)
//            {
//                textView2.setVisibility(View.INVISIBLE);
//                diaryTextView.setVisibility(View.VISIBLE);
//                save_Btn.setVisibility(View.VISIBLE);
//                cha_Btn.setVisibility(View.INVISIBLE);
//                del_Btn.setVisibility(View.INVISIBLE);
//                contextEditText.setVisibility(View.VISIBLE);
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    @SuppressLint("WrongConstant")
//    public void removeDiary(String readDay)
//    {
//
//
//        try
//        {
//            FileOutputStream fos = getContext().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
//
//            String content = "";
//            fos.write((content).getBytes());
//            fos.close();
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @SuppressLint("WrongConstant")
//    public void saveDiary(String readDay)
//    {
//
//        try
//        {
//            FileOutputStream fos = getContext().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
//            String content = contextEditText.getText().toString();
//            fos.write((content).getBytes());
//            fos.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}