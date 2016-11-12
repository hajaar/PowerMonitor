package com.kartikn.powermonitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private List<History> historyList;

    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.history_capacity.setText(history.getCapacity() + "");
        holder.history_datetime.setText(history.getDatetime());
        holder.history_startingcharge.setText(history.getStartingcharge() + "");
        holder.history_power.setText(history.getPower());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView history_id, history_capacity, history_datetime, history_startingcharge, history_power;

        public MyViewHolder(View view) {
            super(view);

            history_capacity = (TextView) view.findViewById(R.id.history_capacity);
            history_datetime = (TextView) view.findViewById(R.id.history_date);
            history_startingcharge = (TextView) view.findViewById(R.id.history_startingcharge);
            history_power = (TextView) view.findViewById(R.id.history_power);
        }
    }
}