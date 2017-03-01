package nexters.hashgoals.models;

import java.io.Serializable;



/**
 * Created by kwongiho on 2017. 2. 23..
 */

public class DetailData implements Detail , Serializable{

    private int id;
    /**
     * the value is mean " the task name."
     */
    private String value;
    private int remainNumber;

    public DetailData(String value, int remainNumber) {
        this.value = value;
        this.remainNumber = remainNumber;
    }

    public DetailData() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(int remainNumber) {
        this.remainNumber = remainNumber;
    }

}
