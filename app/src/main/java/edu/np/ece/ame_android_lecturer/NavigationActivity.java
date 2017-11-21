package edu.np.ece.ame_android_lecturer;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import edu.np.ece.ame_android_lecturer.Fragment.AttendanceTakenFragment;
import edu.np.ece.ame_android_lecturer.Fragment.GroupFragment;
import edu.np.ece.ame_android_lecturer.Fragment.LecturerInfoFragment;
import edu.np.ece.ame_android_lecturer.Fragment.MonitorListFragment;
import edu.np.ece.ame_android_lecturer.Fragment.TimetableFragment;


public class NavigationActivity extends AppCompatActivity {

    private Context context;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setLayoutContent();

        context = this;

        Preferences.setActivity(this);

        checkPermissions();

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void setLayoutContent(){
        try{
            BottomBar bottomBar = (BottomBar)findViewById(R.id.bottomBar);
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    Fragment fragment = null;
                    String title = "";
                    switch (tabId) {
                        case R.id.tab_schedule:
                            fragment = new TimetableFragment();
                            title = "Schedules";
                            break;
                        case R.id.tab_group:
                            fragment = new GroupFragment();
                            title = "Group";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_now:

                            fragment = new AttendanceTakenFragment();
                            title = "Lesson Now";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_monitor:
                            fragment = new MonitorListFragment();
                            title = "Monitor Attendance Now";
                            getSupportActionBar().setSubtitle(null);
                            break;

                        case R.id.tab_more:
                            fragment=new LecturerInfoFragment();
                            title = "More";
                            getSupportActionBar().setSubtitle(null);
                            break;

                        default:
                            fragment = new TimetableFragment();
                            title = "Schedules";
                            break;
                    }
                    setActionBarTitle(title);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(@IdRes int tabId) {
                    android.app.Fragment fragment = null;
                    String title = null;
                    switch (tabId) {
                        case R.id.tab_schedule:
                            fragment = new TimetableFragment();
                            title = "Schedules";
                            break;
                        case R.id.tab_group:
                            fragment = new GroupFragment();
                            title = "Group";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_now:
                            fragment = new AttendanceTakenFragment();
                            title = "Lesson Now";
                            getSupportActionBar().setSubtitle(null);
                            break;
                        case R.id.tab_monitor:
                            fragment = new MonitorListFragment();
                            title = "Monitor Attendance Now";
                            getSupportActionBar().setSubtitle(null);
                            break;

                        case R.id.tab_more:
                            fragment= new LecturerInfoFragment();
                            title = "More";
                            getSupportActionBar().setSubtitle(null);
                            break;

                        default:
                            fragment = new TimetableFragment();
                            title = "Schedules";
                            break;
                    }

                    setActionBarTitle(title);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

    }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 9);
        }
    }


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || !bluetoothAdapter.isEnabled()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs bluetooth service and location access");
                builder.setMessage("Please activate bluetooth service and grant location access so this app can start taking attendance.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        initBluetooth();
                        ActivityCompat.requestPermissions(Preferences.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();

            }
        }
        else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Gentle Remind:Bluetooth service and location access");
            builder.setMessage("Please activate bluetooth service and grant location access so this app can start taking attendance.");
            builder.setNegativeButton("Activate Already", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, final int i) {
                    return;
                }
            });
            builder.setPositiveButton("Activate Now",

                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {

                            Intent intent=new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    }

            );

            builder.create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //On android 6.0+ we need this permission to run bluetooth scan
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to take attendance.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }



}



