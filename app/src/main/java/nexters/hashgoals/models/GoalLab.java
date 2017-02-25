package nexters.hashgoals.models;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by flecho on 2017. 1. 31..
 */

public class GoalLab {
    // GoalLab is a singleton.
    private ArrayList<Goal> mGoals;
    private static GoalLab sGoalLab;
    private Context mAppContext;

    private GoalLab(Context appContext) {
        mAppContext = appContext;
        mGoals = new ArrayList<Goal>();
        for (int i = 0; i< 5; i++) {
            Goal g = new Goal();
            g.setMTitle("Goal #" + i);
            mGoals.add(g);
        }
    }

    public static GoalLab get(Context c) {
        if (sGoalLab == null) {
            sGoalLab = new GoalLab(c.getApplicationContext());
        }
        return sGoalLab;
    }

    public ArrayList<Goal> getGoals() { return mGoals; }

    public void insertGoal(Goal g) {
        mGoals.add(g);
    }

}
