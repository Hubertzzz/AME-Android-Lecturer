package edu.np.ece.ame_android_lecturer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dellpc on 9/4/2017.
 */

public class BeaconScanActivation extends Activity implements BootstrapNotifier {

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager mBeaconmanager;

    ArrayList<Region> regionList = new ArrayList();
    public static List<TimetableResult> timetableList;

    public String record_time;

    final BootstrapNotifier tmp=this;
    private Handler mHandler;

    FragmentManager manager;



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

        if(isLogin.equals("true") && regionList != null){
            //in region
            if(status == 1){
                try{
                    final String auCode = pref.getString("authorizationCode", null);
                    final String lecturerId = pref.getString("lecturer_id", null);

                    String[] studentId_LessonDateId = region.getUniqueId().split(";");
                    if(studentId_LessonDateId.length > 0){
                        //get data of detected student
                        String detectedStudentId = studentId_LessonDateId[0];
                        String lessonDateId = studentId_LessonDateId[1];

                        if (timetableList != null){
                            JsonParser parser = new JsonParser();
                            JsonObject obj = parser.parse("{" +
                                    "\"data\": " +
                                    "["+
                                    "{"+
                                    "\"lesson_date_id\":\"" + lessonDateId + "\"," +
                                    "\"student_id_1\":\"" + lecturerId + "\"," +
                                    "\"student_id_2\":\"" + detectedStudentId + "\"" +
                                    "}" +
                                    "]" +
                                    "}").getAsJsonObject();


                            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
                            Call<String> call = client.takeAttendance(obj);
                            call.enqueue(new ServerCallBack<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.body().equals("Attendance taking successfully")){

                                        Toast.makeText(getBaseContext(), "Taking attendance success", Toast.LENGTH_SHORT).show();

                                        Preferences.studentNotify(getBaseContext(),"Taking attendance success","Student's attendance has been recorded.",Integer.parseInt(lecturerId));
                                        Log.d("test attendance","success");
                                    }

                                    if (response.body().contains("Late")){
                                        Toast.makeText(getBaseContext(),"Late attendance", Toast.LENGTH_SHORT).show();
                                        Preferences.studentNotify(getBaseContext(),"Late attendance","You're late for the class", Integer.parseInt(lecturerId));
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t ){
                                    super.onFailure(call, t);
                                    Log.d("test attendance","failed");
                                }
                            });
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    private int mInterval = 43200000;

    Runnable mStatusChecker = new Runnable(){

        @Override
        public void run() {

            final SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag,Preferences.SharedPreferences_ModeTag);

            String isLogin = pref.getString("isLogin","false");

            if (isLogin.equals("true")){

                mInterval = 43200000;

                mHandler.postDelayed(new )

            }
        }
    }
}
