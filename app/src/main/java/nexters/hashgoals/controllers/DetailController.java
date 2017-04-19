package nexters.hashgoals.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import nexters.hashgoals.helpers.DatabaseHelper;
import nexters.hashgoals.models.Detail;
/**
 * Created by kwongiho on 2017. 2. 24..
 */
public class DetailController implements DAO<Detail>,Controller{
    /*Singleton start*/

    static {
        instance = new DetailController();
    }

    private DetailController(){}

    public static DetailController getInstance(Context context) {
        if( databaseHelper == null ) {
            databaseHelper = DatabaseHelper.getInstance(context);
            dao = DetailControllerImpl.getInstance();
        }
        return instance;
    }

    /*Singleton ended*/

    @Override
    public void setDb(SQLiteDatabase db) {
        //Not used
        throw new RuntimeException("setDb method of "+getClass()+" is not supported method.");
    }

    private static final DetailController instance;

    private static DatabaseHelper databaseHelper;



    /**
     * for Dependency injection.
     */
    private static DAO dao;

    @Override
    public void setCursor(Cursor cursor) {
        throw new RuntimeException("setCursor method of "+getClass()+" is not supported method.");
    }

    @Override
    public int insertData(Detail data)  {
        int result = 0;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        dao.setDb(db);

        db.beginTransaction();


        try {
            result = dao.insertData(data);

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return result;
    }

    @Override
    public List<Detail> getAllData(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query= "Select * from details where foreign_id="+id+" order by percent";

        Cursor cursor = db.rawQuery(query, null);

//        if (!cursor.moveToFirst())
//            throw new RuntimeException("Not Found The Id : "+id);

        dao.setCursor(cursor);
        dao.setDb(db);

        try {

            return dao.getAllData(id);

        } catch(Exception e) {

            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }

}
