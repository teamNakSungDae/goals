package nexters.hashgoals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nexters.hashgoals.R;
import nexters.hashgoals.adapters.DetailAdapter;
import nexters.hashgoals.controllers.DetailController;
import nexters.hashgoals.fragments.EditGoalDialogFragment;
import nexters.hashgoals.fragments.SetRecycleNoDialogFragment;
import nexters.hashgoals.models.Detail;
import nexters.hashgoals.models.Goal;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_title)
    TextView detailTitle;

    @BindView(R.id.add_task_btn)
    ImageView addTaskBtn;

    @BindView(R.id.recycler_detail_task)
    RecyclerView recyclerView;

    DetailAdapter detailAdapter;

    RecyclerView.LayoutManager layoutManager;


    private static DetailController detailController;



    private List<Detail> detailList = new ArrayList<Detail>();

    /**
     * When you press the plus image button.
     * @param v
     */
    @UiThread
    @OnClick(R.id.add_task_btn)
    public void addTaskBtnClick(View v) {
        Toast.makeText(getApplicationContext(),"asdf",Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.add_task_description)
    public void addTaskDescription(View v ){

    }

    @OnClick(R.id.description)
    public void setRecycleNo(View v) {
        FragmentManager fm = getSupportFragmentManager();
        SetRecycleNoDialogFragment setRecycleNoDialogFragment = SetRecycleNoDialogFragment.newInstance("Number Picker");
        //setRecycleNoDialogFragment.show(fm , "fragment_detail_edit_recycle_no");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(DetailActivity.this);
        /* use controller*/
        detailController = DetailController.getInstance(getApplicationContext());
        /*
         * getData from GoalActivity
         */
        Bundle bundle = getIntent().getBundleExtra("goal");
        Goal goal = (Goal)bundle.get("goal");
        Log.e("goal",goal.getTitle());
        /*
         * initialize value.
         */
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        detailAdapter = new DetailAdapter(getApplicationContext(),detailList);
        recyclerView.setAdapter(detailAdapter);

        /*
         * The instance made by include Tag.
         */


        detailTitle.setText( goal . getTitle() );

    }
}
