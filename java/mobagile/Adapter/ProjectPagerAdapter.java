package com.example.jeanmaccury.mobagile.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jeanmaccury.mobagile.Activities.MainActivity;
import com.example.jeanmaccury.mobagile.Activities.ProjectActivity;
import com.example.jeanmaccury.mobagile.Interface.RealmDB;
import com.example.jeanmaccury.mobagile.POJOs.Project;
import com.example.jeanmaccury.mobagile.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Jean Maccury on 11.03.2018.
 */

public class ProjectPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Project> projectList;

    public ProjectPagerAdapter(FragmentManager fm, ArrayList<Project> projectList) {
        super(fm);
        this.projectList = projectList;
    }

    @Override
    public Fragment getItem(int position) {
        return ProjectFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    public static class ProjectFragment extends Fragment {
        @BindView(R.id.project_title) TextView title;
        @BindView(R.id.project_description) TextView description;
        @BindView(R.id.p_button) Button p_button;

        private static final String ARG_SECTION_NUMBER = "section_number";
        private Realm realm;
        private Project project;

        public ProjectFragment() {

        }


        public static ProjectFragment newInstance(int position) {
            ProjectFragment fragment = new ProjectFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_project, container, false);
            ButterKnife.bind(this, rootView);
            RealmResults<Project> result = new RealmDB().getRealmObjects(Project.class, 0);

            if (!result.isEmpty()) {
                project = result.get(getArguments().getInt(ARG_SECTION_NUMBER));
                title.setText(project.getName());
                description.setText(project.getDescription());
                p_button.setText("Open Project");
            }

            return rootView;
        }

        @OnClick(R.id.p_button)
        public void openProject(){
            ((MainActivity)getActivity()).openProject(project.getId());

        }
    }
}
