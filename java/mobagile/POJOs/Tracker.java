package com.example.jeanmaccury.mobagile.POJOs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean Maccury on 08.03.2018.
 */

public class Tracker extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;

    public Tracker(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
