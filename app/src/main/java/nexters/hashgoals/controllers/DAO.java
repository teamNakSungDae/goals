package nexters.hashgoals.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import nexters.hashgoals.models.Detail;

import java.util.List;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public interface DAO<T extends Detail>{

    void setDb(SQLiteDatabase db);
    void setCursor(Cursor cursor);
    List<T> getAllData(int id) throws Exception;
    int insertData(T data) throws Exception;

}