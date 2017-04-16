package nexters.hashgoals.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import nexters.hashgoals.R;
import nexters.hashgoals.adapters.GoalDragSortAdapter;
import nexters.hashgoals.boxes.GoalBox;
import nexters.hashgoals.fragments.SetGoalDialogFragment;
import nexters.hashgoals.helpers.DatabaseHelper;
import nexters.hashgoals.models.Goal;
import nexters.hashgoals.models.GoalAction;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class GoalActivity extends AppCompatActivity {
    private GoalBox goalBox;

    private GoalDragSortAdapter goalDragSortAdapter;
    private DragSortController dragSortController;
    private DragSortListView dragSortListView;

    private enum ToolbarMode {EDIT_MODE, HOME_MODE}
    private Toolbar toolbar;
    private MenuItem deleteItem, editModeItem, modifyItem;

    @BindView(R.id.rl_toolbar) RelativeLayout toolbarRL;
    @BindView(R.id.edit_title) TextView editTitleTextView;
    @BindView(R.id.image_logo) ImageView logoImage;
    @BindView(R.id.image_text_logo) ImageView logoTextImage;
    @BindView(R.id.button_add_goal) ImageView addGoalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        ButterKnife.bind(GoalActivity.this);
        ButterKnife.setDebug(true);

        goalBox = new GoalBox(getApplicationContext());
        initializeToolbar();

        initializeDragSortAdapter();
        initializeDragSortListView();

        /* for debug*/
//        Intent intent = new Intent(this,DetailActivity.class);
//        List<Goal> lists = GoalDataController.getInstance(getApplicationContext()).getAllGoals();
//        if ( lists == null || lists.get(0) == null)
//            return;
//        //Log.e("Toss the data",lists.get(0).getTitle()+"/"+lists.get(0).getId());
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("goal", lists.get(0));
//        intent.putExtra("goal",bundle);
//        startActivity(intent);
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window.
        setToolbarViewsVisibility(android.R.id.home);
    }

    private void initializeDragSortAdapter() {
        goalDragSortAdapter = new GoalDragSortAdapter(getApplicationContext(),
                this,
                R.layout.list_item_goal,
                new String[]{"_id", "text", "list_index"},
                new int[]{R.id.goal_content});
    }


    private void initializeDragSortListView() {
        dragSortListView = (DragSortListView) findViewById(R.id.dslv);

        dragSortController = new DragSortController(dragSortListView);
        dragSortController.setDragHandleId(R.id.order_button);
//        If dragSortController's mSortEnabled is set true, drag-sort is allowed.
//        Otherwise, drag-sort is rejected.
        dragSortController.setSortEnabled(false); // Initally, drag-sort must not be enabled.
        dragSortController.setDragInitMode(0);

        dragSortListView.setFloatViewManager(dragSortController);
        dragSortListView.setOnTouchListener(dragSortController);
        dragSortListView.setDragEnabled(true);
        dragSortListView.setAdapter(goalDragSortAdapter);
        dragSortListView.setDropListener(goalDragSortAdapter.onDrop);

        // test용 코드.
        dragSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startDetailActivity(goalBox.getGoalBy(position));
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.deleteItem = menu.findItem(R.id.button_delete_goal);
        this.editModeItem = menu.findItem(R.id.button_mode_edit_goal);
        this.modifyItem = menu.findItem(R.id.button_modify_goal);
        this.deleteItem.setVisible(false);
        this.modifyItem.setVisible(false); // initial state of modify icon is invisible.

        this.deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        this.editModeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        this.modifyItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        // Handle item selection
        switch (itemId) {
            case android.R.id.home:
                setToolbarViewsVisibility(itemId);
                onModeChange(ToolbarMode.HOME_MODE);
                goalBox.initializeCheckedList();
                changeEditButtonState(0);
                return true;
            case R.id.button_mode_edit_goal:
                setToolbarViewsVisibility(itemId);
                onModeChange(ToolbarMode.EDIT_MODE);
                return true;
            case R.id.button_delete_goal:
                goalBox.delete();
                onListChange();
                goalBox.initializeCheckedList();
                return true;
            case R.id.button_modify_goal:
                if ("modify_on".equals(item.getTitle()))
                    onSetButtonClick(findViewById(R.id.button_modify_goal));
                return true;
            case R.id.button_setting_goal:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setToolbarViewsVisibility(int itemId) {
        boolean baseBoolean;
        int baseVisibility;

        if (itemId == android.R.id.home) {
            baseBoolean = true;
            baseVisibility = View.VISIBLE;
        } else if (itemId == R.id.button_mode_edit_goal) {
            baseBoolean = false;
            baseVisibility = View.GONE;
        } else {
            throw new RuntimeException("None of toolbar's business");
        }

        toolbarRL.setVisibility(baseVisibility);
        logoImage.setVisibility(baseVisibility);
        logoTextImage.setVisibility(baseVisibility);

        editTitleTextView.setVisibility(baseBoolean ? View.GONE : View.VISIBLE);

        if (editModeItem != null) {
            editModeItem.setVisible(baseBoolean);
            deleteItem.setVisible(!baseBoolean);
            modifyItem.setVisible(!baseBoolean);
        }

        getSupportActionBarSafe().setDisplayHomeAsUpEnabled(!baseBoolean);
        getSupportActionBarSafe().setDisplayShowTitleEnabled(!baseBoolean);
    }

    private void onModeChange(ToolbarMode mode) {
        boolean isEditMode = true;

        if (ToolbarMode.HOME_MODE.equals(mode))
            isEditMode = false;

        GoalDragSortAdapter.setEditMenu(isEditMode);
        onListChange();
        if (!isEditMode)
            goalBox.initializeCheckedList();
        dragSortController.setSortEnabled(isEditMode);
    }

    public void onListChange() {
        goalDragSortAdapter.reflection();
    }

    @OnClick(R.id.button_add_goal)
    public void onAddButtonClick() {
        onSetButtonClick(addGoalButton);
    }

    private void onSetButtonClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        SetGoalDialogFragment setGoalDialogFragment = SetGoalDialogFragment.newInstance("Some Title");

        if (view.getId() == R.id.button_add_goal) {
            setGoalDialogFragment.setAction(GoalAction.INSERT);
            setGoalDialogFragment.setGoal(new Goal());
        } else if (view.getId() == R.id.button_modify_goal) {
            setGoalDialogFragment.setAction(GoalAction.UPDATE);
            setGoalDialogFragment.setGoal(goalBox.getGoalBy());
        } else {
            throw new RuntimeException("View is not set button.");
        }

        setGoalDialogFragment.show(fm, "fragment_goal_set");
    }

    @Override
    public void onResume() {
        Log.e("damn", "onResume state");
        super.onResume();
        onListChange();
    }

    @Override
    public void onPause() {
        Log.e("damn", "onPause state");
        super.onPause();
        goalDragSortAdapter.close();
    }

    @Override
    public void onDestroy() {
        Log.e("damn", "onDestroy state");
        super.onDestroy();
        // profileTracker.stopTracking();
        DatabaseHelper.getInstance(getApplicationContext()).close();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void changeEditButtonState(int numOfCheckedItems) {
        if (numOfCheckedItems == 1) {
            this.modifyItem.setTitle("modify_on");
            this.modifyItem.setIcon(R.drawable.modify_on);
        } else {
            this.modifyItem.setTitle("modify_off");
            this.modifyItem.setIcon(R.drawable.modify_off);
        }
    }

    private ActionBar getSupportActionBarSafe() {
        ActionBar supportActionBar = super.getSupportActionBar();
        if (supportActionBar != null) {
            return supportActionBar;
        }
        throw new RuntimeException("There is no support action bar.");
    }

    // Detail activity need position.
    private void startDetailActivity(Goal clickedGoal) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle goalBundle = new Bundle();
        goalBundle.putParcelable("goalInfo", clickedGoal);
        intent.putExtras(goalBundle);
        startActivity(intent);
    }

//    ProfileTracker profileTracker;
//    CallbackManager callbackManager;
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // Load facebook profile image if exists. Or else get logo_icon
//        final ImageView logoImage = (ImageView) findViewById(R.id.logo_icon);
//        logoImage.setVisibility(View.VISIBLE);
//
//        callbackManager = CallbackManager.Factory.create();
//
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(
//                    Profile oldProfile,
//                    Profile currentProfile) {
//                Profile.setCurrentProfile(currentProfile);
//                setLogoIcon(logoImage, currentProfile.getId());
//            }
//        };
//        Profile currentProfile = Profile.getCurrentProfile();
//
//        if (currentProfile != null) {
//            setLogoIcon(logoImage, currentProfile.getId());
//        }
//    }
//    private void setLogoIcon(ImageView logoImage, String userId) {
//        String userImageUrl = String.format("https://graph.facebook.com/%s/picture?type=small",
//                userId);
//        Glide.with(getApplicationContext())
//                .load(userImageUrl)
//                .placeholder(R.drawable.logo_icon)
//                .into(logoImage);
//    }
}
