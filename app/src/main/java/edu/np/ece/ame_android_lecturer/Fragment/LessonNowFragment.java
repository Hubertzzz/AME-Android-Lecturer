package edu.np.ece.ame_android_lecturer.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconTransmitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.np.ece.ame_android_lecturer.BeaconScanActivation;
import edu.np.ece.ame_android_lecturer.Model.LessonDate;
import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.OrmLite.DatabaseManager;
import edu.np.ece.ame_android_lecturer.OrmLite.Subject;
import edu.np.ece.ame_android_lecturer.OrmLite.SubjectDateTime;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;

public class LessonNowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<String> datas = new ArrayList<String>();

    public static List<LessonDate> LessonDateList;
    private Activity context;

    private View inflateView;

    @BindView(R.id.tvModule)
    TextView tvModule;

    @BindView(R.id.tvClass)
    TextView tvClass;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.tvVenue)
    TextView tvVenue;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindView(R.id.tvLeft)
    TextView tvLeft;

    @BindView(R.id.btnStop)
    Button btnStop;

    @BindView(R.id.tvEntire)
    TextView tvEntire;

    private Handler mHandler;
    public static BeaconTransmitter beaconTransmitter;
    public static Beacon.Builder beaconBuilder;
    private String aId;
    private String aDate;

    private List ClassDate = new ArrayList();



    public LessonNowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LessonNowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonNowFragment newInstance(String param1, String param2) {
        LessonNowFragment fragment = new LessonNowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            context = this.getActivity();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflateView = inflater.inflate(R.layout.fragment_lesson_now,container,false);

        ButterKnife.bind(this,inflateView);

        SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag,Preferences.SharedPreferences_ModeTag);

        String isLogin = pref.getString("isLogin","false");
        String isLecturer = pref.getString("isLecturer","true");

        if(isLogin.equals("true") && isLecturer.equals("true")){
            String lecturerName = pref.getString("lecturer_name","");


            if(BeaconScanActivation.timetableResultList != null){

                for (final TimetableResult aSubject_time : BeaconScanActivation.timetableResultList ){
                    ClassDate = aSubject_time.getLesson_date();
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNow = sDateFormat.format(new java.util.Date());
                    for (int i = 0; i< ClassDate.size();i++){

                    }
                    String aTime = aSubject_time.getLesson_date() + " " + aSubject_time.getLesson().getEnd_time();
                    aId = aSubject_time.getLesson_id();
                    List<Subject>subjectList = DatabaseManager.getInstance().QueryBuilder("lesson_id",aId);
                    List<SubjectDateTime>subjectDateTimeList = subjectList.get(0).getSubject_Datetime();

                    String sTime = aSubject_time.getLesson_date().get(0)+ " " + aSubject_time.getLesson().getStart_time();




                    String aModuleSec = subjectList.get(0).getSubject_area();
                    String aModule = subjectList.get(0).getCatalog_number();
                    datas.add(aModuleSec + " " + aModule);
                    tvModule.setText(aModuleSec + " " + aModule);

                    String aClass = aSubject_time.getLesson().getClass_section();
                    datas.add(aClass);
                    tvClass.setText(aClass);


                    String cStartTime = subjectDateTimeList.get(0).getStartTime();
                    String cEndTime = subjectDateTimeList.get(0).getEndTime();
                    tvTime.setText(cStartTime + " - " + cEndTime);//显示时间
                    datas.add(cStartTime+ " - "+ cEndTime);

                    String cVenue = subjectList.get(0).getLocation();
                    tvVenue.setText("#" + cVenue);
                    datas.add(cVenue);








                }
            }
        }

        return inflater.inflate(R.layout.fragment_lesson_now, container, false);
    }

}




