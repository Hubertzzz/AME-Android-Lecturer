package edu.np.ece.ame_android_lecturer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

     //   checkPermissions();

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
}



