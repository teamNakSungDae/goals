package nexters.hashgoals.boxes;

import android.content.Context;

import java.util.ArrayList;

import nexters.hashgoals.controllers.GoalDataController;
import nexters.hashgoals.models.Goal;

/**
 * Created by flecho on 2017. 3. 3..
 */

public class GoalBox {
    GoalDataController goalDataController;
    private static ArrayList<Integer> checkedIdList;
    private static ArrayList<Integer> unCheckedIdList;

    public GoalBox(Context applicationContext) {
        this.goalDataController = GoalDataController.getInstance(applicationContext);
    }

    public void upsert() {

    }

    public void initializeCheckedList() {
        goalDataController.initializeCheckedList();
    }

    public void delete() {
        goalDataController.deleteSelectedItems(); // O
        goalDataController.setLeftItemsNumList(); // O
        goalDataController.alignIndicesAfterDelete();
    }

    public void remapping() {

    }

    public Goal getGoalBy(int clicked) {
        return goalDataController.getGoalFromPosition(++clicked);
    }

    public Goal getGoalBy() {
        return goalDataController.getCheckedGoal();
    }
}
