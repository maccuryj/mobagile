package com.example.jeanmaccury.mobagile.POJOs;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean Maccury on 12.03.2018.
 */

public class TimeEntry extends RealmObject{
    @PrimaryKey
    private int id;
    private ParentIdHolder project;
    private ParentIdHolder issue;
    private int issue_id;
    private int activity_id;
    private User user;
    private double hours;
    private String comments;
    private Date spent_on;

    public TimeEntry(){

    }


    public TimeEntry(int issue_id, int activity_id, double hours, String comments) {
        this.issue_id = issue_id;
        this.activity_id = activity_id;
        this.hours = hours;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public ParentIdHolder getProject() {
        return project;
    }

    public ParentIdHolder getIssue() {
        return issue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getSpent_on() {
        return spent_on;
    }

    public void setSpent_on(Date spent_on) {
        this.spent_on = spent_on;
    }

}
