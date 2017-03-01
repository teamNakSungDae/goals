package nexters.hashgoals.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String TABLE_DETAILS = "details";

    // Goal Table Columns
    private static final String COL_GOAL_ID = "_id"; // This is extremely important.
    private static final String COL_GOAL_TEXT = "text";
    private static final String COL_GOAL_LIST_INDEX = "list_index";
    private static final String COL_GOAL_DAYS = "days";

    // Detail Table Columns
    private static final String COL_DETAIL_ID = "_id";
    private static final String COL_DETAIL_FOREIGN = "foreign_id";
    private static final String COL_DETAIL_TEXT = "text";
    private static final String COL_DETAIL_RECYCLE_NO = "recycle_no";



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
                COL_GOAL_TEXT + " TEXT NOT NULL," +
                COL_GOAL_LIST_INDEX + " INTEGER," +
                COL_GOAL_DAYS + " TEXT" +

                ")";


        String CREATE_DETAIL_TABLE =
                "CREATE TABLE "+ TABLE_DETAILS +
                        " ( "+ COL_DETAIL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COL_DETAIL_FOREIGN+" INTEGER REFERENCES "+TABLE_GOALS+"("+COL_GOAL_ID+") ,"+
                        COL_DETAIL_TEXT +" TEXT," +
                        COL_DETAIL_RECYCLE_NO +" INTEGER "+
                ")";

        db.execSQL(CREATE_GOALS_TABLE);
        db.execSQL(CREATE_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them.
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
            onCreate(db);
        }
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

    /* This method must only be used by reflection(). Otherwise, you must not use it.*/
    public Cursor getOrderedCursor() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM goals ORDER BY list_index ASC", null); // ORDER BY list_index ASC
    }

    public Cursor getNewCursor() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM goals ORDER BY list_index ASC", null);
    }

}
