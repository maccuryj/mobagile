package com.example.jeanmaccury.mobagile.POJOs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean Maccury on 10.03.2018.
 */

public class User extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String username;
    private String password;

    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
