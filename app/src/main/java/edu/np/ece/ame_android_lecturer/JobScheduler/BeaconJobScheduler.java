package edu.np.ece.ame_android_lecturer.JobScheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

import edu.np.ece.ame_android_lecturer.Preferences;

/**
 * Created by dellpc on 10/23/2017.
 */

public class BeaconJobScheduler extends JobService {

    private JobAsyncTask mJobAsyncTask;
    @Override
    public boolean onStartJob(JobParameters params) {
        mJobAsyncTask = new JobAsyncTask();
        mJobAsyncTask.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mJobAsyncTask != null){
            if(mJobAsyncTask.isCancelled()){
                return true;
            }
            mJobAsyncTask.cancel(true);
        }
        return false;
    }

    private class JobAsyncTask extends AsyncTask<JobParameters, Void, JobParameters>{
        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            if (params[0].getExtras().getString("subject-uuid")!= null){
                final Beacon.Builder beaconBuilder = new Beacon.Builder();
                beaconBuilder.setId1(params[0].getExtras().getString("subject-uuid"));
                beaconBuilder.setId2(params[0].getExtras().getString("user-major"));
                beaconBuilder.setId3(params[0].getExtras().getString("user-minor"));


                beaconBuilder.setManufacturer(0x004C);
                beaconBuilder.setTxPower(-59);
                beaconBuilder.setDataFields(Arrays.asList(new Long[] {0l}));
                BeaconParser beaconParser = new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
                final BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);

                beaconTransmitter.startAdvertising(beaconBuilder.build());

                SharedPreferences pref = getBaseContext().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("isActivateBeacon","true");
                editor.apply();
                //一直transmit的话就cmt掉sleep
                //SystemClock.sleep(30000);
                beaconTransmitter.stopAdvertising();
                editor.putString("isActivateBeacon","false");
                editor.apply();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobFinished(jobParameters,true);
        }
    }
}
