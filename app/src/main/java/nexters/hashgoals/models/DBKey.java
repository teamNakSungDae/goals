package nexters.hashgoals.models;

/**
 * Created by kwongiho on 2017. 4. 20..
 */

public interface DBKey {
    int getId();
    void setId(int id);
    int getForeignKey();
    void setForeignKey(int foreignKey);
}
