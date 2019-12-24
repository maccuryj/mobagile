package com.example.jeanmaccury.mobagile.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jeanmaccury.mobagile.Adapter.EntryDialog;
import com.example.jeanmaccury.mobagile.Adapter.IssuesPagerAdapter;
import com.example.jeanmaccury.mobagile.Adapter.TimeEntryAdapter;
import com.example.jeanmaccury.mobagile.Interface.APIMethods;
import com.example.jeanmaccury.mobagile.Interface.RealmDB;
import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.POJOs.TimeEntry;
import com.example.jeanmaccury.mobagile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class IssueActivity extends AppCompatActivity implements APIMethods.EntryCallback{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.t_title) TextView t_title;
    @BindView(R.id.i_subject) TextView subject;
    @BindView(R.id.i_progress) TextView i_progress;
    @BindView(R.id.i_progress_perc) TextView i_progress_perc;
    @BindView(R.id.i_due_date_text) TextView dd_text;
    @BindView(R.id.i_due_date) TextView due_date;
    @BindView(R.id.i_description) TextView i_description;
    @BindView(R.id.i_description_text) TextView i_description_text;
    @BindView(R.id.view_entries) TextView view_entries;
    @BindView(R.id.post_entry) TextView post_entry;
    @BindView(R.id.i_created_on) TextView created_on;
    @BindView(R.id.i_created_on_time) TextView created_on_time;
    @BindView(R.id.i_created_on_date) TextView created_on_date;
    @BindView(R.id.i_updated_on) TextView updated_on;
    @BindView(R.id.i_updated_on_date) TextView updated_on_date;
    @BindView(R.id.i_updated_on_time) TextView updated_on_time;

    private String TAG = "Mobagile_Issue";
    private int iID;
    private Issue issue;
    private ArrayList<TimeEntry> timeEntryList;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        this.iID = intent.getIntExtra("iID", 0);
        if(iID != 0){
            initViews();
        }
        setupToolbar();

        timeEntryList = new ArrayList<>();
        timeEntryList.addAll(new RealmDB().getRealmTimeEntries(iID));
    }

    protected void onDestroy(){
        realm.close();
        super.onDestroy();
    }

    private void setupToolbar(){
        String title = "Issue #" + issue.getId();
        t_title.setText(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initViews(){
        RealmResults<Issue> pResult = new RealmDB().getRealmObjects(Issue.class, iID);

        if (pResult.isEmpty()){
            Log.e(TAG, "Project Loading Error" + iID);
        }
        else{

            issue = pResult.first();

            updateProgress();

            subject.setText(issue.getSubject());
            dd_text.setText("Due Date:");
            due_date.setText(issue.getDue_date());

            view_entries.setText("View Time Entries");
            post_entry.setText("Log Time");

            i_description.setText("Description:");
            i_description_text.setText(issue.getDescription());

            created_on.setText("Created on");
            created_on_date.setText(ProjectActivity.rm_Date(issue.getCreated_on()).get(0));
            created_on_time.setText(ProjectActivity.rm_Date(issue.getCreated_on()).get(1));

            updated_on.setText("Updated on");
            updated_on_date.setText(ProjectActivity.rm_Date(issue.getUpdated_on()).get(0));
            updated_on_time.setText(ProjectActivity.rm_Date(issue.getUpdated_on()).get(1));
        }
    }

    //Update progress view after time entry post
    private void updateProgress(){
        String progress = issue.getDone_ratio() + "%";
        i_progress.setText("Progress: ");
        i_progress_perc.setText(progress);
        i_progress_perc.invalidate();
    }

    public void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    public boolean getNetworkState(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    //Handle button click on "View Entries" button
    @OnClick(R.id.view_entries)
    public void viewEntries(){
        Log.d(TAG, "view_entries clicked.");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Time Entries\n");
        ListView time_entry_list = new ListView(this);
        time_entry_list.setDividerHeight(8);
        TimeEntryAdapter timeEntryAdapter = new TimeEntryAdapter(this, timeEntryList);
        time_entry_list.setAdapter(timeEntryAdapter);
        builder.setView(time_entry_list);

        final Dialog d = builder.create();
        d.create();
        d.show();
        d.getWindow().setLayout(1100, 1400);
    }

    //Create dialog for time entry post
    @OnClick(R.id.post_entry)
    public void postEntry(){
        Log.d(TAG, "post_entries clicked.");

        final Dialog d = new EntryDialog(this, issue);
        d.create();
        d.show();
        d.getWindow().setLayout(1100, 1500);
    }

    //Time entry post response method
    @Override
    public void eOnResponse(RealmObject o) {
        if(o.getClass() == Issue.class) {
            updateDoneRatio(((Issue)o).getDone_ratio());
            updateProgress();
        }
        else{
            new RealmDB().populateRealm(o);
            timeEntryList.add((TimeEntry)o);
        }
    }

    //Time entry post error method
    @Override
    public void eOnError(String error) {
        showSnackbar(error);
    }

    //Update issue progress in database
    private void updateDoneRatio(int doneRatio){
        realm.beginTransaction();
        issue.setDone_ratio(doneRatio);
        realm.commitTransaction();
    }
}
