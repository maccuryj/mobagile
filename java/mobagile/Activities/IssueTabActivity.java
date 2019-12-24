package com.example.jeanmaccury.mobagile.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jeanmaccury.mobagile.Adapter.IssuesPagerAdapter;
import com.example.jeanmaccury.mobagile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IssueTabActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.t_title) TextView t_title;
    @BindView(R.id.iContainer) ViewPager view_pager;
    @BindView(R.id.tabs) TabLayout tabs;

    private static String TAG = "Mobagile_Issue";

    private IssuesPagerAdapter issuesPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_tab);
        ButterKnife.bind(this);
        setupToolbar();

        //Setup adapters for swipe motions and tabview
        issuesPagerAdapter = new IssuesPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(issuesPagerAdapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(view_pager));
    }

    //Open activity based on selected issue
    public void openIssue(int iID){
        Log.d(TAG, "Issue opened with ID: " + iID);
        Intent intent = new Intent(this, IssueActivity.class);
        intent.putExtra("iID", iID);
        startActivity(intent);
    }

    public void setupToolbar(){
        t_title.setText("Issue");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
