package com.example.jeanmaccury.mobagile.POJOs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean Maccury on 10.03.2018.
 */

public class Membership extends RealmObject{
    @PrimaryKey
    private int id;

    private ParentIdHolder project;
    private User user;

    public Membership(){

    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
