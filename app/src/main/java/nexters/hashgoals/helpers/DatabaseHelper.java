package nexters.hashgoals.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import nexters.hashgoals.models.Goal;

import static android.content.ContentValues.TAG;

/**
 * Created by flecho on 2017. 2. 3..
 * referring to https://guides.codepath.com/android/Local-Databases-with-SQLiteOpenHelper
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "goalDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_GOALS = "goals";

    // Goal Table Columns
    private static final String COL_GOAL_ID = "_id"; // This is extremely important.
    private static final String COL_GOAL_TEXT = "text";
    private static final String COL_GOAL_LIST_INDEX = "list_index";

    public static synchronized DatabaseHelper getInstance(Context context) {
        /* Use the application context, which will ensure that you don't accidentally leak an
        * Activity's context. See this article for more information: http://bit.ly/6LRzfx*/
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_GOALS_TABLE = "CREATE TABLE " + TABLE_GOALS +
                "(" +
                COL_GOAL_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                COL_GOAL_TEXT + " TEXT," +
                COL_GOAL_LIST_INDEX + " INTEGER" +
                ")";

        db.execSQL(CREATE_GOALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them.
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
            onCreate(db);
        }
    }

    // Insert a goal into the database
    public void addGoal(Goal goal) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        /* It's a good idea to wrap our insert in a transaction. This helps with performance and
        * ensures consistency of the database. */
        db.beginTransaction();
        try {
            // 중복이 있는지 확인할 것 같다.
            long goalId = addOrUpdateGoal(goal);
            ContentValues values = new ContentValues();
            values.put(COL_GOAL_ID, goalId);
            values.put(COL_GOAL_TEXT, goal.getTitle());

            db.insertOrThrow(TABLE_GOALS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add goal to database");
        } finally {
            db.endTransaction();
        }

    }

    public long addOrUpdateGoal(Goal goal) {
        SQLiteDatabase db = getWritableDatabase();
        long goalId = -1;

        db.beginTransaction();
        try{

            ContentValues values = new ContentValues();
            values.put(COL_GOAL_TEXT, goal.getTitle());
            values.put(COL_GOAL_LIST_INDEX, getCount()+1);
            // Since it is a newly added item, one must be added to the index.

            /* DB update returns the number of rows affected.*/
            int rows = db.update(TABLE_GOALS, values, COL_GOAL_TEXT + "= ?", new String[]{goal.getTitle()});

            // Check if update succeeded
            if (rows == 1) {
                String userSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        COL_GOAL_ID, TABLE_GOALS, COL_GOAL_TEXT);
                Cursor cursor = db.rawQuery(userSelectQuery, new String[]{String.valueOf(goal.getTitle())});
                try {
                    if (cursor.moveToFirst()) {
                        goalId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // goal with this goalTitle did not already exist, so insert new goal
                goalId = db.insertOrThrow(TABLE_GOALS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update goal");
        } finally {
            db.endTransaction();
        }
        return goalId;
    }

    // Get all goals in the database
    public ArrayList<Goal> getAllGoals() {
        ArrayList<Goal> goals = new ArrayList<Goal>();

        String GOALS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_GOALS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GOALS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Goal newGoal = new Goal();
                    newGoal.setTitle(cursor.getString(cursor.getColumnIndex(COL_GOAL_TEXT)));
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

    // Get all goals in the database
    public int getCount() {

        int counter = 0;
        SQLiteDatabase db = getReadableDatabase();
        String GOALS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_GOALS);
        Cursor cursor = db.rawQuery(GOALS_SELECT_QUERY, null);

        try {
            counter = cursor.getCount();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get count from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return counter;
    }

    // Delete all posts and users in the database
    public void deleteAllGoals() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_GOALS, null, null);
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all goals");
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getCursor() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM goals ORDER BY list_index ASC", null);
        //Cursor cursor = db.rawQuery("SELECT * FROM goals", null);
        return cursor;
    }

    public void remapping(int from, int to) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        try {
                if (from < to) {

                    for (int i = from; i <= to; i++) {
                        if (i == from) {
                            values.put(COL_GOAL_LIST_INDEX, Integer.toString(0));
                            db.update(TABLE_GOALS, values, COL_GOAL_LIST_INDEX + "= ?", new String[]{ Integer.toString(i)});
                        } else {
                            values.put(COL_GOAL_LIST_INDEX, Integer.toString(i-1));
                            db.update(TABLE_GOALS, values, COL_GOAL_LIST_INDEX + "= ?", new String[]{ Integer.toString(i)});
                        }
                    }

                    values.put(COL_GOAL_LIST_INDEX, Integer.toString(to));
                    db.update(TABLE_GOALS, values, COL_GOAL_LIST_INDEX + "= ?", new String[]{ Integer.toString(0)});

                } else { // from == to case is already dealt with before this method is called.

                    for (int i = from; i >= to; i--) {
                        if (i == from) {
                            values.put(COL_GOAL_LIST_INDEX, Integer.toString(0));
                            db.update(TABLE_GOALS, values, COL_GOAL_LIST_INDEX + "= ?", new String[]{ Integer.toString(i)});
                        } else {
                            values.put(COL_GOAL_LIST_INDEX, Integer.toString(i+1));
                            db.update(TABLE_GOALS, values, COL_GOAL_LIST_INDEX + "= ?", new String[]{ Integer.toString(i)});
                        }
                    }
                    values.put(COL_GOAL_LIST_INDEX, Integer.toString(to));
                    db.update(TABLE_GOALS, values, COL_GOAL_LIST_INDEX + "= ?", new String[]{ Integer.toString(0)});
                }

        } catch (Exception e) {
            Log.d(TAG, "Error while remapping");
        } finally {
            db.setTransactionSuccessful(); // Without this line, the update is not reflected.
            db.endTransaction();
        }

    }


}
