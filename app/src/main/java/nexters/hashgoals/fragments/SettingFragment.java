package nexters.hashgoals.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import nexters.hashgoals.R;

/**
 * Created by clsan on 19/04/2017.
 */
public class SettingFragment extends Fragment {
  @BindView(R.id.ll_time_reset_goal)
  LinearLayout goalResetTimeLinearLayout;

  private Unbinder unbinder;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_scroll_setting, container, false);
    unbinder = ButterKnife.bind(this, view);

    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @OnClick(R.id.ll_time_reset_goal)
  void onGoalResetTime() {
    DialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
    timePickerDialogFragment.show(getActivity().getSupportFragmentManager(),"Time Picker");
  }
}
