package edu.np.ece.ame_android_lecturer.Fragment;

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
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.np.ece.ame_android_lecturer.Adapter.MonitorListAdapter;
import edu.np.ece.ame_android_lecturer.LogInActivity;
import edu.np.ece.ame_android_lecturer.Model.ListAttendanceStatus;
import edu.np.ece.ame_android_lecturer.Preferences;
import edu.np.ece.ame_android_lecturer.R;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerApi;
import edu.np.ece.ame_android_lecturer.Retrofit.ServerCallBack;
import edu.np.ece.ame_android_lecturer.Retrofit.ServiceGenerator;
import retrofit2.Call;
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

    public void initStudentlist(){
        final ListView listView = (ListView) myView.findViewById(R.id.monitorlist);

        //把stu_id按照递增排序
        Collections.reverse(attendanceStatusList);

        MonitorListAdapter monitorListAdapter = new MonitorListAdapter(getActivity(),R.layout.item_monitor_list,attendanceStatusList);
        listView.setAdapter(monitorListAdapter);
        monitorListAdapter.notifyDataSetChanged();

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
            lesson_date_id="32277";

            object.addProperty("lesson_date_id",lesson_date_id);

            // 需要输入当前课的lesson_date_id
            ServerApi client = ServiceGenerator.createService(ServerApi.class, auCode);
            Call<List<ListAttendanceStatus>> call=client.getStudentAttendanceStatus(object);
            call.enqueue(new ServerCallBack<List<ListAttendanceStatus>>() {
                @Override
                public void onResponse(Call<List<ListAttendanceStatus>> call, Response<List<ListAttendanceStatus>> response) {
                    try {
                        attendanceStatusList=response.body();
                        if(attendanceStatusList==null){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("ERROR");
                            builder.setMessage("Cannot find student list");
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
                            initStudentlist();
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
        listAttendance();
        return myView;
    }



}
