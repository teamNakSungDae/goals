package nexters.hashgoals.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nexters.hashgoals.R;

/**
 * Created by clsan on 19/04/2017.
 */
public class SettingFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.view_scroll_setting, container, false);
  }
}
