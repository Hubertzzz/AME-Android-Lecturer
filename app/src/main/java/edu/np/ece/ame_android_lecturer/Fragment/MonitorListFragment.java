package edu.np.ece.ame_android_lecturer.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Adapter.ExpandableMonitorListAdapter;
import edu.np.ece.ame_android_lecturer.Adapter.MonitorListAdapter;
import edu.np.ece.ame_android_lecturer.LogInActivity;
import edu.np.ece.ame_android_lecturer.Model.ListAttendanceStatus;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.NavigationActivity;
import edu.np.ece.ame_android_lecturer.OrmLite.DatabaseManager;
import edu.np.ece.ame_android_lecturer.OrmLite.Monitor;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MonitorListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View myView;
    private String lesson_date_id;
    private List<ListAttendanceStatus> attendanceStatusList;
    private String lesson_id;
    private List<StudentInfo> studentList;
    private String student_id;
    private String status;
    private String student_name;
    private String new_status;


    ExpandableMonitorListAdapter expandableMonitorListAdapter;
    List<List<String>> expandedData;
    List<ListAttendanceStatus> group;
    List<String> choice;

    DatabaseManager manager;
    TextView tvlesson_name;
    TextView tvclass_section;
    List<Monitor> monitors;


    public MonitorListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonitorListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonitorListFragment newInstance(String param1, String param2) {
        MonitorListFragment fragment = new MonitorListFragment();
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




    }

    public void initAttendanceList(){

        //get lesson_id
        lesson_id=attendanceStatusList.get(0).getLesson().getId();
        //把stu_id按照递增排序
        Collections.reverse(attendanceStatusList);
        listStudent();

    }
    public void addgroup(){
        for(int i=0;i<attendanceStatusList.size();i++){
            group.add(attendanceStatusList.get(i));
            expandedData.add(choice);//每有一个group item 就新添加一组choice数组
        }

    }

    public void initStudentlist(){
        //final ListView listView = (ListView) myView.findViewById(R.id.monitorlist);

        final ExpandableListView listView =(ExpandableListView)myView.findViewById(R.id.monitorlist);
        final TextView tvlesson_name=(TextView)myView.findViewById(R.id.tvlesson_name);
        final TextView tvclass_section = (TextView)myView.findViewById(R.id.tvclass_section);

        expandedData=new ArrayList<List<String>>();
        group=new ArrayList<>();


        choice = new ArrayList<>();
        choice.add(0,"Present");
        choice.add(1,"Absent");
        choice.add(2,"5 mins late");
        choice.add(3,"10 mins late");
        choice.add(4,"15 mins late");
        choice.add(5,"20 mins late");
        choice.add(6,"25 mins late");
        choice.add(7,"30 mins late");
        choice.add(8,"35 mins late");
        choice.add(9,"40 mins late");
        choice.add(10,"45 mins late");
        choice.add(11,"1 hour late");
        choice.add(12,"more than 1 hour late");

        addgroup();

        expandableMonitorListAdapter =new ExpandableMonitorListAdapter(getActivity(),R.layout.item_monitor_list,group,studentList,expandedData);
        listView.setAdapter(expandableMonitorListAdapter);
        expandableMonitorListAdapter.notifyDataSetChanged();

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                student_id=attendanceStatusList.get(groupPosition).getStudent_id();
                student_name=studentList.get(groupPosition).getName();
                status=attendanceStatusList.get(groupPosition).getStatus();
                List<String> latemins=new ArrayList<String>();
                latemins.add(0,"0"); //present
                latemins.add(1,"-1"); //absent
                latemins.add(2,"300"); //5
                latemins.add(3,"600"); //10
                latemins.add(4,"900");//15
                latemins.add(5,"1200");//20
                latemins.add(6,"1500");//25
                latemins.add(7,"1800");//30
                latemins.add(8,"2100");//35
                latemins.add(9,"2400");//40
                latemins.add(10,"2700");//45
                latemins.add(11,"3600");// 1 hour
                latemins.add(12,"5400");

                new_status=latemins.get(childPosition);
                updateStatus();

                return true;
            }
        });
    }


    public void updateStatus(){
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            JsonObject toUp = new JsonObject();
            toUp.addProperty("lesson_date_id",lesson_date_id);
            toUp.addProperty("student_id",student_id);
            toUp.addProperty("status",new_status);

            Call<String> call=client.updateStatus(toUp);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().contains("success")){
                        //get new status & student id
                       /* if(status.equals("1")){
                            Toast.makeText(getActivity().getBaseContext(),"Update attendance status of Student "+student_name+" to Present Successfully",Toast.LENGTH_SHORT).show();

                        }
                        if(status.equals("0")){
                            Toast.makeText(getActivity().getBaseContext(),"Update attendance status of Student "+student_name+" to Absent Successfully",Toast.LENGTH_SHORT).show();

                        }
*/
                        listAttendance();

                    }else if(response.body().contains("failed")){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Failed");
                        builder.setMessage("Update attendance failed.");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, final int i) {
                                        Preferences.clearLecturerInfo();
                                        Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                        builder.create().show();
                    }
                    else {
                        Toast.makeText(getActivity().getBaseContext(),"error",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void listStudent(){
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);
            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            JsonObject toUp = new JsonObject();
            toUp.addProperty("lesson_id",lesson_id);

            Call<List<StudentInfo>> call = client.getStudentList(toUp);
            call.enqueue(new ServerCallBack<List<StudentInfo>>() {
                @Override
                public void onResponse(Call<List<StudentInfo>> call, Response<List<StudentInfo>> response) {
                    Preferences.dismissLoading();
                    try {
                        studentList=response.body();
                        if(studentList==null){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Detect another login.");
                            builder.setMessage("You will automatically sign out. Click here to sign in again.");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                            Preferences.clearLecturerInfo();
                                            Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                            builder.create().show();
                        }
                        else {
                            initStudentlist();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void listAttendance(){
        Preferences.showLoading(getActivity(), "Live Attendance Monitor", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            /*JsonParser parser = new JsonParser();
            lesson_date_id="32277";
            JsonObject object=parser.parse("{" +
                    "\"lesson_date_id\":\""+lesson_date_id+"\""+ "}").getAsJsonObject();*/

            JsonObject object=new JsonObject();
            // 需要输入当前课的lesson_date_id


          //  lesson_date_id="32689";

            object.addProperty("lesson_date_id",lesson_date_id);


            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            Call<List<ListAttendanceStatus>> call=client.getStudentAttendanceStatus(object);
            call.enqueue(new ServerCallBack<List<ListAttendanceStatus>>() {
                @Override
                public void onResponse(Call<List<ListAttendanceStatus>> call, Response<List<ListAttendanceStatus>> response) {
                    try {
                        attendanceStatusList=response.body();
                        if(attendanceStatusList==null){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Not at the time");
                            builder.setMessage("The class haven't begin.");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, final int i) {
                                            Preferences.clearLecturerInfo();
                                            Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                            builder.create().show();



                        }
                        else {
                            initAttendanceList();
                            tvlesson_name.setText(monitors.get(0).getSubjectarea()+" "+monitors.get(0).getModule());
                            tvclass_section.setText(monitors.get(0).getClass_section());
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
        myView= inflater.inflate(R.layout.fragment_monitor_list, container, false);

        tvlesson_name = (TextView)myView.findViewById(R.id.tvlesson_name);
        tvclass_section = (TextView)myView.findViewById(R.id.tvclass_section);


        manager = new DatabaseManager(getActivity());


        monitors = manager.getMonitor();//det data
        if(monitors.size()!=0){
            String Ldate=monitors.get(0).getLdate();
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tDate = sDateFormat.format(new java.util.Date());
            lesson_date_id=monitors.get(0).getLesson_date_id();

        }else {
            lesson_date_id="";
        }

       // lesson_date_id="32699"; //没有attendance list的课 未来的课


        listAttendance();



        return myView;
    }



}
