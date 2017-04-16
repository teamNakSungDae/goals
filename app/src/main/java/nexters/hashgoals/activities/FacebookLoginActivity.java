package nexters.hashgoals.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;
import nexters.hashgoals.R;
import nexters.hashgoals.fonts.FontsLoader;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by yoon on 2017. 2. 18..
 */

public class FacebookLoginActivity extends AppCompatActivity
        implements FacebookCallback<LoginResult>
{
    @BindView(R.id.button_login_facebook) LoginButton loginButton;
    @BindView(R.id.button_login_skip) Button skipButton;

    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_login_facebook);

        ButterKnife.bind(this);
        setFonts();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, this);
    }

    private void setFonts() {
        loginButton.setTypeface(FontsLoader.getTypeface(getApplicationContext(), FontsLoader.N_S_MEDUIM));
        skipButton.setTypeface(FontsLoader.getTypeface(getApplicationContext(), FontsLoader.N_S_MEDUIM));
    }

    // FacebookCallback<LoginResult>
    @Override
    public void onSuccess(LoginResult loginResult) {
        startGoalActivity();
    }

    // FacebookCallback<LoginResult>
    @Override
    public void onCancel() {
    }

    // FacebookCallback<LoginResult>
    @Override
    public void onError(FacebookException error) {
    }

    @OnClick(R.id.button_login_skip)
    void onSkipButtonClicked() {
        startGoalActivity();
    }

    private void startGoalActivity() {
        Intent intent = new Intent(this, GoalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}