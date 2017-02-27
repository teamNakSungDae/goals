package nexters.hashgoals.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import nexters.hashgoals.helpers.DatabaseHelper;
import nexters.hashgoals.models.Goal;
import nexters.hashgoals.models.GoalAction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by flecho on 2017. 2. 10..
 */

/*
*  All operations like insert, update, delete to/from "goals" table are handled here.
* */

public class GoalDataController {

    private static final String TABLE_GOALS = "goals";

    private static GoalDataController mGoalDataController;
    private static DatabaseHelper mDatabaseHelper;
    private static ArrayList<Integer> checkedIdList; // This list holds the _id of the items to be deleted.
    private static ArrayList<Integer> unCheckedIdList; // This list holds the list_index of the items to be preserved.


    /* package private access controlelr. */
    private static class Columns {
        static final String ID = "_id";
        static final String TEXT = "text";
        static final String LIST_INDEX = "list_index";
        static final String DAYS = "days";
    }

    /* GoalDataController takes hold of DatabaseHelper instance. */
    private GoalDataController(Context context){
        mDatabaseHelper = DatabaseHelper.getInstance(context);
        checkedIdList = new ArrayList<>();
        unCheckedIdList = new ArrayList<>();
    }

    public static GoalDataController getInstance(Context context){
        if(mGoalDataController == null){
            mGoalDataController = new GoalDataController(context);
        }
        return mGoalDataController;
    }

