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
}
