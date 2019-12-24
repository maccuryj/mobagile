package com.example.jeanmaccury.mobagile.Interface;

import android.util.Log;

import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.POJOs.BodyWrapper;
import com.example.jeanmaccury.mobagile.POJOs.Membership;
import com.example.jeanmaccury.mobagile.POJOs.Project;
import com.example.jeanmaccury.mobagile.POJOs.TimeEntry;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;

import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by Jean Maccury on 04.03.2018.
 */

public class APIMethods {

    private final String TAG = "Mobagile_API";
    private final String API_KEY = "5ae2fc78f92687e2e9eade4a2e56d8b53f600229";
    private final String BASE_URL = "http://10.0.2.2/redmine/";

    public void getProjects(final apiCallback callback){
        final RetrofitService service = getRFService();

        Call<BodyWrapper> call = service.getProjects(API_KEY);
        call.enqueue(new Callback<BodyWrapper>() {
            @Override
            public void onResponse(Call<BodyWrapper> call, Response<BodyWrapper> response) {
                if (response.isSuccessful()) {
                    if(!response.body().getProjectList().isEmpty())
                        Log.d(TAG, "Project call succeeded!");
                        callback.onResponse(response.body().getProjectList());
                }
                else {
                    callback.onError("Projects - Server call failed!");
                    Log.e(TAG, "Project response: " + response.code() + " (" + response.errorBody() + ")");
                }
            }

            @Override
            public void onFailure(Call<BodyWrapper> call, Throwable t) {
                callback.onError("Projects - Server call failed!");
                Log.e(TAG, "Project error: " + t.getMessage());
            }
        });
    }

    public ArrayList<Project> getProjectsSync(){
        ArrayList<Project> projects = new ArrayList<>();
        final RetrofitService service = getRFService();
        Call<BodyWrapper> call = service.getProjects(API_KEY);
        try {
            BodyWrapper wrapper = call.execute().body();
            projects = wrapper.getProjectList();
            Log.d(TAG, "Project call succeeded!");
            return projects;
        }
        catch (IOException e){
            Log.e(TAG, e.getMessage());
        }
        return projects;
    }

    public ArrayList<Membership> getMembersSync(final int projectId){
        ArrayList<Membership> members = new ArrayList<>();
        final RetrofitService service = getRFService();
        Call<BodyWrapper> call = service.getMembers(projectId);
        try {
            BodyWrapper wrapper = call.execute().body();
            members = wrapper.getMemberList();
            Log.d(TAG, "Member call succeeded!");
            return members;
        }
        catch (IOException e){
            Log.e(TAG, e.getMessage());
        }
        return members;
    }

    public ArrayList<Issue> getIssuesSync(final int projectId){
        ArrayList<Issue> issues = new ArrayList<>();
        final RetrofitService service = getRFService();
        Call<BodyWrapper> call = service.getIssues(projectId);
        try {
            BodyWrapper wrapper = call.execute().body();
            issues = wrapper.getIssueList();
            Log.d(TAG, "Issue call succeeded!");
            return issues;
        }
        catch (IOException e){
            Log.e(TAG, e.getMessage());
        }
        return issues;
    }

    public ArrayList<TimeEntry> getTimeEntriesSync(final int projectId){
        ArrayList<TimeEntry> timeEntries = new ArrayList<>();
        final RetrofitService service = getRFService();
        Call<BodyWrapper> call = service.getTimeEntries(projectId);
        try {
            BodyWrapper wrapper = call.execute().body();
            timeEntries = wrapper.getTimeEntryList();
            Log.d(TAG, "Time entry call succeeded!");
            return timeEntries;
        }
        catch (IOException e){
            Log.e(TAG, e.getMessage());
        }
        return timeEntries;
    }

    public void postTimeEntry(final EntryCallback callback, final int issueId, final TimeEntry timeEntry, final int doneRatio){
        final RetrofitService service = this.getRFService();
        final BodyWrapper entryWrapper = new BodyWrapper();
        entryWrapper.setTime_entry(timeEntry);
        
        Call<BodyWrapper> tCall = service.postTimeEntry("application/json", entryWrapper, API_KEY);
        tCall.enqueue(new Callback<BodyWrapper>() {
            @Override
            public void onResponse(Call<BodyWrapper> call, Response<BodyWrapper> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Time entry post succeeded!");
                    callback.eOnResponse(response.body().getTime_entry());
                    if (doneRatio != 0) {
                        final BodyWrapper issueWrapper = new BodyWrapper();
                        issueWrapper.setIssue(new Issue(doneRatio));
                        Call<Void> iCall = service.updateDoneRatio(issueId, "application/json", API_KEY, issueWrapper);
                        iCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "Issue progress updated!");
                                    callback.eOnResponse(issueWrapper.getIssue());
                                } else {
                                    callback.eOnError("Issue - Server call failed!");
                                    Log.e(TAG, "Issue response: " + response.code() + " (" + response.errorBody() + ")");
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                callback.eOnError("Issue - Server call failed!");
                                Log.e(TAG, "Issue error: " + t.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e(TAG, response.message());
                    callback.eOnError("Time Entry post failed - " + response.code() + " - " + response.message());

                }
            }

            @Override
            public void onFailure(Call<BodyWrapper> call, Throwable t) {
                callback.eOnError("Time entry - Server call failed!");
                Log.e(TAG, "Time entry error: " + t.getMessage());
            }
        });
    }

    public interface apiCallback{
        void onResponse(ArrayList response);

        void onError(String error);
    }
    
    public interface EntryCallback{
        void eOnResponse(RealmObject o);
        
        void eOnError(String error);
    }

    public RetrofitService getRFService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitService.class);
    }
}
