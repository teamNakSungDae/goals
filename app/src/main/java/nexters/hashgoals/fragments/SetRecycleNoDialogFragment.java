package nexters.hashgoals.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import nexters.hashgoals.R;

/**
 * Created by kwongiho on 2017. 2. 28..
 */

public class SetRecycleNoDialogFragment extends DialogFragment {
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.number_picker)
    NumberPicker numberPicker;

    /*
    ButterKinfe.
     */
    private Unbinder unbinder;

    public static SetRecycleNoDialogFragment newInstance(String title) {
        SetRecycleNoDialogFragment frag = new SetRecycleNoDialogFragment();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.confirm)
    public void confirmClick(View v) {
        if(numberPicker.getValue() == 0 )
            Toast.makeText(getActivity(),"반복 횟수를 지정해 주세요",Toast.LENGTH_SHORT).show();
    }
}
