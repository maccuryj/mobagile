package com.example.jeanmaccury.mobagile.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.POJOs.Membership;
import com.example.jeanmaccury.mobagile.POJOs.TimeEntry;
import com.example.jeanmaccury.mobagile.Services.NotificationService;
import com.example.jeanmaccury.mobagile.Adapter.ProjectPagerAdapter;
import com.example.jeanmaccury.mobagile.Interface.APIMethods;
import com.example.jeanmaccury.mobagile.Interface.RealmDB;
import com.example.jeanmaccury.mobagile.POJOs.Project;
import com.example.jeanmaccury.mobagile.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

//Launcher activity to display an overview of the projects
public class MainActivity extends AppCompatActivity implements APIMethods.apiCallback{

    private static final String TAG = "Mobagile_Main";
    private Realm realm;
    private ArrayList<Project> projectList = new ArrayList<>();
    private ProjectPagerAdapter projectPagerAdapter;
    private ViewPager viewPager;
    private boolean dataLoaded = false;

    @BindView(R.id.p_container) ViewPager view_pager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.t_title) TextView t_title;

    //Launched at activity creation
    //Initialisation method for toolbar, database and activity content
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRealm();
        setupToolbar();

        NotificationService.schedule(getApplicationContext());

        //Check if network is available
        if(getNetworkState()) {
            new APIMethods().getProjects(this);
        }
        //If no network is available, offline process is started
        else{
            List<Project> pList = new RealmDB().getRealmObjects(Project.class, 0);
            if (!pList.isEmpty()){
                projectList.addAll(pList);
                dataLoaded = true;
                showSnackbar("No network connection!");
            }
        }
        //Support adapters to enable swipe screen
        projectPagerAdapter = new ProjectPagerAdapter(getSupportFragmentManager(), projectList);
        viewPager = view_pager;
        viewPager.setAdapter(projectPagerAdapter);
    }

    //Close the database instance on destruction of activity
    @Override
    public void onDestroy(){
        realm.close();
        super.onDestroy();
    }

    private void setupToolbar(){
        t_title.setText("My Projects");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //Inflate options menu inside toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Define actions for options menu button clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            Log.d(TAG, "Clicked refresh!");
            dataLoaded = false;
            if (getNetworkState()) {
                new fetchData().execute(projectList);
            }
            else{
                showSnackbar("No network connection!");
            }
            return true;
        }
        else if(id == R.id.menu_licenses){
            Log.d(TAG, "Clicked licenses!");
            new LibsBuilder().withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR).start(this);
        }
        return super.onOptionsItemSelected(item);

    }

    public boolean getNetworkState(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    //Simplified syntax for showing a snackbar
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.p_container), message, Snackbar.LENGTH_SHORT).show();
    }

    //Function for database setup
    //Changes in database structure, which is unavoidable, lead to crashes
    //Therefore, catch database migration exception
    private void setupRealm() {
        try {
            Realm.init(this);
            try {
                realm = Realm.getDefaultInstance();
            } catch (RealmMigrationNeededException r) {
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                realm = Realm.getDefaultInstance();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            showSnackbar("Database could not be loaded!");
        }
    }

    //Define action for opening a project
    public void openProject(int pID){
        if (!dataLoaded){
            showSnackbar("Please wait while data finished loading!");
        }
        else {
            Log.d(TAG, "Project opened with ID: " + pID);
            Intent intent = new Intent(this, ProjectActivity.class);
            intent.putExtra("pID", pID);
            startActivity(intent);
        }
    }

    //Project call response callback
    @Override
    public void onResponse(ArrayList response){
        new RealmDB().populateRealm(response);
        projectList.addAll(response);
        new fetchData().execute(projectList);
        projectPagerAdapter.notifyDataSetChanged();

    }

    //Project call error callback
    @Override
    public void onError(String error) {
        showSnackbar(error);
    }

    //Inner class to handle network operations on a background thread after projects were loaded
    public class fetchData extends AsyncTask<ArrayList<Project>, Integer, Void> {
        @Override
        protected Void doInBackground(ArrayList<Project>... params) {
            ArrayList<Membership> members = new ArrayList<>();
            ArrayList<Issue> issues = new ArrayList<>();
            ArrayList<TimeEntry> entries = new ArrayList<>();
            for(Project project : params[0]){
                members.addAll(new APIMethods().getMembersSync(project.getId()));
                issues.addAll(new APIMethods().getIssuesSync(project.getId()));
                entries.addAll(new APIMethods().getTimeEntriesSync(project.getId()));
            }
            new RealmDB().populateRealm(members);
            new RealmDB().populateRealm(issues);
            new RealmDB().populateRealm(entries);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //After execution, user can open projects
        @Override
        protected void onPostExecute(Void v) {
            showSnackbar("Project data loaded!");
            dataLoaded = true;
            super.onPostExecute(v);
        }
    }
}
