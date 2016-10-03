package com.samhith.attendance;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<AndroidVersion> android;

    public DataAdapter(ArrayList<AndroidVersion> android) {
        this.android = android;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.subject.setText( android.get(i).getSubject());
      //  viewHolder.USN.setText("USN : "+ android.get(i).getUSN());
        viewHolder.test_dept.setText("Department : "+ android.get(i).getTestDept());
        viewHolder.date.setText("Test Date : " + android.get(i).getDate());
        viewHolder.time.setText("Test Time : "+ android.get(i).getTime());
        viewHolder.seat_no.setText("Your Seat Number : "+android.get(i).getSeatNo());
        viewHolder.room_no.setText(android.get(i).getRoomNo());


    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView USN,test_dept,date,time,seat_no,room_no,subject;
        public ViewHolder(View view) {
            super(view);

          //  USN = (TextView)view.findViewById(R.id.USN);
            test_dept = (TextView)view.findViewById(R.id.test_dept);
            date = (TextView)view.findViewById(R.id.date);
            time = (TextView)view.findViewById(R.id.time);
            seat_no = (TextView)view.findViewById(R.id.seat_no);
            room_no = (TextView)view.findViewById(R.id.room_no);
            subject = (TextView)view.findViewById(R.id.subject);

        }
    }

}