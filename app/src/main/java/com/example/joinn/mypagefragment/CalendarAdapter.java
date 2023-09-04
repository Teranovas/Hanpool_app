package com.example.joinn.mypagefragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joinn.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends  RecyclerView.Adapter<CalendarAdapter.CalendarviewHolder>{

    ArrayList<Date> dayList;


    public CalendarAdapter(ArrayList<Date> dayList){
        this.dayList = dayList;

    }

    @NonNull
    @Override
    public CalendarviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell,parent,false);

        return new CalendarviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarviewHolder holder, int position) {

        Date monthDate = dayList.get(position);

        Calendar dateCalendar = Calendar.getInstance();

        dateCalendar.setTime(monthDate);

        int currentDay = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = CalendarUtil.selectedDate.get(Calendar.MONTH)+1;
        int currentYear = CalendarUtil.selectedDate.get(Calendar.YEAR);

        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);

        //비교해서 년, 월 같으면 진한색 아니면 연한색으로 변경
        if(displayMonth == currentMonth && displayYear == currentYear){
            holder.parentView.setBackgroundColor(Color.parseColor("#D5D5D5"));

            holder.itemView.setBackgroundColor(Color.parseColor("#CEFBC9"));
        }
        else{
            holder.parentView.setBackgroundColor(Color.parseColor("#F6F6F6"));
        }

        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);

        holder.dayText.setText(String.valueOf(dayNo));




        if( (position + 1) % 7 == 0){
            holder.dayText.setTextColor(Color.BLUE);
        }
        else if( position == 0 || position % 7 == 0){
            holder.dayText.setTextColor(Color.RED);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarviewHolder extends RecyclerView.ViewHolder{

        TextView dayText;

        View parentView;

        public CalendarviewHolder(@NonNull View itemView) {
            super(itemView);

            dayText = itemView.findViewById(R.id.dayText);

            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}
