package com.example.jeanmaccury.mobagile.Services;


import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jeanmaccury.mobagile.Activities.MainActivity;
import com.example.jeanmaccury.mobagile.Interface.APIMethods;
import com.example.jeanmaccury.mobagile.Interface.RealmDB;
import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.POJOs.Membership;
import com.example.jeanmaccury.mobagile.POJOs.Project;
import com.example.jeanmaccury.mobagile.POJOs.TimeEntry;
import com.example.jeanmaccury.mobagile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean Maccury on 20.03.2018.
 */

public class NotificationService extends JobService {
    private static String TAG = "Notification_Service";

    private static final int JOB_ID = 1;
    private static final int MIN = 60 * 1000;

    public static void schedule(Context context) {
        Log.d(TAG, "Service started");
        ComponentName component = new ComponentName(context, NotificationService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, component)
                .setPeriodic(60 * MIN)
                .setRequiresDeviceIdle(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Notification service running!");
       new fetchData().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public class fetchData extends AsyncTask<JobParameters, Void, JobParameters> {

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            Log.d(TAG, "Async Task started!");
            RealmDB rDb = new RealmDB();
            ArrayList<Project> projects = new ArrayList();
            ArrayList<Issue> issues = new ArrayList<>();

            List<Integer> pKeys = rDb.getKeys(projects);
            List<Integer> iKeys = rDb.getKeys(issues);

            projects.addAll(new APIMethods().getProjectsSync());
            for (Project project : projects) {
                issues.addAll(new APIMethods().getIssuesSync(project.getId()));
            }

            rDb.populateRealm(projects);
            rDb.populateRealm(issues);
            List<Integer> pRealmKeys = rDb.getKeys(rDb.getRealmObjects(Project.class, 0));
            List<Integer> iRealmKeys = rDb.getKeys(rDb.getRealmObjects(Issue.class, 0));

            List pComplement = rDb.getRelativeComplement(pKeys, pRealmKeys);
            List iComplement = rDb.getRelativeComplement(iKeys, iRealmKeys);

            String notificationTitle = "Project updates";
            String notificationText = "There have been updates on projects concerning you!";
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            if (pComplement.size() > 0 || iComplement.size() > 0){
                NotificationCompat.Builder mBuilder = new NotificationCompat
                        .Builder(getApplicationContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... v) {
            super.onProgressUpdate(v);
        }

        @Override
        protected void onPostExecute(JobParameters j) {
            super.onPostExecute(j);
            jobFinished(j, false);

        }
    }

}
