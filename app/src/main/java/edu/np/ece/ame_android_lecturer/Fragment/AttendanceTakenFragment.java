package edu.np.ece.ame_android_lecturer.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.np.ece.ame_android_lecturer.BeaconScanActivation;
import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import edu.np.ece.ame_android_lecturer.OrmLite.DatabaseManager;
import edu.np.ece.ame_android_lecturer.OrmLite.Subject;
import edu.np.ece.ame_android_lecturer.OrmLite.SubjectDateTime;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;


public class AttendanceTakenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<String> datas = new ArrayList<String>();

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
    Button tvEntire;

    private Handler mHandler;
    public static BeaconTransmitter beaconTransmitter;
    public static Beacon.Builder beaconBuilder;
    private String aId;
    private String aDate;



    private OnFragmentInteractionListener mListener;

    public AttendanceTakenFragment() {

    }



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
        try{
           context = this.getActivity();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflateView = inflater.inflate(R.layout.fragment_attendance_taken,container,false);

        ButterKnife.bind(this,inflateView);

        SharedPreferences pref = getActivity().getSharedPreferences(Preferences.SharedPreferencesTag,Preferences.SharedPreferences_ModeTag);

        String isLogin = pref.getString("isLogin","false");
        String isLecturer = pref.getString("isLecturer","true");

        if(isLogin.equals("true") && isLecturer.equals("true")){
            String lecturerName = pref.getString("lecturer_name","");


            if(BeaconScanActivation.timetableResultList != null){

                for (final TimetableResult aSubject_time : BeaconScanActivation.timetableResultList ){
                    String aTime = aSubject_time.getLesson_date() + " " + aSubject_time.getLesson().getEnd_time();
                    aId = aSubject_time.getLesson_id();
                    List<Subject>subjectList = DatabaseManager.getInstance().QueryBuilder("lesson_id",aId);
                    List<SubjectDateTime>subjectDateTimeList = subjectList.get(0).getSubject_Datetime();
                }
            }
        }

        return inflater.inflate(R.layout.fragment_attendance_taken, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
