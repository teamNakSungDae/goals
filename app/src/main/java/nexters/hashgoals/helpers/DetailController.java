package nexters.hashgoals.helpers;

import android.content.Context;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public class DetailController {
    private DatabaseHelper databaseHelper;
    public DetailController(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

}
