package nexters.hashgoals.controllers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import nexters.hashgoals.models.Detail;
import nexters.hashgoals.models.DetailData;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

/**
 * SOLID Test class.
 */
public class DetailControllerImpl implements DAO<Detail>,Controller {
    /* Singleton start */
    private static final DetailControllerImpl detailControllerImpl;
    public static DetailControllerImpl getInstance(){
        return detailControllerImpl;
    }
    static {
        detailControllerImpl = new DetailControllerImpl();
    }
    private DetailControllerImpl(){}
    /*Singleton ended*/

    /*
     * for dependency injection.
     */
    private SQLiteDatabase db ;
    private Cursor cursor;

    @Override
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    //private static final String INSERT_QUERY = "insert into details (foreign_id,text,repeat_no,remain_no) ";
    /* dependency injection ended */
    @Override
    public int insertData(Detail data)  throws Exception{
        ContentValues values = new ContentValues();
        values . put ( "foreign_id" , data.getForeignKey() );
        values . put ( "text" , data.getTaskName() );
        values . put ( "remain_no" , data.getRepeat() );
        values . put ( "repeat_no" , data.getRepeat() );
        return (int)db.insert("details", null, values);
    }

    @Override
    public List<Detail> getAllData(int foreignKey) throws Exception {
        List<Detail> lists = new ArrayList<Detail>();
        cursor.moveToFirst();

        do {


            Detail detail = new DetailData();
            detail.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            detail.setTaskName(cursor.getString(cursor.getColumnIndex("text")));
            detail.setRemain(cursor.getInt(cursor.getColumnIndex("remain_no")));
            detail.setRepeat(cursor.getInt(cursor.getColumnIndex("repeat_no")));
            detail.setForeignKey(cursor.getInt(cursor.getColumnIndex("foreign_id")));
            detail.setPercent(cursor.getInt(cursor.getColumnIndex("percent"))*1.0);
            Log.e("cursor.moveToNext()","detailControllerImpl percent"+detail.getPercent());
            lists.add(detail);

        } while(cursor.moveToNext());
        return lists;
    }

    @Override
    public void updateRemainAndPercent(Detail detail) throws Exception {
        String query = "update details set percent="+detail.getPercent()+",remain_no="+detail.getRemain()+" where _id="+detail.getId();
        db.execSQL(query);
    }
}
