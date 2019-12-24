package com.example.jeanmaccury.mobagile.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeanmaccury.mobagile.POJOs.TimeEntry;
import com.example.jeanmaccury.mobagile.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

/**
 * Created by Jean Maccury on 12.03.2018.
 */

public class TimeEntryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TimeEntry> entryList;

    public TimeEntryAdapter(Context context, ArrayList<TimeEntry> entryList) {
        this.context = context;
        this.entryList = entryList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isEmpty() {
        return entryList.isEmpty();
    }

    @Override
    public int getCount() {
        return entryList.size();
    }

    @Override
    public Object getItem(int position) {
        return entryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return entryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.time_entry_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TimeEntry entry = entryList.get(position);

        String s = String.valueOf(entry.getSpent_on());
        String spent = s.substring(4, 11) + s.substring(30, 34);
        String h = String.valueOf(entry.getHours()) + "h";

        holder.user.setText(entry.getUser().getName());
        holder.spent_on.setText(spent);
        holder.hours.setText(h);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.te_user)
        TextView user;
        @BindView(R.id.te_spent_on)
        TextView spent_on;
        @BindView(R.id.te_hours)
        TextView hours;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}