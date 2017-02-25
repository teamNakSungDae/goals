package nexters.hashgoals.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import nexters.hashgoals.fragments.SetGoalDialogFragment;
import nexters.hashgoals.helpers.DatabaseHelper;


public class GoalActivity extends AppCompatActivity {

    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public GoalDragSortAdapter goalDragSortAdapter;
    
    GoalDataController goalDataController;
    DragSortController dragSortController;
    DragSortListView dslv;
    Cursor dragSortCursor;
    Toolbar toolbar;
    Menu menu;
    MenuItem modifyItem;

//    ProfileTracker profileTracker;
//    CallbackManager callbackManager;

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
        setAddButton();
        populateDragSortListView();
    }

    private void showSetDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SetGoalDialogFragment setGoalDialogFragment = SetGoalDialogFragment.newInstance("Some Title");
        setGoalDialogFragment.show(fm, "fragment_goal_set");
    }

    private void setAddButton() {
        ImageView addButton = (ImageView) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetDialog();
            }
        });
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
        this.modifyItem = menu.findItem(R.id.modify);
        this.modifyItem.setVisible(false); // initial state of modify icon is invisible.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuItem deleteItem = menu.findItem(R.id.delete);
        MenuItem editItem = menu.findItem(R.id.edit);


        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                editItem.setVisible(true);
                deleteItem.setVisible(false);
                modifyItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                onBackButton();
                return true;
            case R.id.delete:
                goalDataController.deleteSelectedItems(); // O
                goalDataController.setLeftItemsNumList(); // O
                goalDataController.alignIndicesAfterDelete();
                goalDragSortAdapter.reflection();
                goalDataController.initializeCheckedList();
                return true;
            case R.id.edit:
                editItem.setVisible(false);
                deleteItem.setVisible(true);
                modifyItem.setVisible(true);
                modifyItem.setIcon(getResources().getDrawable(R.drawable.modify_on));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button added.
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                onEditButton();
                return true;
            case R.id.modify:
                Log.e("damn", "modify button is clicked.");
                /* 이 부분 구현해서, Goal의 제목과 반복 알람이 수정되게 바꾸면 된다. */
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
        dragSortController.setSortEnabled(false); // Initally, drag-sort must not be enabled.
        dragSortController.setDragInitMode(0);

        dslv.setFloatViewManager(dragSortController);
        dslv.setOnTouchListener(dragSortController);
        dslv.setDragEnabled(true);
        /* If dragSortController's mSortEnabled is set true, drag-sort is allowed.
        Otherwise, drag-sort is rejected.
         */
    }

    public void populateDragSortListView() {
        databaseHelper = DatabaseHelper.getInstance(this);
        db = databaseHelper.getWritableDatabase();

        dragSortCursor = db.rawQuery("SELECT * FROM goals order by list_index", null); // This adapter attached cursor closes at the 'onPause' state.

        String[] fromFieldNames = new String[]{"_id", "text", "list_index"};
        int[] toViewIDs = new int[]{R.id.goal_content};

        goalDragSortAdapter =
                new GoalDragSortAdapter(GoalActivity.this, R.layout.list_item_goal, dragSortCursor,
                        fromFieldNames, toViewIDs, goalDataController);
        // Application context cannot be cast to GoalActivity. Therefore, must pass context as GoalActivity.this.
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
        dragSortController.setSortEnabled(true);
    }

    public void onBackButton(){
        displayLogo();
        GoalDragSortAdapter.setEditMenu(false);
        goalDragSortAdapter.reflection();
        goalDataController.initializeCheckedList();
        dragSortController.setSortEnabled(false);
    }

    public void changeEditButtonState(int numOfCheckedItems) {
        if (numOfCheckedItems >= 2)
            this.modifyItem.setIcon(getResources().getDrawable(R.drawable.modify_off));
        else
            this.modifyItem.setIcon(getResources().getDrawable(R.drawable.modify_on));
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // Load facebook profile image if exists. Or else get logo_icon
//        final ImageView logoIcon = (ImageView) findViewById(R.id.logo_icon);
//        logoIcon.setVisibility(View.VISIBLE);
//
//        callbackManager = CallbackManager.Factory.create();
//
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(
//                    Profile oldProfile,
//                    Profile currentProfile) {
//                Profile.setCurrentProfile(currentProfile);
//                setLogoIcon(logoIcon, currentProfile.getId());
//            }
//        };
//        Profile currentProfile = Profile.getCurrentProfile();
//
//        if (currentProfile != null) {
//            setLogoIcon(logoIcon, currentProfile.getId());
//        }
//    }

//    private void setLogoIcon(ImageView logoIcon, String userId) {
//        String userImageUrl = String.format("https://graph.facebook.com/%s/picture?type=small",
//                userId);
//        Glide.with(getApplicationContext())
//                .load(userImageUrl)
//                .placeholder(R.drawable.logo_icon)
//                .into(logoIcon);
//    }

    @Override
    public void onResume() {
        Log.e("damn", "onResume state");
        super.onResume();
        goalDragSortAdapter.reflection();
    }

    @Override
    public void onPause() {
        Log.e("damn", "onPause state");
        super.onPause();
        if (dragSortCursor != null && !dragSortCursor.isClosed()) {
            dragSortCursor.close();
        }
    }

    @Override
    public void onDestroy() {
        Log.e("damn", "onDestroy state");
        super.onDestroy();
        // profileTracker.stopTracking();
        db.close();
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

}
