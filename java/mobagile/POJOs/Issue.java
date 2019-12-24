package com.example.jeanmaccury.mobagile.POJOs;

import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean Maccury on 08.03.2018.
 */

public class Issue extends RealmObject {
    @PrimaryKey
    private int id;

    private ParentIdHolder project;
    private Tracker tracker;
    private String subject;
    private String description;
    private String start_date;
    private String due_date;
    private int done_ratio;
    private String created_on;
    private String updated_on;

    public Issue(){

    }

    public Issue(int done_ratio){
        this.done_ratio = done_ratio;
    }

    public int getId() {
        return id;
    }

    public ParentIdHolder getProject() {
        return project;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public int getDone_ratio() {
        return done_ratio;
    }

    public void setDone_ratio(int done_ratio) {
        this.done_ratio = done_ratio;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }


}
