package edu.np.ece.ame_android_lecturer;

import android.app.Application;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.OrmLite.DatabaseManager;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dellpc on 11/6/2017.
 */

public class BeaconScanActivation extends Application implements BootstrapNotifier {

    private BeaconManager beaconManager;
    private BackgroundPowerSaver backgroundPowerSaver;
    private RegionBootstrap regionBootstrap;


    ArrayList<Region> regionList = new ArrayList();
    public static List<TimetableResult> timetableResultList;

    final BootstrapNotifier tmp = this;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getBaseContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        backgroundPowerSaver = new BackgroundPowerSaver(getBaseContext());
        beaconManager.setBackgroundMode(true);

        beaconManager.setBackgroundScanPeriod(50001);
        beaconManager.setBackgroundBetweenScanPeriod(300001);

        DatabaseManager.init(getBaseContext());

        mHandler = new Handler();

    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int status, Region region) {
        final SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag,Preferences.SharedPreferences_ModeTag);

        String isLogin = pref.getString("isLogin","false");

        if (isLogin.equals("true") && regionList!=null ){
            //in the beacon region
            if(status == 1){
                try{
                    final String auCode = pref.getString("authorizationCode",null);
                    final String lecturerId = pref.getString("lecturer_id",null);

                    String[] studentId_lessonDateId = region.getUniqueId().split(";");

                    if(studentId_lessonDateId.length > 0){
                        //Get data of detected student
                        String detectedStudentId = studentId_lessonDateId[0];
                        String lessonDateId = studentId_lessonDateId[1];

                        if (timetableResultList != null){
                            JsonParser parser = new JsonParser();
                            JsonObject obj = parser.parse("{" +
                                    "\"data\": " +
                                    "[" +
                                    "{" +
                                    "\"lesson_date_id\":\"" + lessonDateId + "\"," +
                                    "\"student_id\":\"" + detectedStudentId + "\"," +
                                    "\"lecturer_id\":\"" + lecturerId + "\"" +
                                    "}" +
                                    "]" +
                                    "}").getAsJsonObject();


                            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
                            Call<String> call = client.createLecturerAttendance(obj);
                            call.enqueue(new ServerCallBack<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("Attendance taking successfully")) {
                                        Toast.makeText(getBaseContext(), "Taking Attendance Suceess", Toast.LENGTH_SHORT).show();
                                        Preferences.lecturerNotify(getBaseContext(), "Taking Attendance Success", "Attendance has been recorded", Integer.parseInt(lecturerId));
                                        Log.d("test attendance", "success");

                                    }

                                    if (response.body().contains("Late")) {
                                        Toast.makeText(getBaseContext(), "Late Attendance", Toast.LENGTH_SHORT).show();
                                        Preferences.lecturerNotify(getBaseContext(), "Late Attendance", "Late", Integer.parseInt(lecturerId));
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    super.onFailure(call, t);
                                    Log.d("test attendance","failed");
                                }

                            });
                        }


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    // this will re-run after every 12 hours
    private int mInterval = 43200000;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            final SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

            String isLogin = pref.getString("isLogin", "false");

            if (isLogin.equals("true")){
                // if user has already login, this part only 2 times per day
                mInterval = 43200000;

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (timetableResultList != null){
                            ComponentName serviceName = new ComponentName(getBaseContext(),BeaconJob)
                        }

                    }
                },10000);
            }
        }
    }
}
