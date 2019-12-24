package com.example.jeanmaccury.mobagile.POJOs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jean Maccury on 25.03.2018.
 */

public class ParentIdHolder extends RealmObject {
    @PrimaryKey
    private int id;

    public ParentIdHolder() {
    }

    public ParentIdHolder(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
