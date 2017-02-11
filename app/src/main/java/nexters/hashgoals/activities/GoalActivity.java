package nexters.hashgoals.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import nexters.hashgoals.R;
import nexters.hashgoals.adapters.GoalDragSortAdapter;
import nexters.hashgoals.controllers.GoalDataController;
import nexters.hashgoals.fonts.FontsLoader;
import nexters.hashgoals.fragments.EditGoalDialogFragment;
import nexters.hashgoals.helpers.DatabaseHelper;


public class GoalActivity extends AppCompatActivity {

    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public GoalDragSortAdapter goalDragSortAdapter;
    
    GoalDataController goalDataController;
    DragSortController dragSortController;
    DragSortListView dslv;

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        goalDataController = GoalDataController.getInstance(getApplicationContext());
        dslv = (DragSortListView) findViewById(R.id.dslv);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        defaultToolbar();

        setDragSortListView();
        populateDragSortListView();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditGoalDialogFragment editGoalDialogFragment = EditGoalDialogFragment.newInstance("Some Title");
        editGoalDialogFragment.show(fm, "fragment_edit_goal");
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //((MenuItem)menu).setIcon(R.drawable.more);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_goals:
                showEditDialog();
                return true;
            case R.id.new_edit:
                showEditToolbar();
                return true;
            case R.id.setting:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void defaultToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void showEditToolbar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);


        TextView editTitle = (TextView) toolbar.findViewById(R.id.edit_title);
        editTitle.setTypeface(FontsLoader.getTypeface(getApplicationContext(), FontsLoader.N_S_MEDUIM));
        ImageView logoIcon = (ImageView) findViewById(R.id.logo_icon);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        ImageView arrowBack = (ImageView) findViewById(R.id.arrow_back);

        logoIcon.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.INVISIBLE);

        arrowBack.setVisibility(View.VISIBLE);
        editTitle.setVisibility(View.VISIBLE);

        GoalDragSortAdapter.setEditMenu(true);
        goalDragSortAdapter.reflection();
    }

    public void setDragSortListView() {

        dragSortController = new DragSortController(dslv);
        dragSortController.setDragHandleId(R.id.order_button);
        dragSortController.setSortEnabled(true);
        dragSortController.setDragInitMode(0);

        dslv.setFloatViewManager(dragSortController);
        dslv.setOnTouchListener(dragSortController);
        dslv.setDragEnabled(true);

    }

    public void populateDragSortListView() {

        databaseHelper = DatabaseHelper.getInstance(this);
        db = databaseHelper.getWritableDatabase();

        Cursor goalCursor = db.rawQuery("SELECT * FROM goals", null);
        String[] fromFieldNames = new String[] { "text" };
        int[] toViewIDs =new int[] {R.id.goal_content};

        goalDragSortAdapter =
                new GoalDragSortAdapter(getApplicationContext(), R.layout.list_item_goal, goalCursor,
                        fromFieldNames, toViewIDs, goalDataController);

        dslv.setAdapter(goalDragSortAdapter);
        dslv.setDropListener(goalDragSortAdapter.onDrop);

    }

    @Override
    public void onResume() {
        super.onResume();
        goalDragSortAdapter.reflection();
    }

}
