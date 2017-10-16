package edu.np.ece.ame_android_lecturer.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.LessonDate;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
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

    public void LoadList(){
        //select timeslot & view history date
        Bundle arguments = getArguments();
        if(arguments!=null){
            lesson_id=arguments.getString("lesson_id");
        }
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
