package nexters.hashgoals.controllers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import nexters.hashgoals.models.Detail;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public interface DAO<T extends Detail>{

    void setDb(SQLiteDatabase db);
    void setCursor(Cursor cursor);
    List<T> getAllData(int foreignKey) throws Exception;
    int insertData(T data) throws Exception;
    void updateRemainAndPercent(Detail detail) throws Exception ;

}