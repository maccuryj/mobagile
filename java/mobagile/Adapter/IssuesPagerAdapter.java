package com.example.jeanmaccury.mobagile.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jeanmaccury.mobagile.Activities.IssueTabActivity;
import com.example.jeanmaccury.mobagile.Interface.RealmDB;
import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.realm.Realm;

/**
 * Created by Jean Maccury on 11.03.2018.
 */

public class IssuesPagerAdapter extends FragmentStatePagerAdapter {

    public IssuesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return IssuesFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    public static class IssuesFragment extends Fragment {
        private List<String> trackers = Arrays.asList("Feature", "Bug", "Support");
        private ArrayList<Issue> issueList = new ArrayList<>();
        private IssueAdapter issueAdapter;

        private static final String ARG_SECTION_NUMBER = "section_number";

        @BindView(R.id.issue_list) ListView issues_list;

        public IssuesFragment() {

        }

        @Override
        public void onResume() {
            issueAdapter.notifyDataSetChanged();
            super.onResume();
        }

        public static IssuesFragment newInstance(int sectionNumber) {
            IssuesFragment fragment = new IssuesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_issue_tab, container, false);
            ButterKnife.bind(this, rootView);

            int pID = getActivity().getIntent().getIntExtra("pID", 0);
            int sectionNr = getArguments().getInt(ARG_SECTION_NUMBER);

            issueList.addAll(new RealmDB().getRealmIssues(pID, trackers.get(sectionNr)));
            issueAdapter = new IssueAdapter(this.getContext(), issueList);
            issues_list.setAdapter(issueAdapter);

            return rootView;
        }

        @OnItemClick(R.id.issue_list)
        public void openIssue(int position){
            ((IssueTabActivity)getActivity()).openIssue(issueList.get(position).getId());
        }
    }
}