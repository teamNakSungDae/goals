package nexters.hashgoals.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.NoArgsConstructor;
import nexters.hashgoals.R;
import nexters.hashgoals.activities.GoalActivity;
import nexters.hashgoals.controllers.GoalDataController;
import nexters.hashgoals.models.Goal;

/**
 * Created by flecho on 2017. 2. 7..
 */

// Empty constructor is required for DialogFragment
// Make sure not to add arguments to the constructor
// Use 'newInstance' instead as shown below
@NoArgsConstructor
public class SetGoalDialogFragment extends DialogFragment {

    /* Butterknife was used to make onClick codes more readable */
    @BindView(R.id.txt_your_goal) EditText mEditText;
    @BindView(R.id.btn_save) Button mSaveButton;
    @BindView(R.id.btn_cancel) Button mCancelButton;

    @BindViews({R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday})
    List<Button> dayViews;
    Goal goal;

    private boolean[] daysButtonState;

    private Unbinder unbinder;

    public static SetGoalDialogFragment newInstance(String title) {
        SetGoalDialogFragment frag = new SetGoalDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_set, container);
        unbinder = ButterKnife.bind(this, view);
        goal = new Goal();

        return view;
    }

    /*
    * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
    * but before any saved state has been restored in to the view.
    * */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        initializeDaysButtonState();

        String title = getArguments().getString("title", "Enter your goal");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @OnClick(R.id.btn_save)
    void onSaveButtonClicked() {
        goal.setMTitle(mEditText.getText().toString());
        goal.setMDaysOfWeek(getIntArrayOfDays());
        GoalDataController.getInstance(getActivity()).addOrUpdateGoal(goal);
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void onCancelButtonClicked() {
        dismiss();
    }

    void initializeDaysButtonState() {
        daysButtonState = new boolean[7];
        for (int i = 0; i<daysButtonState.length; i++) {
            daysButtonState[i] = false;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        GoalActivity goalActivity = (GoalActivity)getActivity();
        Cursor cursor = goalActivity.db.rawQuery("SELECT * FROM goals order by list_index", null);
        goalActivity.goalDragSortAdapter.changeCursor(cursor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday})
    public void onDayButtonClicked(View view) {
        int day;
        Button dayButton;

        switch (view.getId()) {
            case R.id.monday:
                day = 0;
                break;
            case R.id.tuesday:
                day = 1;
                break;
            case R.id.wednesday:
                day = 2;
                break;
            case R.id.thursday:
                day = 3;
                break;
            case R.id.friday:
                day = 4;
                break;
            case R.id.saturday:
                day = 5;
                break;
            default:
                day = 6;
                break;
        }

        dayButton = dayViews.get(day);

        if (daysButtonState[day]) {
            dayButton.setBackgroundResource(R.color.goals_btn_repeat_goal_set);

        } else {
            dayButton.setBackgroundResource(R.color.goals_click_on_btn_repeat_goal_set);

        }
        daysButtonState[day] = !daysButtonState[day];
    }

    public int[] getIntArrayOfDays() {
        int[] arrayOfDays = new int [7];
        for (int i=0; i<daysButtonState.length; i++) {
            if (daysButtonState[i])
                arrayOfDays[i] = 1;
            else
                arrayOfDays[i] = 0;
        }
        return arrayOfDays;
    }
}
