package com.example.jeanmaccury.mobagile.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jean Maccury on 09.03.2018.
 */

public class IssueAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Issue> issueList;

    public IssueAdapter(Context context, ArrayList<Issue> issueList) {
        this.context = context;
        this.issueList = issueList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isEmpty() {
        return issueList.isEmpty();
    }

    @Override
    public int getCount() {
        return issueList.size();
    }

    @Override
    public Object getItem(int position) {
        return issueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return issueList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.issue_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Issue issue = issueList.get(position);
        String dr = String.valueOf(issue.getDone_ratio()) + "%";

        if(issue.getDone_ratio() == 100){
            convertView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.issueClosed));
        }
        else if(issue.getDone_ratio() > 0 && issue.getDone_ratio() < 100){
            convertView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.issueIP));
        }

        holder.issue_subject.setText(issue.getSubject());
        holder.done_ratio.setText(dr);

        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.issue_subject) TextView issue_subject;
        @BindView(R.id.done_ratio) TextView done_ratio;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
