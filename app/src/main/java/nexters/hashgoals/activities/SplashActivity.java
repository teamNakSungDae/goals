package nexters.hashgoals.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import nexters.hashgoals.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by clsan on 2017. 2. 18..
 * Add access token tracking for facebook by clsan on 2017. 2. 19..
 */

public class SplashActivity extends Activity {
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NotoSansCJKkr-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                // TODO(clsan) : Access token이 만료되었을 때 어떻게 관리해줘야 하는지 리서치 해볼 것
                AccessToken.setCurrentAccessToken(currentAccessToken);
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

//        if (accessToken == null) {
        startActivity(FacebookLoginActivity.class);
//        } else {
//            startActivity(GoalActivity.class);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    private void startActivity(Class T) {
        Intent intent = new Intent(this, T);
        startActivity(intent);
        finish();
    }
}