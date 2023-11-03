package com.example.joinn.mypagefragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateFragment extends Fragment{


    private TextView monthYearText;

    RecyclerView recyclerView;

    ArrayList<String> carpoolDates;

    private TextView selectedDateText;

    private TextView backBtn;


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

        selectedDateText = view.findViewById(R.id.selectedDateText);

        recyclerView = view.findViewById(R.id.recyclerView);

        CalendarUtil.selectedDate = Calendar.getInstance();

        carpoolDates = new ArrayList<>();

        backBtn = view.findViewById(R.id.back_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                Fragment newFragment = new MyPageFragment();

                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference carpoolRef = database.getReference("carpoolPlans").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        carpoolRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carpoolDates.clear(); // 기존 데이터를 제거
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getValue(String.class);
                    carpoolDates.add(date);
                }
                // 데이터가 변경되면 달력 뷰를 갱신해야 할 수도 있습니다.
                setMonthView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리
            }
        });


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


        CalendarAdapter adapter = new CalendarAdapter(dayList, carpoolDates);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext().getApplicationContext(),7);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        adapter.setOnDateClickListener(new CalendarAdapter.OnDateClickListener() {
            @Override
            public void onDateClicked(String date, boolean hasCarpool) {
                TextView selectedDateText = getView().findViewById(R.id.selectedDateText);
                if (hasCarpool) {
                    selectedDateText.setText(date + " - 카풀이 존재");
                } else {
                    selectedDateText.setText(date + " - 카풀 없음");
                }
                selectedDateText.setVisibility(View.VISIBLE);
            }
        });
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