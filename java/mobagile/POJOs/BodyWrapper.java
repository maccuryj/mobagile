package com.example.jeanmaccury.mobagile.POJOs;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Jean Maccury on 22.03.2018.
 */

public class BodyWrapper {
    @SerializedName("projects")
    private ArrayList<Project> projectList = new ArrayList<>();
    @SerializedName("issues")
    private ArrayList<Issue> issueList = new ArrayList<>();
    @SerializedName("memberships")
    private ArrayList<Membership> memberList = new ArrayList<>();
    @SerializedName("time_entries")
    private ArrayList<TimeEntry> timeEntryList = new ArrayList<>();
    @SerializedName("time_entry")
    private TimeEntry time_entry;
    @SerializedName("issue")
    private  Issue issue;


    public BodyWrapper(){

    }

    public ArrayList<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(ArrayList<Project> projectList) {
        this.projectList = projectList;
    }

    public ArrayList<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(ArrayList<Issue> issueList) {
        this.issueList = issueList;
    }

    public ArrayList<Membership> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<Membership> memberList) {
        this.memberList = memberList;
    }

    public ArrayList<TimeEntry> getTimeEntryList() {
        return timeEntryList;
    }

    public void setTimeEntryList(ArrayList<TimeEntry> timeEntryList) {
        this.timeEntryList = timeEntryList;
    }

    public TimeEntry getTime_entry() {
        return time_entry;
    }

    public void setTime_entry(TimeEntry time_entry) {
        this.time_entry = time_entry;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
