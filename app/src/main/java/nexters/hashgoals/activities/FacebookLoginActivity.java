package nexters.hashgoals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nexters.hashgoals.R;

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
        setContentView(R.layout.activity_facebook_login);

        ButterKnife.bind(this);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, this);
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
}