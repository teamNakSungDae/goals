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
import butterknife.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import nexters.hashgoals.R;
import nexters.hashgoals.activities.GoalActivity;
import nexters.hashgoals.controllers.GoalDataController;
import nexters.hashgoals.models.Goal;
import nexters.hashgoals.models.GoalAction;

import java.util.List;

/**
 * Created by flecho on 2017. 2. 7..
 */

// Empty constructor is required for DialogFragment
// Make sure not to add arguments to the constructor
// Use 'newInstance' instead as shown below
@Setter
@NoArgsConstructor
public class SetGoalDialogFragment extends DialogFragment {
    @NonNull
    private GoalAction action;

    @NonNull
    private Goal goal;

    private boolean[] daysButtonState;

    /* Butterknife was used to make onClick codes more readable */
    @BindView(R.id.txt_your_goal) EditText mEditText;
    @BindView(R.id.btn_save) Button mSaveButton;
    @BindView(R.id.btn_cancel) Button mCancelButton;

    @BindViews({R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday})
    List<Button> mDaysButton;

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
        mEditText.setText(goal.getMTitle());

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
        goal.setMDaysOfWeekOf(getStringArrayListOfDays());
        GoalDataController.getInstance(getActivity()).upsertGoal(goal, action);

        // Hide fragment.
        this.dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void onCancelButtonClicked() {
        dismiss();
    }

    private void initializeDaysButtonState() {
        daysButtonState = new boolean[7];
        int day_idx = 0;
        if (GoalAction.INSERT.equals(action)) {
            for (day_idx = 0; day_idx < daysButtonState.length; day_idx++) {
                daysButtonState[day_idx] = false;
            }
        } else {
            for (String state : goal.parseDaysOfWeek()) {
                daysButtonState[day_idx] = state.equals("1");
                mDaysButton.get(day_idx++).setBackgroundResource(
                        state.equals("1") ?
                                R.color.goals_click_on_btn_repeat_goal_set :
                                R.color.goals_btn_repeat_goal_set);
            }
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

        dayButton = mDaysButton.get(day);

        if (daysButtonState[day])
            dayButton.setBackgroundResource(R.color.goals_btn_repeat_goal_set);
        else
            dayButton.setBackgroundResource(R.color.goals_click_on_btn_repeat_goal_set);

        daysButtonState[day] = !daysButtonState[day];
    }

    private String[] getStringArrayListOfDays() {
        String[] arrayOfDays = new String[7];
        int index = 0;
        for (boolean aDaysButtonState : daysButtonState) {
            if (aDaysButtonState)
                arrayOfDays[index++] = "1";
            else
                arrayOfDays[index++] = "0";
        }
        return arrayOfDays;
    }
}
