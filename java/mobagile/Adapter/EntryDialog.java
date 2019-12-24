package com.example.jeanmaccury.mobagile.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.jeanmaccury.mobagile.Activities.IssueActivity;
import com.example.jeanmaccury.mobagile.Activities.MainActivity;
import com.example.jeanmaccury.mobagile.Interface.APIMethods;
import com.example.jeanmaccury.mobagile.POJOs.Issue;
import com.example.jeanmaccury.mobagile.POJOs.TimeEntry;
import com.example.jeanmaccury.mobagile.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jean Maccury on 14.03.2018.
 */

public class EntryDialog extends Dialog {

    @BindView(R.id.e_cancel) Button cancel_button;
    @BindView(R.id.e_ok) Button ok_button;
    @BindView(R.id.edit_hours) TextInputEditText edit_hours;
    @BindView(R.id.edit_ratio) TextInputEditText edit_ratio;
    @BindView(R.id.comment_input) TextInputEditText comment;
    @BindView(R.id.dialog_view) RelativeLayout dialog_view;

    private Context context;
    private Issue issue;

    public EntryDialog(Context context, Issue issue){
        super(context);
        setContentView(R.layout.entry_dialog);
        ButterKnife.bind(this);
        this.issue = issue;
        this.context = context;
        setOwnerActivity((IssueActivity) context);
        setTitle("Log Time");

        edit_ratio.setText(String.valueOf(issue.getDone_ratio()));
    }

    @OnClick(R.id.e_cancel)
    public void cancelDialog(){
        cancel();
    }

    @OnClick(R.id.e_ok)
    public void postEntry(){
        if (!((IssueActivity)getOwnerActivity()).getNetworkState()){
            ((IssueActivity)getOwnerActivity()).showSnackbar("No network connection!");
            dismiss();
        }
        else {


            edit_hours.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            edit_hours.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            double h;
            int r;
            String c;
            try {
                h = Integer.valueOf(edit_hours.getText().toString());
            } catch (Exception e) {
                h = 0;
            }
            try {
                r = Integer.valueOf(edit_ratio.getText().toString());
            } catch (Exception e) {
                r = issue.getDone_ratio();
            }
            try {
                c = comment.getText().toString();
            } catch (Exception e) {
                c = "";
            }

            edit_ratio.setText(String.valueOf(issue.getDone_ratio()));
            if (h > 24 || h <= 0) {
                showSnackbar("'Hours' field needs to be set between 0 and 24.");
                edit_hours.setTextColor(ContextCompat.getColor(getContext(), R.color.inputError));
            } else if (r < issue.getDone_ratio()) {
                showSnackbar("'Ratio' field cannot be lower than current progress.");
                edit_ratio.setTextColor(ContextCompat.getColor(getContext(), R.color.inputError));
            } else if (r > 100) {
                showSnackbar("'Ratio' field cannot be higher than 100, you moron.");
                edit_ratio.setTextColor(ContextCompat.getColor(getContext(), R.color.inputError));
            } else {
                if (r - issue.getDone_ratio() == 0) {
                    r = 0;
                }
                new APIMethods().postTimeEntry(((IssueActivity) getOwnerActivity()), issue.getId(),
                        new TimeEntry(issue.getId(), 9, h, c), r);
                dismiss();
            }
        }
    }

    public void showSnackbar(String message){
        Snackbar.make(dialog_view, message, Snackbar.LENGTH_LONG);
    }



}
