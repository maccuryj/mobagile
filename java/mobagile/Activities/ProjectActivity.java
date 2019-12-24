package com.example.jeanmaccury.mobagile.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jeanmaccury.mobagile.Interface.APIMethods;
import com.example.jeanmaccury.mobagile.Interface.RealmDB;
import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.POJOs.Membership;
import com.example.jeanmaccury.mobagile.POJOs.Project;
import com.example.jeanmaccury.mobagile.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProjectActivity extends AppCompatActivity {

    private static final String TAG = "Mobagile_Project";
    private Realm realm;
    private int pID;
    private Project project;
    private ArrayList<String> memberList;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.t_title) TextView t_title;
    @BindView(R.id.p_title) TextView p_title;
    @BindView(R.id.p_description) TextView p_description;
    @BindView(R.id.p_description_text) TextView p_description_text;
    @BindView(R.id.created_on) TextView created_on;
    @BindView(R.id.created_on_date) TextView created_on_date;
    @BindView(R.id.created_on_time) TextView created_on_time;
    @BindView(R.id.updated_on) TextView updated_on;
    @BindView(R.id.updated_on_date) TextView updated_on_date;
    @BindView(R.id.updated_on_time) TextView updated_on_time;
    @BindView(R.id.p_progress) TextView p_progress;
    @BindView(R.id.determinate_bar) ProgressBar progress_bar;
    @BindView(R.id.p_progress_perc) TextView p_progess_perc;
    @BindView(R.id.view_members) TextView view_members;
    @BindView(R.id.view_issues) TextView view_issues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        memberList = new ArrayList<>();
        Intent intent = getIntent();
        pID = intent.getIntExtra("pID", 0);
        if(pID != 0){
            initViews();
        }
        setupToolbar();
        loadMembers();
    }

    //Called in order to update progress bar view if issue has been updated
    @Override
    protected void onResume(){
        updateProgress();
        super.onResume();
    }

    protected void onDestroy(){
        realm.close();
        super.onDestroy();
    }

    private void setupToolbar(){
        String title = "Project #" + project.getId();
        t_title.setText(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //Function for the initialisation of the views
    private void initViews(){
        RealmResults<Project> pResult = new RealmDB().getRealmObjects(Project.class, pID);

        if (pResult.isEmpty()){
            Log.e(TAG, "Failed loading project" + pID);
        }
        else{
            project = pResult.first();

            p_title.setText(project.getName());

            p_progress.setText("Progress");

            view_members.setText("View Members");
            view_issues.setText("View Issue");

            p_description.setText("Description");
            p_description_text.setText(project.getDescription());

            created_on.setText("Created on");
            created_on_date.setText(rm_Date(project.getCreated_on()).get(0));
            created_on_time.setText(rm_Date(project.getCreated_on()).get(1));

            updated_on.setText("Updated on");
            updated_on_date.setText(rm_Date(project.getUpdated_on()).get(0));
            updated_on_time.setText(rm_Date(project.getUpdated_on()).get(1));
        }
    }

    //Handle button click on "View Issues" button
    @OnClick(R.id.view_issues)
    public void viewIssues(){
        Log.d(TAG, "view_issues clicked.");
        Intent intent = new Intent(this, IssueTabActivity.class);
        intent.putExtra("pID", pID);
        startActivity(intent);
    }

    //Handle button click on "View Members" button
    @OnClick(R.id.view_members)
    public void viewMembers(){
        Log.d(TAG, "view_members clicked.");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Project Members\n");
        ListView member_list = new ListView(this);
        member_list.setDividerHeight(8);
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, memberList);
        member_list.setAdapter(memberAdapter);
        builder.setView(member_list);

        final Dialog d = builder.create();
        d.create();
        d.show();
        d.getWindow().setLayout(800, 1000);

    }

    //Calculation of the project progress on the basis of issue progress
    public void updateProgress() {
        int progressRatio = 0;
        RealmResults<Issue> iResult = new RealmDB().getRealmIssues(pID, null);
        if(!iResult.isEmpty()){
            for(Issue issue : iResult){
                progressRatio += issue.getDone_ratio();
            }
            progressRatio /= iResult.size();
        }
        String progress = String.valueOf(progressRatio) + "%";
        progress_bar.setProgress(progressRatio);
        p_progess_perc.setText(progress);
    }

    //Load membership objects into the database
    public void loadMembers() {
        RealmResults<Membership> result = new RealmDB().getRealmMembers(pID);
        if(!result.isEmpty()){
            for(Membership member : result){
                memberList.add(member.getUser().getName());
            }
        }
    }

    //Date formatting helper method
    public static ArrayList<String> rm_Date(String dt){
        ArrayList<String> rmDate = new ArrayList<String>();
        rmDate.add(dt.substring(0,10));
        rmDate.add(dt.substring(11,19));

        return rmDate;
    }

}
