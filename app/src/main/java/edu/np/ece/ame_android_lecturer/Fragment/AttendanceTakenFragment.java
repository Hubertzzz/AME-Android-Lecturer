package edu.np.ece.ame_android_lecturer.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Adapter.MonitorListAdapter;
import edu.np.ece.ame_android_lecturer.Model.Lesson;
import edu.np.ece.ame_android_lecturer.Model.LessonDate;
import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.OrmLite.DatabaseManager;
import edu.np.ece.ame_android_lecturer.OrmLite.Monitor;
import edu.np.ece.ame_android_lecturer.OrmLite.Subject;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link AttendanceTakenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceTakenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<TimetableResult> timetableList;

    private ArrayList<String> datas = new ArrayList<>();

    private List LessonDateList;

    private Activity context;
    private View myView;
    private String aMo;
    private List<LessonDate> lessonDatesResult;
    private List<LessonDate > weeklydate=new ArrayList<>();
    private List<Lesson> weeklylesson= new ArrayList<>();
    FragmentManager fragmentManager;


    private Handler mHandler;
    public static BeaconTransmitter beaconTransmitter;
    public static Beacon.Builder beaconBuilder;
    private String aID;
    private String aDate;
    private String aClass;
    private String aModule;
    private String aModuleSec;
    private String Ldate;



    //private String aStartTime,aEndTime;

    // TODO: Rename and change types of parameters


    public AttendanceTakenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceTakenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceTakenFragment newInstance(String param1, String param2) {
        AttendanceTakenFragment fragment = new AttendanceTakenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
    }

    private void loadInformation(){
        final TextView tvModule = (TextView)myView.findViewById(R.id.tvModule);
        final TextView tvClass = (TextView)myView.findViewById(R.id.tvClass);
        final TextView tvTime = (TextView)myView.findViewById(R.id.tvTime);
        final TextView tvVenue = (TextView)myView.findViewById(R.id.tvVenue);
        final Button btnStop = (Button)myView.findViewById(R.id.btnStop);
        final TextView tvInfo = (TextView)myView.findViewById(R.id.tvInfo);
        Preferences.showLoading(context,"Now Class","Loading data from server...");
        try{
            final SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag,Preferences.SharedPreferences_ModeTag);
            String auCode = pref.getString("authorizationCode",null);
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final String tDate = sDateFormat.format(new java.util.Date());

            final String userMajor = pref.getString("major","");
            final String userMinor = pref.getString("minor","");


            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            String aStartTime,aEndTime;

            String expand = new String("lesson,venue,lesson_date,beacon_lesson");
            Call<List<TimetableResult>> call = client.getTimetableCurrentWeek(expand);
            call.enqueue(new ServerCallBack<List<TimetableResult>>() {
                @Override
                public void onResponse(Call<List<TimetableResult>> call, Response<List<TimetableResult>> response) {
                    try{
                        timetableList = response.body();
                        if(timetableList != null){
                            /*String aModuleSec = String.valueOf(timetableList.get(0).getLesson().getSubject_area());
                            String aModule = String.valueOf(timetableList.get(0).getLesson().getCatalog_number());
                            datas.add(aModuleSec+" "+aModule);
                            tvModule.setText(aModuleSec+" "+aModule);*/

                         //   AddLessondate();


                            for(int i = 0 ; i < timetableList.size(); i++){
                                for(int e=0; e< timetableList.get(i).getLesson_date().size();e++){
                                    if(tDate.equals(timetableList.get(i).getLesson_date().get(e).getLdate())){
                                        aModuleSec = String.valueOf(timetableList.get(i).getLesson().getSubject_area());
                                        aModule = String.valueOf(timetableList.get(i).getLesson().getCatalog_number());
                                        tvModule.setText(aModuleSec + " " + aModule);
                                        aMo = aModuleSec +" "+aModule;

                                        datas.add(aModuleSec + " " + aModule);



                                        aClass = String.valueOf(timetableList.get(i).getLesson().getClass_section());
                                        tvClass.setText(aClass);
                                        datas.add(aClass);


                                        Ldate = timetableList.get(i).getLesson_date().get(e).getLdate();

                                       /* Fragment fragment=new MonitorListFragment();
                                        Bundle bundle= new Bundle();
                                        bundle.putString("module",aModule);
                                        bundle.putString("class",aClass);
                                        fragment.setArguments(bundle);*/

                                       /* SharedPreferences.Editor edt=pref.edit();
                                        // editor.putString("lesson_date_id",);
                                        edt.putString("module",aModule);
                                        edt.putString("class",aClass);
                                        edt.commit();*/
                                        String date=timetableList.get(i).getLesson_date().get(e).getId();

                                        DatabaseManager monitorDao= new DatabaseManager(getActivity());

                                        Monitor monitor=new Monitor();
                                        monitorDao.deleteMonitor(); //clear odd
                                        monitor.setModule(aModule);
                                        monitor.setSubjectarea(aModuleSec);
                                        monitor.setClass_section(aClass);
                                        monitor.setLesson_date_id(date);
                                    //    monitor.setLesson_date(Ldate);
                                        monitorDao.addMonitor(monitor);

                                       /* DatabaseManager manager=new DatabaseManager(getActivity());
                                        Monitor monitor1=new Monitor();
                                     //   manager.deleteMonitor(); //clear odd data
                                        List<Monitor> monitors= manager.getMonitor(); //det data*/





                                        String aStartTime = String.valueOf(timetableList.get(i).getLesson().getStart_time());
                                        String aEndTime = String.valueOf(timetableList.get(i).getLesson().getEnd_time());
                                        tvTime.setText(aStartTime+" - "+aEndTime);
                                        datas.add(aStartTime);
                                        datas.add(aEndTime);
                                        String aVenue = String.valueOf(timetableList.get(i).getVenue().getLocation());
                                        tvVenue.setText("#"+aVenue);
                                        datas.add(aVenue);


                                        Date TimeNow = new Date();
                                        Calendar calendar1 = Calendar.getInstance();
                                        calendar1.setTime(TimeNow);

                                        String sTime = tDate + " "+ aEndTime;
                                        Date cTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sTime);
                                        Calendar calendar2 = Calendar.getInstance();
                                        calendar2.setTime(cTime);

                                        if(calendar1.getTime().before(calendar2.getTime())){

                                            beaconBuilder = new Beacon.Builder();
                                            beaconBuilder.setId1(timetableList.get(i).getLessonBeacon().getUuid());
                                            beaconBuilder.setId2(userMajor);
                                            beaconBuilder.setId3(userMinor);

                                            beaconBuilder.setManufacturer(0x004C);//apple company code
                                            beaconBuilder.setTxPower(-59);
                                            BeaconParser beaconParser = new BeaconParser()
                                                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
                                            beaconTransmitter = new BeaconTransmitter(getActivity().getBaseContext(),beaconParser);
                                            break;
                                        }


                                    }

                                }




                            }



                        }
                       /* if (beaconTransmitter != null && beaconBuilder != null){
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    beaconTransmitter.startAdvertising(beaconBuilder.build());
                                }
                            },360000);*/

                        //}
                        beaconTransmitter.startAdvertising(beaconBuilder.build());

                        btnStop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                beaconTransmitter.stopAdvertising();
                                Toast.makeText(context,"Stop Advertising.....",Toast.LENGTH_SHORT).show();
                                tvInfo.setText("Finished Transmiting");
                            }
                        });
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
        Preferences.dismissLoading();
    }


    /*public void AddLessondate(){
        for(int i=0;i<timetableList.size();i++){
            lessonDatesResult=timetableList.get(i).getLesson_date();
            weeklylesson.add(timetableList.get(i).getLesson());
            weeklydate.add(timetableList.get(i).getLesson_date().get(i));

        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_attendance_taken,container,false);
        loadInformation();
        mHandler = new Handler();






        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
