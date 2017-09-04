package edu.np.ece.ame_android_lecturer;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import edu.np.ece.ame_android_lecturer.Model.LoginResult;

/**
 * Created by MIYA on 31/08/17.
 */

public class Preferences {
    public static final String SharedPreferencesTag = "ATK_BLE_Preferences";
    public static final int SharedPreferences_ModeTag = Context.MODE_PRIVATE;

    public static final int LIST_ITEM_TYPE_1 = 0;
    public static final int LIST_ITEM_TYPE_2 = 1;
    public static final int LIST_ITEM_TYPE_COUNT = 2;

    private static final int CODE_INCORRECT_USERNAME = 10;
    private static final int CODE_INCORRECT_PASSWORD = 11;
    private static final int CODE_INCORRECT_DEVICE = 12;
    private static final int CODE_UNVERIFIED_EMAIL = 13;
    public static final int CODE_UNVERIFIED_DEVICE = 14;
    private static final int CODE_UNVERIFIED_EMAIL_DEVICE = 15;
    private static final int CODE_INVALID_ACCOUNT = 16;

    public static ProgressDialog loading;
    public static boolean isShownLoading = false;

    public static Activity activity;

    public static void showLoading(final Activity activity, final String title, final String message) {
        try {
            if (!isShownLoading) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading = ProgressDialog.show(activity, title, message, false, false);
                        isShownLoading = true;
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void dismissLoading() {
        try {
            if (isShownLoading) {

                loading.dismiss();
                isShownLoading = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setActivity(Activity act) {
        activity = act;
    }

    public static Activity getActivity() {
        return activity;
    }


    public static boolean checkInternetOn() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            return false;
        } else {
            return true;
        }

    }

    public static void setStudentInfo(LoginResult _lecturerInfo){
        SharedPreferences pref = activity.getSharedPreferences("ATK_BLE_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("isLogin","true");
        editor.putString("isStudent","true");
        editor.putString("lecturer_id",_lecturerInfo.getId());
        editor.putString("lecturer_name",_lecturerInfo.getName());
        editor.putString("lecturer_acad",_lecturerInfo.getAcad());
        editor.putString("authorizationCode", "Bearer" + _lecturerInfo.getToken());
        editor.putString("major",_lecturerInfo.getMajor());
        editor.putString("minor",_lecturerInfo.getMinor());

        editor.apply();
    }

    public static void studentNotify(Context context, String title, String content, int id){

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.checklist)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.icon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent intent = new Intent(context, LogInActivity.class);

        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

}
