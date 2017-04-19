package nexters.hashgoals.controllers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    /* dependency injection ended */
    @Override
    public int insertData(Detail data)  throws Exception{
        ContentValues values = new ContentValues();
        values . put ( "foreign_id" , data.getId() );
        values . put ( "text" , data.getTaskName() );
        values . put ( "recycle_no" , data.getRecycleNo() );
        values . put ( "remain_no" , data.getRecycleNo() );
        return (int)db.insertOrThrow("details", null, values);
    }

    @Override
    public List<Detail> getAllData(int id) throws Exception {
        List<Detail> lists = new ArrayList<Detail>();
        do {
            Detail detail = new DetailData();
            detail.setId( cursor.getInt( cursor.getColumnIndex("_id")));
            detail.setTaskName( cursor . getString ( cursor . getColumnIndex ( "text" ) ) );
            detail.setRecycleNo( cursor . getInt ( cursor . getColumnIndex ( "remain_no" ) ) );
            detail.setRecycleNo( cursor . getInt( cursor.getColumnIndex( "recycle_no" )));
            //detail.setPe( cursor . getInt( cursor. getColumnIndex( "percent" )));

            lists.add(detail);
        } while ( cursor.moveToNext() );
        return lists;
    }
}
