package edu.np.ece.ame_android_lecturer.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Adapter.ExpandableMonitorListAdapter;
import edu.np.ece.ame_android_lecturer.Adapter.MonitorListAdapter;
import edu.np.ece.ame_android_lecturer.LogInActivity;
import edu.np.ece.ame_android_lecturer.Model.LessonDate;
import edu.np.ece.ame_android_lecturer.Model.ListAttendanceStatus;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.NavigationActivity;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AttendanceHistoryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View myView;
    private String lesson_id;
    private List<LessonDate> dateList;
    private List<String> ldatelist=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    ExpandableMonitorListAdapter monitorListAdapter;
    private  String lesson_date_id;
    private List<ListAttendanceStatus> attendanceStatusList;

    private List<StudentInfo> studentList;
    private String student_id;
    private String status;
    private String new_status;
    private String student_name;
    private String comment;

    List<List<String>> expandedData;
    List<ListAttendanceStatus> group;
    List<String> choice;


    public AttendanceHistoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceHistoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceHistoryListFragment newInstance(String param1, String param2) {
        AttendanceHistoryListFragment fragment = new AttendanceHistoryListFragment();
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


    public void initdatelist(){
        //把得到的datelist中的getldate（）放入dropbox
       final Spinner spinner=(Spinner)myView.findViewById(R.id.sp_timeslot);
        for(int i=0;i<dateList.size();i++){

            ldatelist.add(dateList.get(i).getLdate());


        }
        //ldatelist.size() 还没有签到记录的话，spinner里应该什么也不显示

            arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, ldatelist);
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //show list
                    // final ListView listView=(ListView)myView.findViewById(R.id.list_timeslot);
                    lesson_date_id = dateList.get(position).getId();
                    //api call
                    Callhistorylist();
                    // monitorListAdapter=new MonitorListAdapter(getActivity(),R.layout.item_monitor_list,);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



    }
    public void Callhistorylist(){
        Preferences.showLoading(getActivity(), "Attendance List", "Loading data from server...");
        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            JsonObject object=new JsonObject();
            object.addProperty("lesson_date_id",lesson_date_id);
         //   object.addProperty("lesson_date_id",32694);
            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            Call<List<ListAttendanceStatus>> call=client.getStudentAttendanceStatus(object);
            call.enqueue(new ServerCallBack<List<ListAttendanceStatus>>() {
                @Override
                public void onResponse(Call<List<ListAttendanceStatus>> call, Response<List<ListAttendanceStatus>> response) {
                    try {
                        attendanceStatusList=response.body();
                        if(attendanceStatusList==null){
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
                        }
                        else {
                           //get student list

                            listStudent();
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
    public void listStudent(){

        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);
            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            JsonObject toUp = new JsonObject();
            toUp.addProperty("lesson_id",lesson_id);
          //  toUp.addProperty("lesson_id",140);
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
    public void addgroup(){
        for(int i=0;i<attendanceStatusList.size();i++){
            group.add(attendanceStatusList.get(i));
            expandedData.add(choice);//每有一个group item 就新添加一组choice数组
        }

    }

    public void initStudentlist(){
        final ExpandableListView listView = (ExpandableListView) myView.findViewById(R.id.list_timeslot);


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


        Collections.reverse(attendanceStatusList);
        monitorListAdapter = new ExpandableMonitorListAdapter(getActivity(),R.layout.item_monitor_list,attendanceStatusList,studentList,expandedData);
        listView.setAdapter(monitorListAdapter);
        monitorListAdapter.notifyDataSetChanged();

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
                PopUpDialogBox();
          //      Toast.makeText(getActivity().getBaseContext(),attendanceStatusList.get(groupPosition).getStudent_id(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void PopUpDialogBox(){
        final EditText inputServer = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Comment Field").setIcon(android.R.drawable.ic_menu_edit).setView(inputServer)
                .setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                comment=inputServer.getText().toString();
                updateStatus();

            }

        });
        builder.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInputMethod();
            }
        },100);

    }
    private void showInputMethod() {
        //自动弹出键盘
        InputMethodManager inputManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //强制隐藏Android输入法窗口
        // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }

    public void updateStatus(){
        try {
            Preferences.showLoading(getActivity(), "Comment Field", "Sending comment to server...");

            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);

            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            JsonObject toUp = new JsonObject();
            toUp.addProperty("lesson_date_id",lesson_date_id);
            toUp.addProperty("student_id",student_id);
            toUp.addProperty("status",new_status);
            toUp.addProperty("remark",comment);
            Call<String> call=client.updateStatus(toUp);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body().contains("success")){

                        LoadList();


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
        Preferences.dismissLoading();
        Toast.makeText(getActivity().getBaseContext(),"Update ["+student_name+"] 's attendance successfully. ",Toast.LENGTH_SHORT).show();
    }

    public void LoadList(){
        //select timeslot & view history date

        Intent i = getActivity().getIntent();
        lesson_id= i.getStringExtra("lesson_id");
        /*Bundle arguments = getArguments();
        if(arguments!=null){
            lesson_id=arguments.getString("lesson_id");
        }*/

        try {
            SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag, Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode", null);


            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            String expand=lesson_id;
            Call<List<LessonDate>> call=client.getAllDateOfaLesson(expand);
            call.enqueue(new ServerCallBack<List<LessonDate>>() {
                @Override
                public void onResponse(Call<List<LessonDate>> call, Response<List<LessonDate>> response) {
                    try {
                        dateList=response.body();
                        if(dateList==null){
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
                        }else {
                            initdatelist();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_attendance_history_list, container, false);
        LoadList();
        return myView;
    }




}
