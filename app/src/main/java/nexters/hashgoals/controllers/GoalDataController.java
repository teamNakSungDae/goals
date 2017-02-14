package nexters.hashgoals.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nexters.hashgoals.helpers.DatabaseHelper;

/**
 * Created by flecho on 2017. 2. 10..
 */

public class GoalDataController {

        private static final String TABLE_GOALS = "goals";

        private static GoalDataController mGoalDataController;
        private static DatabaseHelper mDatabaseHelper;
        /* package private access controlelr. */
        static class Columns {
            static final String ID = "_id";
            static final String TEXT = "text";
            static final String LIST_INDEX = "list_index";
        }

        /* GoalDataController takes hold of DatabaseHelper instance. */
        private GoalDataController(Context context){
            mDatabaseHelper = DatabaseHelper.getInstance(context);
        }

        public static GoalDataController getInstance(Context context){
            if(mGoalDataController == null){
                mGoalDataController = new GoalDataController(context);
            }
            return mGoalDataController;
        }

        public boolean insertData(String memo, boolean completeness){
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // It is going to create your database and table.
            ContentValues contentValues = new ContentValues();
            contentValues.put(Columns.TEXT, memo);

            long result = db.insert(TABLE_GOALS, null, contentValues); // takes 3 arguments..
            if(result == -1)
                return false;
            else
                return true;
        }


        public Cursor getMemoData(){
            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM goals ORDER BY list_index ASC", null);
            return cursor;
        }

        public Integer deleteData(String id){ // Since id is a primary key
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // It is going to create your database and table.
            return db.delete(TABLE_GOALS, "_id = ?", new String[] { id });
        }

        public void deleteDataFrom(String id){
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // It is going to create your database and table.
            db.delete(TABLE_GOALS, "_id >= ?", new String[]{id});
        }

        public void deleteAllData(){
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // It is going to create your database and table.
            db.execSQL("delete from " + TABLE_GOALS);
        }

        public void rearrangeData(String id){
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase(); // It is going to create your database and table.
            db.execSQL("UPDATE " + TABLE_GOALS + " SET _id = (_id - 1) WHERE _id > " + id);
            db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='event_table'");
        }


        public void moveDataTo(int position, ContentValues values){
            SQLiteDatabase db= mDatabaseHelper.getWritableDatabase();
            String id = Integer.toString(position);
            db.update(TABLE_GOALS, values, " _id = ?", new String[]{id});
        }


}

