package nexters.hashgoals.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nexters.hashgoals.adapters.GoalDragSortAdapter;
import nexters.hashgoals.helpers.DatabaseHelper;
import nexters.hashgoals.models.Goal;

import static android.R.attr.left;
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
        private static ArrayList<Integer> checkedItemNumList; // This list holds the _id of the items to be deleted.
        private static ArrayList<Integer> leftItemNumList; // This list holds the list_index of the items to be preserved.

        /* package private access controlelr. */
        static class Columns {
            static final String ID = "_id";
            static final String TEXT = "text";
            static final String LIST_INDEX = "list_index";
        }

        public int getCheckedItemNumList() {
            return checkedItemNumList.size();
        }

        /* GoalDataController takes hold of DatabaseHelper instance. */
        private GoalDataController(Context context){
            mDatabaseHelper = DatabaseHelper.getInstance(context);
            checkedItemNumList = new ArrayList<Integer>();
            leftItemNumList = new ArrayList<Integer>();
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
                int deleteCounter = 0;
                Log.d("damn", "checked list size is : " + checkedItemNumList.size());

                deleteCounter = db.delete(TABLE_GOALS,
                        "_id in (" + StringUtils.join(checkedItemNumList, ",") + ")",
                        null);

                if ((deleteCounter != 0) && deleteCounter == checkedItemNumList.size()) {
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

            int numOfItemsLeft = mDatabaseHelper.getCount(); // the remaining items after deletion.

            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues values = new ContentValues();
            
            try {
                //int safeUpdateVariable = 0;

                for (int i=0; i < numOfItemsLeft; i++) {
                    int targetIndex = leftItemNumList.get(i);
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
            Cursor cursor = mDatabaseHelper.getOrderedCursor();
    
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("list_index"));
                        leftItemNumList.add(id);
                        Log.d("damn", "list_index from cursor: " + id);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d(TAG, "Error while trying to get goals from database");
            } finally {

                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
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
                        checkedItemNumList.add(id);
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
                        checkedItemNumList.remove(checkedItemNumList.indexOf(id));
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

        /*
        * I'm not sure whether this is a good idea for initializing ArrayList.
        * */
        public void initializeCheckedList() {
            checkedItemNumList.clear();
            leftItemNumList.clear();
        }

        public Cursor getMemoData(){
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM goals ORDER BY list_index ASC", null);
            return cursor;
        }

        public void deleteAllData(){
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // It is going to create your database and table.
            db.execSQL("delete from " + TABLE_GOALS);
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
            String temp = StringUtils.join(idToListIndexMap.entrySet().toArray(), ",");
            temp = "(" + temp.replace(",", "), (") + ")";
            temp = temp.replace("=", ",");
            Log.e("hello", temp);

            db.execSQL(String.format("replace into %s (_id, list_index) values %s",
                    TABLE_GOALS,
                    temp));
        } catch (Exception e) {
            Log.d(TAG, "Error while remapping");
        } finally {
            if(q != null && !q.isClosed()) {
                q.close();
            }
            db.setTransactionSuccessful(); // Without this line, the update is not reflected.
            db.endTransaction();
        }

    }

    // Get all goals in the database
    public ArrayList<Goal> getAllGoals() {
        ArrayList<Goal> goals = new ArrayList<Goal>();

        String GOALS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_GOALS);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(GOALS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Goal newGoal = new Goal();
                    newGoal.setTitle(cursor.getString(cursor.getColumnIndex(Columns.TEXT)));
                    goals.add(newGoal);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return goals;
    }

    /**
    * For debugging.
    * This method shows ID of each column.
    * */
    public void forTest() {
        String temp = "";
        for(int i=0; i<checkedItemNumList.size(); i++) {
            if(i == 0)
                temp = checkedItemNumList.get(i).toString();
            else
                temp = temp + "," + checkedItemNumList.get(i);
        }

        //Toast.makeText(mContext, temp, Toast.LENGTH_SHORT).show();
    }

    public long addOrUpdateGoal(Goal goal) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long goalId = -1;

        db.beginTransaction();
        try{

            ContentValues values = new ContentValues();
            values.put(Columns.TEXT, goal.getTitle());
            values.put(Columns.LIST_INDEX, mDatabaseHelper.getCount()+1);
            // Since it is a newly added item, one must be added to the index.

            // goal with this goalTitle did not already exist, so insert new goal
            goalId = db.insertOrThrow(TABLE_GOALS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update goal");
        } finally {
            db.endTransaction();
        }
        return goalId;
    }
}





