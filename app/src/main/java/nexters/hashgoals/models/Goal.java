package nexters.hashgoals.models;

import java.util.UUID;

/**
 * Created by flecho on 2017. 1. 31..
 */

public class Goal {
    private long mId;
    private String mTitle;

    public Goal() {
        // Generate unique identifier
    }

    @Override
    public String toString() { return mTitle; }
    /*
    * The default implementation of ArrayAdapter<T>.getView(...) relies on toString().
    * It inflates the layout, finds the correct Crime object, and then calls toString() on the object
    * to populate the TextView.
    * */

    public long getId() {
        return mId;
    }

    public String getTitle() { return mTitle; }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
