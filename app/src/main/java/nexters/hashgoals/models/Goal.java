package nexters.hashgoals.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by flecho on 2017. 1. 31..
 */

@Getter
@NoArgsConstructor
public class Goal {
    private long mId;

    @Setter
    private String mTitle;

    private String mDaysOfWeek;


    public void setMDaysOfWeek(List<String> daysOfWeek) {
        this.mDaysOfWeek = StringUtils.join(daysOfWeek, ",");
    }

    public String[] parseDaysOfWeek() {
        return StringUtils.split(mDaysOfWeek, ",");
    }

    @Override
    public String toString() { return mTitle; }
    /*
    * The default implementation of ArrayAdapter<T>.getView(...) relies on toString().
    * It inflates the layout, finds the correct Crime object, and then calls toString() on the object
    * to populate the TextView.
    * */


}
