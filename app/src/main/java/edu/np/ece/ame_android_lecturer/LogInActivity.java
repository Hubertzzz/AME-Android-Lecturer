package edu.np.ece.ame_android_lecturer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.np.ece.ame_android_lecturer.Model.LoginInfo;
import edu.np.ece.ame_android_lecturer.Model.LoginResult;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_FORGOT_PASSWORD = 1;

    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_forgotPass)
    TextView _forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _forgotPasswordLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Forgot Password activity
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivityForResult(intent, REQUEST_FORGOT_PASSWORD);
            }
        });

       /* SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("isLogin", "false");
        editor.apply();*/
    }
    public void login() {

        if (!validate()) {
            _loginButton.setEnabled(true);
            return;
        }

        _loginButton.setEnabled(false);

        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();

        loginAction(username, password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    public void onBackPressed() {
        // disable going back to the NavigationActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
        Preferences.dismissLoading();
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LogInActivity.this).create();
        alertDialog.setTitle("Login Failed");
        if (checkInternetOn()) {
            alertDialog.setMessage("Please check username and password again.");

        } else {
            alertDialog.setMessage("Please turn on internet connection.");

        }
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public boolean checkInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            return false;
        } else {
            return true;
        }

    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() || username.length() < 4 || username.length() > 255) {
            _usernameText.setError("Enter a valid username address");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 255) {
            _passwordText.setError("Password is required at least 6 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public void loginAction(String username, String password) {


        //Preferences.showLoading(this, "Log In", "Authenticating...");
        final Activity act = this;
        ServerApi client = ServiceGenerator.createService(ServerApi.class);
        LoginInfo up = new LoginInfo(username, password, this);
        Call<LoginResult> call = client.login(up);
        call.enqueue(new ServerCallBack<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                // Preferences.setActivity(act);
                // Preferences.dismissLoading();
                int messageCode = response.code();
                if (messageCode == 200) // SUCCESS
                {
                    startNavigation();
                    onLoginSuccess();

                } else {
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                onLoginFailed();
            }
        });

    }
    private void startNavigation() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("isLogin", "true");
        editor.apply();
    }
}
