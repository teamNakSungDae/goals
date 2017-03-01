package nexters.hashgoals.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nexters.hashgoals.helpers.DatabaseHelper;
import nexters.hashgoals.models.Detail;
import nexters.hashgoals.models.DetailData;

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
        values . put ( "foreign" , data.getId() );
        values . put ( "text" , data.getValue() );
        values . put ( "recycle_no" , 50 );
        return (int)db.insertOrThrow("details", null, values);

    }

    @Override
    public List<Detail> getAllData(Detail data) throws Exception {
        List<Detail> lists = new ArrayList<Detail>();
        do {
            Detail detail = new DetailData();
            detail.setId( cursor.getInt( cursor.getColumnIndex("_id")));
            detail.setValue( cursor . getString ( cursor . getColumnIndex ( "text" ) ) );
            detail.setRemainNumber( cursor . getInt ( cursor . getColumnIndex ( "recycle_no" ) ) );
            lists.add(detail);
        } while ( cursor.moveToNext() );
        return lists;
    }
}
