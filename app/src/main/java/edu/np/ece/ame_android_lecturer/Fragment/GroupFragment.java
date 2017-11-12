package edu.np.ece.ame_android_lecturer.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Adapter.GroupListAdapter;
import edu.np.ece.ame_android_lecturer.Adapter.TimetableListAdapter;
import edu.np.ece.ame_android_lecturer.LogInActivity;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import edu.np.ece.ame_android_lecturer.StudentListActivity;
import retrofit2.Call;
import retrofit2.Response;


public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View myView;
    private List<TimetableResult> timetableList;
    private List<String> grouplist=new ArrayList<>();
    private List<String> cataloglist=new ArrayList<>();
    private List<String> arealist=new ArrayList<>();
    private Activity context;



    public GroupFragment() {
        // Required empty public constructor
    }



    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = this.getActivity();
    }

   /* public boolean isDifferentGroup(String group1,String group2){
        if(group1.compareToIgnoreCase(group2)==0){
            return false;

        }
        return true;
    }*/
    public  void addGroup(){

        for(int i=0;i<timetableList.size();i++) {
            String group = timetableList.get(i).getLesson().getClass_section();
            // if(i==0||isDifferentGroup(group,timetableList.get(i-1).getLesson().getClass_section())){
            grouplist.add(group);
            cataloglist.add(timetableList.get(i).getLesson().getCatalog_number());
            arealist.add(timetableList.get(i).getLesson().getSubject_area());
        //}


        }
        final ListView listView = (ListView) myView.findViewById(R.id.group_list);
        GroupListAdapter adapter = new GroupListAdapter(getActivity(), R.layout.item_tutorial_group,grouplist,cataloglist,arealist);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), StudentListActivity.class);
                intent.putExtra("Group_name",timetableList.get(i).getLesson().getClass_section());
                intent.putExtra("lesson_id",timetableList.get(i).getLesson_id()); //用的intent传递数据
                startActivity(intent);
                Toast.makeText(getActivity().getBaseContext(),timetableList.get(i).getLesson_id(),Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void loadGroupList(){
        Preferences.showLoading(getActivity(), "Group List", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            String expand = new String("lesson,venue,lesson_date,beacon_lesson");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try {
                        timetableList = response.body();
                        if (timetableList == null) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Detect another login.");
                            builder.setMessage("You will automatically sign out. Click here to sign in again.");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                            Preferences.clearLecturerInfo();
                                            Intent intent = new Intent(getActivity(), LogInActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                            builder.create().show();

                        } else {
                            addGroup();

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preferences.dismissLoading();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_group, container, false);
        loadGroupList();
        return myView;
    }


}
