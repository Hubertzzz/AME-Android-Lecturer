package edu.np.ece.ame_android_lecturer;

import android.app.Application;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

/**
 * Created by dellpc on 11/13/2017.
 */

public class BeaconScanActivation extends Application implements BootstrapNotifier {

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager mBeaconmanager;

    final BootstrapNotifier tmp = this;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Start","boost up");
        mBeaconmanager = BeaconManager.getInstanceForApplication(getBaseContext());
        mBeaconmanager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        mBeaconmanager.setBackgroundMode(true);
        backgroundPowerSaver = new BackgroundPowerSaver(getBaseContext());

        mBeaconmanager.setBackgroundScanPeriod(5000l);
        mBeaconmanager.setBackgroundBetweenScanPeriod(30000l);

        Region region = new Region("all-beacon-region",null,null,null);
        regionBootstrap = new RegionBootstrap(tmp,region);


    }

    @Override
    public void didEnterRegion(Region region) {
            Log.e("this","Saw a beacon entered");
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
            Log.e("beacon","detected");

    }
}
