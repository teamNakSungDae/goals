package nexters.hashgoals.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by yoon on 2017. 2. 18..
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
        finish();
    }
}