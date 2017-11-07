package edu.np.ece.ame_android_lecturer;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.np.ece.ame_android_lecturer.JobScheduler.BeaconJobScheduler;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.OrmLite.DatabaseManager;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dellpc on 10/16/2017.
 */

public class BeaconScanActivation extends Application implements BootstrapNotifier {

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager mBeaconManager;

    ArrayList<Region> regionList = new ArrayList<>();
    public static List<TimetableResult> timetableResultList;
    public static List<StudentInfo> studentInfoList;

    public String record_time;

    final BeaconScanActivation tmp = this;

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getBaseContext());
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconManager.setBackgroundMode(true);
        backgroundPowerSaver = new BackgroundPowerSaver(getBaseContext());

        mBeaconManager.setBackgroundScanPeriod(50001);
        mBeaconManager.setBackgroundBetweenScanPeriod(300001);

        DatabaseManager.init(getBaseContext());

        mHandler = new Handler();

        startRepeatingTask();


    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int status, Region region) {
        final SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

        String isLogin = pref.getString("isLogin","false");

        if (isLogin.equals("true") && regionList != null){

            if(status == 1){
                try{
                    final String auCode = pref.getString("authorizationCode", null);
                    final String lecturerId = pref.getString("lecturer_id", null);

                    String[] studentId_lessonDateId = region.getUniqueId().split(";");
                    if (studentId_lessonDateId.length > 0){
                        String detectedStudentId = studentId_lessonDateId[0];
                        String lessonDateId = studentId_lessonDateId[1];

                        if (timetableResultList != null){
                            JsonParser parser = new JsonParser();
                            JsonObject obj = parser.parse("{"+
                            "\"data\": "+
                            "["+
                            "{"+
                            "\"lesson_date_id\":\"" + lessonDateId + "\"," +
                            "\"student_id_1\":\"" + lecturerId + "\"," +
                            "\"student_id_2\":\"" + detectedStudentId + "\"" +
                            "}"+
                            "]"+
                            "}").getAsJsonObject();


                            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
                            Call<String> call = client.takeAttendance(obj);
                            call.enqueue(new ServerCallBack<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("Attendance taking successfully")){
                                        Toast.makeText(getBaseContext(),"Taking attendance success", Toast.LENGTH_SHORT).show();
                                        Log.d("test attendance","success");
                                    }
                                    if (response.body().contains("Late")){
                                        Toast.makeText(getBaseContext(), "Late attendance", Toast.LENGTH_SHORT).show();
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

    private int mInterval = 43200000;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            final SharedPreferences pref = getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);

            String isLogin = pref.getString("isLogin", "false");

            if (isLogin.equals("true")){
                mInterval = 43200000;

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (timetableResultList != null){
                            ComponentName serviceName = new ComponentName(getBaseContext(), BeaconJobScheduler.class);

                            String userMajor = pref.getString("major","");
                            String userMinor = pref.getString("minor","");

                            if (!userMajor.equals("") || !userMinor.equals("")){

                                JobScheduler jobScheduler =
                                        (JobScheduler) getBaseContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                                jobScheduler.cancelAll();

                                for (TimetableResult aSubject_time : timetableResultList){
                                    try{
                                        String timeEnd = aSubject_time.getLesson_date() + " " + aSubject_time.getLesson().getEnd_time();
                                        Date timeEnd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeEnd);
                                        Calendar calendar1 = Calendar.getInstance();
                                        calendar1.setTime(timeEnd2);

                                        Date timeNow = new Date();
                                        Calendar calendar2 = Calendar.getInstance();
                                        calendar2.setTime(timeNow);

                                        if (calendar2.getTime().before(calendar1.getTime())){
                                            String timeStart = aSubject_time.getLesson_date() + " " + aSubject_time.getLesson().getStart_time() ;
                                            Date timeStart2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeStart);
                                            long timeInterval = timeStart2.getTime() - timeNow.getTime();

                                            if (timeInterval < 0){
                                                timeInterval = 60000;
                                            }

                                            //数据存入jobScheduler
                                            PersistableBundle bundle1 = new PersistableBundle();
                                            bundle1.putString("subject-uuid",aSubject_time.getLessonBeacon().getUuid());
                                            bundle1.putString("user-major",userMajor);
                                            bundle1.putString("user-minor",userMinor);
                                            bundle1.putString("user-lesson",aSubject_time.getLesson().getFacility() + " " + aSubject_time.getLesson().getCatalog_number() + " "
                                            + aSubject_time.getLesson_date() + " " + aSubject_time.getLesson().getStart_time());

                                            int min = 5000;
                                            int max = 20000;
                                            Random r = new Random();
                                            long randomTime = r.nextInt(max - min) + min;
                                            timeInterval = timeInterval + randomTime;

                                            JobInfo.Builder builder = new JobInfo.Builder(Integer.parseInt(aSubject_time.getLesson().getId()),serviceName);
                                            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                                            builder.setMinimumLatency(timeInterval);
                                            builder.setExtras(bundle1);
                                            jobScheduler.schedule(builder.build());


                                            for (StudentInfo aStudent : studentInfoList ){
                                                if (aStudent.getBeacon_ser().getMajor().equals("")){
                                                    String studentMajor = aStudent.getBeacon_ser().getMajor();
                                                    String studentMinor = aStudent.getBeacon_ser().getMinor();
                                                    Region region = new Region(aStudent.getId() + ";" +
                                                    aSubject_time.getLesson().getId() + ";student",
                                                            Identifier.parse(aSubject_time.getLessonBeacon().getUuid()),
                                                            Identifier.parse(studentMajor),Identifier.parse(studentMinor));
                                                    if(!regionList.contains(region)){
                                                        regionList.add(region);
                                                    }
                                                }
                                            }

                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }


                        }

                        //init jobscheduler
                        Intent startServiceIntent = new Intent(getBaseContext(), BeaconJobScheduler.class);
                        startService(startServiceIntent);

                        regionBootstrap = new RegionBootstrap(tmp, regionList);
                    }
                },10000);
            }else{
                //if logout, stop monitoring to release memory.
                if (regionList != null && regionList.size()>0){
                    for (Region tmp : regionList){
                        try{
                            mBeaconManager.stopMonitoringBeaconsInRegion(tmp);
                        }catch(RemoteException e){
                            e.printStackTrace();
                        }
                    }
                }

                regionList.clear();

                mInterval = 10000;
            }

            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };
    public void startRepeatingTask(){
        mStatusChecker.run();
    }


}
