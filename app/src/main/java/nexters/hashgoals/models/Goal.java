package nexters.hashgoals.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by flecho on 2017. 1. 31..
 */

@Getter
@Setter
@NoArgsConstructor
public class Goal implements Parcelable {

    private long mId;

    private String mTitle;
    private String mDaysOfWeek;


    public void setMDaysOfWeekOf(String[] daysOfWeek) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    /* This method is used to regenerate the object. All Parcelables must have a CREATOR that implements
    these two methods. */
    public static final Parcelable.Creator<Goal> CREATOR = new Parcelable.Creator<Goal>() {
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[] {this.mTitle, this.mDaysOfWeek});
    }

    private Goal(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);

        // The order needs to be the same as in writeToParcel() method.
        this.mTitle = data[0];
        this.mDaysOfWeek = data[1];
    }


}
