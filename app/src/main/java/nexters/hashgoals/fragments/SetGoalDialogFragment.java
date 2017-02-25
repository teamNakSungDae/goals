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

import butterknife.BindView;
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
        // @Nullable이 뭐지?
        // onViewCreated는 뭐지?
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        String title = getArguments().getString("title", "Enter your goal");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @OnClick(R.id.btn_save)
    void onSaveButtonClicked() {
        Goal g = new Goal();
        g.setTitle(mEditText.getText().toString());
        GoalDataController.getInstance(getActivity()).addOrUpdateGoal(g);
        //DatabaseHelper.getInstance(getActivity()).addOrUpdateGoal(g);
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void onCancelButtonClicked() {
        dismiss();
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
}
