package com.example.joinn.mypagefragment;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.example.joinn.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CalendarViewHolder> {
    private ArrayList<Date> dayList;
    private String selectedDate;

    public CustomAdapter() {
        this.dayList = dayList;
        this.selectedDate = null;
    }

    public void setSelectedDate(String date) {
        this.selectedDate = date;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        Date monthDate = dayList.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);

        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        String formattedDate = formatDate(monthDate);

        // 선택한 날짜와 다른 날짜를 다르게 스타일링합니다.
        if (selectedDate != null && selectedDate.equals(formattedDate)) {
            // 선택한 날짜의 스타일
            holder.dayText.setTextColor(Color.WHITE);
            holder.dayText.setBackgroundResource(R.drawable.circle_background); // 동그라미 그리기 또는 다른 스타일 적용
        } else {
            // 다른 날짜의 스타일
            holder.dayText.setTextColor(Color.BLACK);
            holder.dayText.setBackgroundResource(0); // 동그라미 제거 또는 다른 스타일 적용
        }

        holder.dayText.setText(String.valueOf(displayDay));
    }


    @Override
    public int getItemCount() {
        return dayList.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;

        CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
        }
    }

    // Date를 문자열로 변환하는 메서드
    private String formatDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
