package nexters.hashgoals.models;

import java.io.Serializable;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public class DetailPercent implements Detail,Serializable {

    private int id;
    /**
     * The value mean " percent value "
     */
    private String value;

    public DetailPercent(String value) {
        this.value = value;
    }

    public DetailPercent() {
    }

    @Override
    public void setId(int id) {
        this.id=id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * not use.
     * @return
     */
    @Override
    public int getRemainNumber() {
        return 0;
    }

    @Override
    public void setRemainNumber(int remainNumber) {

    }
}
