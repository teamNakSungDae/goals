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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
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
    Toolbar toolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        goalDataController = GoalDataController.getInstance(getApplicationContext());
        dslv = (DragSortListView) findViewById(R.id.dslv);

        ButterKnife.bind(GoalActivity.this);
        ButterKnife.setDebug(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        displayLogo();
        defaultToolbar();

        setDragSortListView();
        populateDragSortListView();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditGoalDialogFragment editGoalDialogFragment = EditGoalDialogFragment.newInstance("Some Title");
        editGoalDialogFragment.show(fm, "fragment_edit_goal");
    }

    public void defaultToolbar() {

        //setToolbarTitleFont();
        toolbar.setTitle(R.string.goal_edit_title); //
        setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // back button added.
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*
        * setDisplayOptions(,) method affects the entire options of an ActionBar.
        * */

    }

    private void setToolbarTitleFont() {
        TextView mToolbarTitle;

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            mToolbarTitle = (TextView) f.get(toolbar);
            mToolbarTitle.setTypeface(FontsLoader.getTypeface(getApplicationContext(), FontsLoader.N_S_MEDUIM));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MenuItem deleteItem = menu.findItem(R.id.delete);

        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                deleteItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                onBackButton();
                return true;
            case R.id.add_goals:
                showEditDialog();
                return true;
            case R.id.delete:
                return true;
            case R.id.new_edit:
                deleteItem.setVisible(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button added.
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                onEditButton();
                return true;
            case R.id.setting:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    private void displayLogo() {

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.toolbar_rl);
        ImageView logoIcon = (ImageView) findViewById(R.id.logo_icon);
        ImageView logo = (ImageView) findViewById(R.id.logo);

        relativeLayout.setVisibility(View.VISIBLE);
        logoIcon.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);

    }

    private void hideLogo() {

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.toolbar_rl);
        ImageView logoIcon = (ImageView) findViewById(R.id.logo_icon);
        ImageView logo = (ImageView) findViewById(R.id.logo);

        relativeLayout.setVisibility(View.GONE);
        logoIcon.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);

    }

    public void onEditButton() {

        hideLogo();
        GoalDragSortAdapter.setEditMenu(true);
        goalDragSortAdapter.reflection();
    }

    public void onBackButton(){

        displayLogo();
        GoalDragSortAdapter.setEditMenu(false);
        goalDragSortAdapter.reflection();
    }

    @Override
    public void onResume() {
        super.onResume();
        goalDragSortAdapter.reflection();
    }

}