    /* When delete button on menu is clicked, this method is performed. */
    public void deleteSelectedItems() {

        // It is going to create your database and table.
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int deleteCounter;

            Log.d("damn", "checked list size is : " + checkedIdList.size());

            deleteCounter = db.delete(TABLE_GOALS,
                    "_id in (" + StringUtils.join(checkedIdList, ",") + ")",
                    null);
            Log.e("damn", "how many lines are deleted: " + deleteCounter);

            if ((deleteCounter != 0) && deleteCounter == checkedIdList.size()) {
                db.setTransactionSuccessful();
                Log.d("damn", "delete is successfully done.");
            }

        } catch (Exception e) {
            Log.d("damn", "Error occured.");
        } finally {
            Log.d("damn", "point 0 reached ");
            db.endTransaction();
            Log.d("damn", "point 0.5 reached ");
        }
    }

    /* After performing delete, entities in list_index column must be rearranged. */
    public void alignIndicesAfterDelete() {

        int numOfItemsLeft = unCheckedIdList.size();
        //mDatabaseHelper.getCount(); // the remaining items after deletion.

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();

        try {
            //int safeUpdateVariable = 0;
            Log.d("damn", "numOfItmesLeft = " + numOfItemsLeft);

            for (int i=0; i < numOfItemsLeft; i++) {

                int targetIndex = unCheckedIdList.get(i);

                Log.d("damn", "targetIndex = " + targetIndex);
                values.put(Columns.LIST_INDEX, Integer.toString(i+1));
                db.update(TABLE_GOALS, values, Columns.LIST_INDEX + "= ?", new String[]{ Integer.toString(targetIndex)});
            }
            db.setTransactionSuccessful(); // db.update must be followed by this method which works like 'commit'.

        } catch (Exception e) {
            Log.d(TAG, "Error while remapping");
        } finally {
            // Without this line, the update is not reflected.
            db.endTransaction();
        }
    }

    /* Thi method must be called after 'deleteSelectedItems()' in order to get left items correctly.  */
    public void setLeftItemsNumList() {
        Cursor cursor = mDatabaseHelper.getNewCursor();

        boolean cursormtf;
        try {
            if (cursormtf = cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("list_index"));
                    unCheckedIdList.add(id);
                    Log.d("damn", "list_index from cursor: " + id);
                } while (cursor.moveToNext());
            }
            Log.d("damn", "cursormtf value is " + cursormtf);
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get goals from database");
        } finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public int getCheckedIdListSize() {
        return checkedIdList.size();
    }

    /* Get checked items from database and store its index in ArrayList. */
    public void addCheckedItemToList(int position) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT _id FROM goals WHERE list_index = " + position;
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    checkedIdList.add(id);
                    Log.e("damn", "here!");
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public void removeUnCheckedItemFromList(int position) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String query = "SELECT _id FROM goals WHERE list_index = " + position;
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    checkedIdList.remove(checkedIdList.indexOf(id));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public void initializeCheckedList() {
        checkedIdList = new ArrayList<>();
        unCheckedIdList = new ArrayList<>();
    }

    public Cursor getMemoData(){
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM goals ORDER BY list_index ASC", null);
    }

    public void deleteAllData(){
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // It is going to create your database and table.
        db.execSQL("delete from " + TABLE_GOALS);
    }

    public Goal getGoalFromPosition(int position) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String title = null;
        String days = null;

        Goal goal = new Goal();

        Cursor cursor = db.rawQuery("SELECT * FROM goals WHERE list_index = " + position, null);
        try {
            if (cursor.moveToFirst()) {

                title = cursor.getString(cursor.getColumnIndex(Columns.TEXT));
                days = cursor.getString(cursor.getColumnIndex(Columns.DAYS));

            } else {
                Log.e("damn", "must never reach here.");
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        goal.setMTitle(title);
        goal.setMDaysOfWeek(StringUtils.split(days, ","));

        return goal;
    }


    public void remapping(int from, int to) {

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        Map<String, String> idToListIndexMap = new HashMap<>();
        Map<String, String> listIndexToListIndexMap = new HashMap<>();

        if (from < to) {
            for (int i = from; i <= to; i++) {
                if (i == from) {
                    listIndexToListIndexMap.put(Integer.toString(from), Integer.toString(to));
                } else {
                    listIndexToListIndexMap.put(Integer.toString(i), Integer.toString(i-1));
                }
            }
        } else { // from == to case is already dealt with before this method is called.
            for (int i = from; i >= to; i--) {
                if (i == from) {
                    listIndexToListIndexMap.put(Integer.toString(from), Integer.toString(to));
                } else {
                    listIndexToListIndexMap.put(Integer.toString(i), Integer.toString(i+1));
                }
            }
        }
        Cursor q = null;
        try {
            Log.e("hello",
                    String.format("select * from %s where list_index in (%s)",
                            TABLE_GOALS,
                            StringUtils.join(listIndexToListIndexMap.values().toArray(), ",")));
            q = db.rawQuery(
                    String.format("select * from %s where list_index in (%s)",
                            TABLE_GOALS,
                            StringUtils.join(listIndexToListIndexMap.values().toArray(), ",")),
                    null);

            if (q.moveToFirst()) {
                do {
                    idToListIndexMap.put(q.getString(q.getColumnIndex("_id")),
                            listIndexToListIndexMap.get(q.getString(q.getColumnIndex("list_index"))));
                } while (q.moveToNext());
            }
            String idList = "(" + StringUtils.join(idToListIndexMap.keySet(), ", ") + ")";
            String temp = StringUtils.join(idToListIndexMap.entrySet().toArray(), " when ");
            temp = "when " + temp + " end";
            temp = temp.replace("=", " then ");
            Log.e("hello", temp);

            db.execSQL(String.format("update %s set list_index = case _id %s where _id in %s",
                    TABLE_GOALS,
                    temp,
                    idList));
        } catch (Exception e) {
            Log.d("hello", "Error while remapping");
        } finally {
            if(q != null && !q.isClosed()) {
                q.close();
            }
            db.setTransactionSuccessful(); // Without this line, the update is not reflected.
            db.endTransaction();
        }

    }


    public Goal getCheckedGoal() {
        Goal goal = new Goal();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        Cursor q = db.rawQuery(
                String.format("select * from %s where list_index = %s",
                        TABLE_GOALS,
                        checkedIdList.get(0).toString()),
                null);

        if (q.moveToFirst()) {
            do {
                goal.setMId(q.getInt(q.getColumnIndex("_id")));
                goal.setMTitle(q.getString(q.getColumnIndex("text")));
                goal.setMDaysOfWeek(StringUtils.split(q.getString(q.getColumnIndex("days")), ","));
            } while (q.moveToNext());
        }

        q.close();
        return goal;
    }

    public long addOrUpdateGoal(Goal goal, GoalAction action) {

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long goalId = -1;

        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(Columns.TEXT, goal.getMTitle());
            values.put(Columns.DAYS, goal.getMDaysOfWeek());
            if (GoalAction.INSERT.equals(action)) {
                values.put(Columns.LIST_INDEX, mDatabaseHelper.getCount()+1);

                // Since it is a newly added item, one must be added to the index.
                // goal with this goalTitle did not already exist, so insert new goal
                goalId = db.insertOrThrow(TABLE_GOALS, null, values);
            } else if (GoalAction.UPDATE.equals(action)) {
                if (getCheckedIdListSize() != 1)
                    throw new RuntimeException("checkedIdListSize is not 1. Update Failed.");

                db.update(TABLE_GOALS,
                        values,
                        Columns.ID + "= ?",
                        new String[]{Integer.toString(checkedIdList.get(0))});
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update goal");
        } finally {
            db.endTransaction();
        }
        return goalId;
    }
}





