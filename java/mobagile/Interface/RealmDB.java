package com.example.jeanmaccury.mobagile.Interface;

import android.util.Log;

import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.POJOs.Membership;
import com.example.jeanmaccury.mobagile.POJOs.Project;
import com.example.jeanmaccury.mobagile.POJOs.TimeEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Jean Maccury on 04.03.2018.
 */

public class RealmDB {

    public  RealmResults getRealmObjects(Class c, int id){
        Realm realm = Realm.getDefaultInstance();
        RealmResults result = realm.where(c).findAll();
        if(id != 0){
            result = result.where().equalTo("id", id).findAll();
        }
        realm.close();
        return result;
    }

    public RealmResults<Membership> getRealmMembers(int pID){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Membership> result = realm.where(Membership.class)
                .equalTo("project.id", pID).findAll();
        realm.close();
        return result;
    }

    public RealmResults<Issue> getRealmIssues(int pID, String issueType){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Issue> result = realm.where(Issue.class)
                .equalTo("project.id", pID).findAll();
        if(issueType != null) {
            result = result.where().equalTo("tracker.name", issueType).findAll()
                                    .sort("done_ratio");
        }
        realm.close();
        return result;
    }

    public RealmResults<TimeEntry> getRealmTimeEntries(int iID){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TimeEntry> result;
        result = realm.where(TimeEntry.class)
                .equalTo("issue.id", iID).findAll()
                .sort("spent_on");
        realm.close();
        return result;
    }

    private void refreshRealm(List response, List oldDb){
        Realm realm = Realm.getDefaultInstance();
        Class c = response.get(0).getClass();

        List responseKeys = getKeys(response);
        List dbKeys = getKeys(oldDb);
        List<Integer> del = getRelativeComplement(dbKeys, responseKeys);

        realm.beginTransaction();
        for (int i : del){
            getRealmObjects(c, i).deleteAllFromRealm();
        }
        realm.commitTransaction();

        realm.close();
    }

    public List getKeys(List list){
        List<Integer> keys = new ArrayList<>();
        if (list.isEmpty()){
            return keys;
        }
        if (list.get(0).getClass() == Project.class){
            for (Object o  : list){
                keys.add(((Project)o).getId());
            }
            return keys;
        }
        else if (list.get(0).getClass() == Issue.class){
            for (Object o  : list){
                keys.add(((Issue)o).getId());
            }
            return keys;
        }
        else if (list.get(0).getClass() == Membership.class){
            for (Object o  : list){
                keys.add(((Membership)o).getId());
            }
            return keys;
        }
        else if (list.get(0).getClass() == TimeEntry.class){
            for (Object o  : list){
                keys.add(((TimeEntry)o).getId());
            }
            return keys;
        }
        else {
            return new ArrayList();
        }
    }

    public <T> List<T> getRelativeComplement(List<T> listA, List<T> listB) {
        List<T> list = new ArrayList<>();

        for (T t : listA) {
            if(!listB.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public void populateRealm(RealmObject response){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(response);
        realm.commitTransaction();
        realm.close();
    }

    public void populateRealm(ArrayList<? extends RealmObject> response){
        if(!response.isEmpty()) {
            Realm realm = Realm.getDefaultInstance();
            List oldDb = realm.copyFromRealm(realm.where(response.get(0).getClass()).findAll());
            realm.beginTransaction();
            realm.insertOrUpdate(response);
            realm.commitTransaction();
            realm.close();

            refreshRealm(response, oldDb);
        }
    }

}
