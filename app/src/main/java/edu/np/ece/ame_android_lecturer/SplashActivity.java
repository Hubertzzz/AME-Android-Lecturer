package edu.np.ece.ame_android_lecturer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import edu.np.ece.ame_android_lecturer.Utils.ConnectivityUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by MIYA on 23/10/17.
 */

public class SplashActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preferences.setActivity(this);
        SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
        String islogin = pref.getString("isLogin", "");
        if (!islogin.equals("true")) {
            if (!ConnectivityUtils.isConnected(this)) {
                ConnectivityUtils.showConnectionFailureDialog(this);
                return;
            }
            obtainedAuCode();
        } else {
            startAuthenticatedArea();
        }

    }

    private void obtainedAuCode() {
        Preferences.showLoading(SplashActivity.this, "Initialize", "Checking authentication...");
        try {
            SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);
            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);

            String expand = new String("lesson,venue,lesson_date,beacon_lesson");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try {
                        Preferences.dismissLoading();

                        int code = response.code();
                        if (code == 200) {
                            startAuthenticatedArea();
                        } else {
                            startLogin();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
//        if (requestCode == REQUEST_ENABLE_BT) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//
//            }
//            else
//            {
//                finish();
//            }
//        }
    }

    private void startLogin() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    private void startAuthenticatedArea() {
        SharedPreferences pref = Preferences.getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("isActivateBeacon", "false");
        editor.apply();
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }
}
