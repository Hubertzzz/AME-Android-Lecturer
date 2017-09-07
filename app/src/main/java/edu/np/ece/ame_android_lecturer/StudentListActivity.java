package edu.np.ece.ame_android_lecturer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Fragment.AttendanceHistoryListFragment;
import edu.np.ece.ame_android_lecturer.Fragment.StudentlistFragment;

public class StudentListActivity extends FragmentActivity {
    ViewPager viewPager;
    List<Fragment> fragmentList;
    FragmentManager manager;
    private FragmentPagerItemAdapter adapter;
    private PagerTabStrip pagerTabStrip;
    private String[] tabNames=new String[2];
    private String lesson_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        viewPager=(ViewPager)this.findViewById(R.id.viewPage);
        pagerTabStrip=(PagerTabStrip)this.findViewById(R.id.viewpagertab);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

       // tabNames=getResources().getStringArray(R.array.tabs);

        fragmentList = new ArrayList<Fragment>();
        Intent intent = getIntent();
        if(intent!=null){
            Fragment fragment = new StudentlistFragment();
            manager = getSupportFragmentManager();
            FragmentTransaction transaction= manager.beginTransaction();

            tabNames[0]=intent.getStringExtra("Group_name");
            tabNames[1]="Attendance";

            lesson_id=intent.getStringExtra("lesson_id");

            fragmentList.add(fragment);
            fragmentList.add(new AttendanceHistoryListFragment());
            ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(),fragmentList);
            viewPager.setAdapter(viewpagerAdapter);
        }

    }

    public class ViewpagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        public ViewpagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            fragmentList=fragments;
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);

        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabNames[position];
        }

        @Override
        public int getCount() {
            //   return fragmentList!=null?fragmentList.size():0;
            return fragmentList.size();
        }

    }
}
