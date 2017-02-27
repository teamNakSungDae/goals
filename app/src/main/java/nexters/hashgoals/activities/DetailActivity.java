package nexters.hashgoals.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import nexters.hashgoals.R;
import nexters.hashgoals.adapters.DetailAdapter;
import nexters.hashgoals.models.Detail;
import nexters.hashgoals.models.Goal;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.recycler_detail_task)
    RecyclerView recyclerView;
    DetailAdapter detailAdapter;
    RecyclerView.LayoutManager layoutManager;

    private List<Detail> detailList = new ArrayList<Detail>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        detailAdapter = new DetailAdapter(getApplicationContext(),detailList);


    }

    /*
    * To 기호:
    * 아래의 방식으로 클릭된 아이템을 받을 수 있음.
    * Goal goal= getIntent().getParcelableExtra("goalInfo");
    *
    * */

}
