package com.example.joinn.mypagefragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinn.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private ArrayList<Date> dayList;
    private Date selectedDate;

    public CalendarAdapter(ArrayList<Date> dayList) {
        this.dayList = dayList;
        this.selectedDate = null;
    }

    public void setSelectedDate(Date date) {
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

        Calendar selectedCalendar = Calendar.getInstance();
        if (selectedDate != null) {
            selectedCalendar.setTime(selectedDate);
        }

        int currentDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = selectedCalendar.get(Calendar.MONTH) + 1;
        int currentYear = selectedCalendar.get(Calendar.YEAR);

        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);

        if (displayMonth == currentMonth && displayYear == currentYear) {
            holder.parentView.setBackgroundColor(Color.parseColor("#D5D5D5"));
            holder.itemView.setBackgroundColor(Color.parseColor("#CEFBC9"));
        } else {
            holder.parentView.setBackgroundColor(Color.parseColor("#F6F6F6"));
        }

        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        holder.dayText.setText(String.valueOf(dayNo));

        if ((position + 1) % 7 == 0) {
            holder.dayText.setTextColor(Color.BLUE);
        } else if (position % 7 == 0) {
            holder.dayText.setTextColor(Color.RED);
        } else {
            holder.dayText.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedDate(monthDate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;
        View parentView;

        CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}
