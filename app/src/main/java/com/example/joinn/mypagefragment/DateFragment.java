package com.example.joinn.mypagefragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.joinn.R;
import java.util.ArrayList;

public class DateFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private DateSelectionListener dateSelectionListener;

    public DateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerView);

        // RecyclerView 설정
        customAdapter = new CustomAdapter();
        recyclerView.setAdapter(customAdapter);

        // CalendarView에서 날짜를 선택할 때 호출되는 리스너 설정
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // 선택한 날짜를 문자열로 변환
                String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);

                // 동그라미를 그리는 함수 호출
                drawCircleOnCalendar(selectedDate);
            }
        });

        return view;
    }

    // 선택한 날짜에 동그라미를 그리는 함수
    public void drawCircleOnCalendar(String selectedDate) {
        // RecyclerView 어댑터에 선택한 날짜를 전달하여 동그라미를 그릴 수 있도록 수정
        if (customAdapter != null) {
            customAdapter.setSelectedDate(selectedDate);
        }

        // 선택한 날짜를 상대방에게 전달
        if (dateSelectionListener != null) {
            dateSelectionListener.onDateSelected(selectedDate);
        }
    }


    public void setDateSelectionListener(DateSelectionListener listener) {
        this.dateSelectionListener = listener;
    }

    public interface DateSelectionListener {
        void onDateSelected(String selectedDate);
    }
}
